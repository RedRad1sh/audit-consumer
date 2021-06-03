package com.neoflex.nkucherenko.resourcemanageraudit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.nkucherenko.resourcemanageraudit.dto.KafkaDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

import static java.util.Arrays.asList;

@Service
@Slf4j
public class KafkaAuditConsumer {
    @Value("${kafka.server}")
    private String server = "";

    @Value("${kafka.topic}")
    private String topicName = "";

    @Value("${kafka.username}")
    private String username = "";

    @Value("${kafka.password}")
    private String password = "";

    @Value("${kafka.group}")
    private String group = "";

    private Consumer<String, String> getConsumer() {
        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                server);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_uncommitted");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("request.timeout.ms", "30000");
        props.put("ssl.endpoint.identification.algorithm", "https");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + username + "\" password=\"" + password + "\";");
        return new KafkaConsumer<>(props);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void readMessages() throws Exception {
        Consumer<String, String> consumer = getConsumer();
        consumer.subscribe(asList(topicName));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
        List<KafkaDTO> kafkaDTOList = new ArrayList<>();
        if (!consumerRecords.isEmpty()) {
            Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord iteratorRecord = iterator.next();
                kafkaDTOList.add(extractInfoFromJson(iteratorRecord));
            }
        }
        consumer.commitAsync();
        consumer.close();
    }

    private KafkaDTO extractInfoFromJson(ConsumerRecord<String, String> iteratorRecord) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaDTO kafkaDTO = objectMapper.readValue(iteratorRecord.value().toString(), KafkaDTO.class);
        return kafkaDTO;
    }

}