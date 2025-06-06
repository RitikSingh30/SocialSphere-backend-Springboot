package com.socialsphere.socialsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SocialsphereApplication {
	// TODO add debug logging statement in the project
	public static void main(String[] args) {
		SpringApplication.run(SocialsphereApplication.class, args);
	}

}
