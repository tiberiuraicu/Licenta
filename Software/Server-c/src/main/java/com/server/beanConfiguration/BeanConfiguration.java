package com.server.beanConfiguration;

import org.apache.log4j.Logger;
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
import org.springframework.scheduling.annotation.EnableAsync;
import com.server.constants.Constants;
import com.server.devicesDataReceiver.Receiver;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.processing.CEP.CEPFunctions;
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.MqReceiver.MqReceiverFunctions;
import com.server.processing.REST.RestMapPageFunctions;
import com.server.processing.REST.HomePageFunctions;
import com.server.processing.REST.AuthentificationFunctions;
import com.server.socket.NotificationBroadcaster;

@Configuration
@ComponentScan(basePackages = "com.server")
@EnableAsync
public class BeanConfiguration {

	final static Logger logger = Logger.getLogger(BeanConfiguration.class);

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory conn = new CachingConnectionFactory();
		conn.setHost(Constants.FACTORY_HOST);
		conn.setUsername(Constants.RABBITMQ_USERNAME);
		conn.setPassword(Constants.RABBITMQ_PASSWORD);
		return conn;
	}

	@Bean
	public RabbitTemplate templateGet() {
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setUseDirectReplyToContainer(false);
		return template;
	}

	@Bean
	@Qualifier("queueConsumer")
	public Queue queueConsumer() {
		return new Queue(Constants.QUEUE_CONSUMER);
	}

	@Bean
	@Qualifier("queueSensor")
	public Queue queueSensor() {
		return new Queue(Constants.QUEUE_SENSOR);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(Constants.EXCHANGE_NAME);
	}

	@Bean
	public Binding bindingConsumator(DirectExchange exchange, Queue queueConsumer) {
		return BindingBuilder.bind(queueConsumer).to(exchange).with(Constants.CONSUMER_KEY);
	}

	@Bean
	public Binding bindingSensor(DirectExchange exchange, Queue queueSensor) {
		return BindingBuilder.bind(queueSensor).to(exchange).with(Constants.SENSOR_KEY);
	}

	@Bean
	public Receiver receiver() {
		return new Receiver();
	}

	@Bean
	public CEPFunctions CEPFunctions() {
		return new CEPFunctions();
	}

	@Bean
	public InstructionsSender instructionsSender() {
		return new InstructionsSender();
	}

	@Bean
	public DatabaseFunctions databaseFunctions() {
		return new DatabaseFunctions();
	}

	@Bean
	public MqReceiverFunctions receiverFunctions() {
		return new MqReceiverFunctions();
	}

	@Bean
	public AuthentificationFunctions authentificationFunctions() {
		return new AuthentificationFunctions();
	}

	@Bean
	public RestMapPageFunctions restMapPageFunctions() {
		return new RestMapPageFunctions();
	}

	@Bean
	public HomePageFunctions homePageFunctions() {
		return new HomePageFunctions();
	}

	@Bean
	public NotificationBroadcaster notificationBroadcaster() {
		return new NotificationBroadcaster();
	}
}