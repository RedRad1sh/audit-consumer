package com.neoflex.nkucherenko.resourcemanageraudit.rest;

import com.neoflex.nkucherenko.resourcemanageraudit.mail.EmailContext;
import com.neoflex.nkucherenko.resourcemanageraudit.mail.DefaultEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class TestMailController {

    private final DefaultEmailService defaultEmailService;

    @PostMapping("/{email}")
    public void sendMail(@PathVariable String email) throws MessagingException {
        Map<String, Object> entities = new HashMap<>();
        entities.put("sourceEntity", "Source");
        entities.put("modifiedEntity", "Modified");
        EmailContext emailContext = EmailContext.builder()
                .from(email)
                .to(email)
                .templateLocation("mail.html")
                .context(entities)
                .subject("Audit: Entity modified")
                .email(email)
                .build();
        defaultEmailService.sendMail(emailContext);
    }
}
