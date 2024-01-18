package com.zooting.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ZootingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZootingApplication.class, args);
	}

}
