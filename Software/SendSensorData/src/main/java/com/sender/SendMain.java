package com.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.sender.rabbitMQSender.DataInfoSenderToRaspberry;

@SpringBootApplication
@EnableScheduling
public class SendMain implements CommandLineRunner {
	
	@Autowired
	DataInfoSenderToRaspberry dataInfoSenderToRaspberry;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SendMain.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// continuously send data to raspberry
		while (true) {

			dataInfoSenderToRaspberry.sendData();

		}
	}
}
