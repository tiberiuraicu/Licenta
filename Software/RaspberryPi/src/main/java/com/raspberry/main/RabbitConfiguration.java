package com.raspberry.main;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.raspberry.constants.Constants;

@Configuration
public class RabbitConfiguration {
	
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
 
	//create the exchange in which is the data sent to server
	@Bean
	public DirectExchange senderExchange() {
		return new DirectExchange(Constants.SENDER_EXCHANGE_NAME);
	}
	
	//create the exchange from which the information from sensor is received
	@Bean
	public DirectExchange receiverExchange() {
		return new DirectExchange(Constants.RECEIVER_EXCHANGE_NAME);
	}

	//create the queue for getting the outlet information 
	@Bean
	@Qualifier("queueOutlet")
	public Queue queueOutlet() {
		return new Queue(Constants.QUEUE_OUTLET);
	}

	//create the queue for getting the switch information 
	@Bean
	@Qualifier("queueSwitch")
	public Queue queueSwitch() {
		return new Queue(Constants.QUEUE_SWITCH);
	}

	//create the queue for getting the sensor information 
	@Bean
	@Qualifier("queueSensor")
	public Queue queueSensor() {
		return new Queue(Constants.QUEUE_SENSOR);
	}
 
	//bind the queues to the specific key
	@Bean
	public Binding bindingOutlet(DirectExchange receiverExchange, Queue queueOutlet) {
		return BindingBuilder.bind(queueOutlet).to(receiverExchange).with(Constants.OUTLET_KEY);
	}

	@Bean
	public Binding bindingSwitch(DirectExchange receiverExchange, Queue queueSwitch) {
		return BindingBuilder.bind(queueSwitch).to(receiverExchange).with(Constants.SWITCH_KEY);
	}

	@Bean
	public Binding bindingSensor(DirectExchange receiverExchange, Queue queueSensor) {
		return BindingBuilder.bind(queueSensor).to(receiverExchange).with(Constants.SENSOR_KEY);
	}

	@Bean
	public DataInfoSender dataInfoSender() {
		return new DataInfoSender();
	}
}
