package com.sender.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.sender.rabbitMQConfiguration.RabbitConfiguration;
import com.sender.rabbitMQSender.DataInfoSender;

@EnableScheduling
public class SendMain {

	public static void main(String[] args) throws Exception {
		
		//continuously send data to raspberry
		while (true) {
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
					new Class[] { RabbitConfiguration.class });
			DataInfoSender dataInfoSender = (DataInfoSender) context.getBean("dataInfoSender");
			
			dataInfoSender.sendData();
			
			context.close();		
		}
	}
}
