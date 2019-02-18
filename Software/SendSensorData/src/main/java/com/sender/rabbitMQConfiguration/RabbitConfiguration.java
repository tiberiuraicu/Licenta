package com.sender.rabbitMQConfiguration;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sender.constants.Constants;
import com.sender.rabbitMQSender.DataInfoSender;

@Configuration
public class RabbitConfiguration {
	
	//create the exchange in which is the data sent to raspberry
	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(Constants.EXCHANGE_NAME);
	}
	
	//creating rabbitMQ broker connection
	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory conneFactory = new CachingConnectionFactory();
		conneFactory.setHost(Constants.HOST);
		conneFactory.setPort(Constants.RABBITMQ_PORT);
		conneFactory.setUsername(Constants.RABBITMQ_USERNAME);
		conneFactory.setPassword(Constants.RABBITMQ_PASSWORD);
		return conneFactory;
	}
	
	//create rabbitMQ template which has send functions
	@Bean
	public RabbitTemplate templateGet() {
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	@Bean
	public DataInfoSender dataInfoSender() {
		return new DataInfoSender();
	}
}