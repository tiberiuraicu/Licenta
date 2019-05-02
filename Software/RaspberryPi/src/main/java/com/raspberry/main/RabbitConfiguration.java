package com.raspberry.main;

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
import com.raspberry.constants.Constants;
import com.raspberry.devicesDataSenderAndReceiver.DevicesDataSender;
import com.raspberry.serverDataSenderAndReceiver.InstructionsSender;

@Configuration
@ComponentScan(basePackages= {"com.raspberry.devicesDataSenderAndReceiver","com.raspberry.serverDataSenderAndReceiver"})
public class RabbitConfiguration {
	Constants c = new Constants();

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

	// create the exchange in which is the data sent to server
	@Bean
	public DirectExchange serverExchange() {
		return new DirectExchange(Constants.SERVER_EXCHANGE_NAME);
	}

	// create the exchange from which the information from sensor is received
	@Bean
	public DirectExchange devicesExchange() {
		return new DirectExchange(Constants.DEVICES_EXCHANGE_NAME);
	}

	// create the queue for getting the outlet information
	@Bean
	@Qualifier("queueOutlet")
	public Queue queueOutlet() {
		return new Queue(c.QUEUE_OUTLET);
	}

	// create the queue for getting the switch information
	@Bean
	@Qualifier("queueSwitch")
	public Queue queueSwitch() {
		return new Queue(Constants.QUEUE_SWITCH);
	}

	// create the queue for getting the sensor information
	@Bean
	@Qualifier("queueSensor")
	public Queue queueSensor() {
		return new Queue(Constants.QUEUE_SENSOR);
	}

	// create the queue for getting the instruction information from server
	@Bean
	@Qualifier("queueInstruction")
	public Queue queueInstruction() {
		return new Queue(Constants.QUEUE_INSTRUCTION);
	}

	// bind the queues(with information from devices) to the specific key
	@Bean
	public Binding bindingOutlet(DirectExchange devicesExchange, Queue queueOutlet) {
		return BindingBuilder.bind(queueOutlet).to(devicesExchange).with(Constants.OUTLET_KEY);
	}

	@Bean
	public Binding bindingSwitch(DirectExchange devicesExchange, Queue queueSwitch) {
		return BindingBuilder.bind(queueSwitch).to(devicesExchange).with(Constants.SWITCH_KEY);
	}

	@Bean
	public Binding bindingSensor(DirectExchange devicesExchange, Queue queueSensor) {
		return BindingBuilder.bind(queueSensor).to(devicesExchange).with(Constants.SENSOR_KEY);
	}
	
	// bind the queue(with instructions from server) to the specific key
	@Bean
	public Binding bindingInstruction(DirectExchange serverExchange, Queue queueInstruction) {
		return BindingBuilder.bind(queueInstruction).to(serverExchange).with(Constants.INSTRUCTION_KEY);
	}

	@Bean
	public DevicesDataSender dataInfoSender() {
		return new DevicesDataSender();
	}
	@Bean
	public InstructionsSender instructionsSender() {
		return new InstructionsSender();
	}
}
