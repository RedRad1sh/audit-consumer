package com.neoflex.nkucherenko.resourcemanageraudit;

import com.neoflex.nkucherenko.resourcemanageraudit.mail.DefaultEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceManagerAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceManagerAuditApplication.class, args);
	}

}
