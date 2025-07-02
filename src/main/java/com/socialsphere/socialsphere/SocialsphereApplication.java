package com.socialsphere.socialsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMongoAuditing
@EnableTransactionManagement
public class SocialsphereApplication {
	// TODO add debug logging statement in the project
	public static void main(String[] args) {
		SpringApplication.run(SocialsphereApplication.class, args);
	}

}
