package com.raspberry.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaspberryPIStart{
	
	public static void main(String[] args) {
		SpringApplication.run(RaspberryPIStart.class, args);
	}
}