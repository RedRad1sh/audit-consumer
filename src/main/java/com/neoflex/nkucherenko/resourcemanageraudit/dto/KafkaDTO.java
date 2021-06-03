package com.neoflex.nkucherenko.resourcemanageraudit.dto;

import lombok.Data;

@Data
public class KafkaDTO {
    String action;
    String date;
    String sourceEntity;
    String modifiedEntity;
}
