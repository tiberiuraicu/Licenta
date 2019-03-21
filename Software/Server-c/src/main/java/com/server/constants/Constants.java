package com.server.constants;

import java.io.FileInputStream;
import java.util.Properties;

public class Constants {
	static final Properties prop = new Properties();

	// the exchange for sending and receiving info to/from raspberry
	public static String FACTORY_HOST;

	// rabbitmq broker credentials
	public static String RABBITMQ_USERNAME;
	public static String RABBITMQ_PASSWORD;

	// the keys with which the data from sensors is encrypted
	public static String CONSUMER_KEY;
	public static String SENSOR_KEY;

	// the key with which the instrunctions are encrypted
	public static String INSTRUCTION_KEY;

	//the name of the exchange
	public static String EXCHANGE_NAME;
	
	// Queue through which we receive the consumer data from raspberry
	public static String QUEUE_CONSUMER;
	
	// Queue through which we receive the sensor data from raspberry
	public static String QUEUE_SENSOR;

	static {
		try {
			prop.load(new FileInputStream("src//main//resources//config.properties"));
			
			FACTORY_HOST = prop.getProperty("factory.host");
		
			RABBITMQ_USERNAME = prop.getProperty("rabbit.username");
			RABBITMQ_PASSWORD = prop.getProperty("rabbit.password");
	
			
			CONSUMER_KEY = prop.getProperty("consumer.key");
			SENSOR_KEY = prop.getProperty("sensor.key");
			
			INSTRUCTION_KEY = prop.getProperty("instruction.key");
			
			EXCHANGE_NAME = prop.getProperty("exchange");

			QUEUE_CONSUMER = prop.getProperty("queue.consumer");
			QUEUE_SENSOR = prop.getProperty("queue.sensor");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
