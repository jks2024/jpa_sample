package com.human.jpa_sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class JpaSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaSampleApplication.class, args);
	}

}
