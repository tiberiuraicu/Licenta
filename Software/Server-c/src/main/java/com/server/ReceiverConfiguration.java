package com.server;


import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages="com.server.Server")
@PropertySource("classpath:config.properties")
public class ReceiverConfiguration {
	@Value("${factory.host}")
	String host;
	@Value("${rabbit.username}")
	String rabbitUsername;
	@Value("${rabbit.password}")
	String rabbitPassword;
	@Value("${exchange}")
	String exchange;
	@Value("${queue.consumer}")
	String queueConsumer;
	@Value("${consumer.key}")
	String consumerKey;

	final static Logger logger = Logger.getLogger(ReceiverConfiguration.class);

		@Bean
		public CachingConnectionFactory connectionFactory() {
			CachingConnectionFactory conn = new CachingConnectionFactory();

			conn.setHost(host);
			conn.setUsername(rabbitUsername);
			conn.setPassword(rabbitPassword);
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
			return new Queue(queueConsumer);
		}

		
		@Bean
		public DirectExchange exchange() {
			return new DirectExchange(exchange);
		}

	
		@Bean
		public Binding bindingConsumator(DirectExchange exchange, Queue queueConsumer) {
			return BindingBuilder.bind(queueConsumer).to(exchange).with(consumerKey);
		}

		@Bean
		public Receiver receiver() {
			return new Receiver();
		}
		

	

}