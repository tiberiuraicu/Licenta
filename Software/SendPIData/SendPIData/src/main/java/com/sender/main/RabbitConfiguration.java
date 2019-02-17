package com.sender.main;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sender.constants.Constants;

@Configuration
public class RabbitConfiguration {
	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(Constants.EXCHANGE_NAME);
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory conneFactory = new CachingConnectionFactory();
		conneFactory.setHost(Constants.HOST);
		conneFactory.setPort(Constants.RABBITMQ_PORT);
		conneFactory.setUsername(Constants.RABBITMQ_USERNAME);
		conneFactory.setPassword(Constants.RABBITMQ_PASSWORD);
		return conneFactory;
	}

	@Bean
	public RabbitTemplate templateGet() {
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	@Bean
	public DataInfoSender procesorMessage() {
		return new DataInfoSender();
	}
}
