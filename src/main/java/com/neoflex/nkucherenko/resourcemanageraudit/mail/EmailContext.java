package com.neoflex.nkucherenko.resourcemanageraudit.mail;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class EmailContext {
    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;
}
