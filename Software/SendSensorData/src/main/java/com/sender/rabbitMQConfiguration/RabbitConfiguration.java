package com.sender.rabbitMQConfiguration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.sender.constants.Constants;
import com.sender.rabbitMQReceiver.InstructionsReceiverFromRaspberry;
import com.sender.rabbitMQSender.DataInfoSenderToRaspberry;

@Configuration
@ComponentScan(basePackages= {"com.sender","com.sender.rabbitMQSender","com.sender.rabbitMQReceiver"})
public class RabbitConfiguration {

	// create the exchange in which is the data sent to raspberry
	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(Constants.EXCHANGE_NAME);
	}

	@Bean
	@Qualifier("queueInstructions")
	public Queue queueConsumer() {
		return new Queue(Constants.QUEUE_INSTRUCTION);
	}

	@Bean
	public Binding bindingConsumator(DirectExchange exchange, Queue queueInstructions) {
		return BindingBuilder.bind(queueInstructions).to(exchange).with(Constants.INSTRUCTION_KEY);
	}

	// creating rabbitMQ broker connection
	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory conneFactory = new CachingConnectionFactory();
		conneFactory.setHost(Constants.HOST);
		conneFactory.setPort(Constants.RABBITMQ_PORT);
		conneFactory.setUsername(Constants.RABBITMQ_USERNAME);
		conneFactory.setPassword(Constants.RABBITMQ_PASSWORD);
		return conneFactory;
	}

	// create rabbitMQ template which has send functions
	@Bean
	public RabbitTemplate templateGet() {
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	@Bean
	public DataInfoSenderToRaspberry dataInfoSenderToRaspberry() {
		return new DataInfoSenderToRaspberry();
	}

	@Bean
	public InstructionsReceiverFromRaspberry instructionsReceiverFromRaspberry() {
		return new InstructionsReceiverFromRaspberry();
	}
}