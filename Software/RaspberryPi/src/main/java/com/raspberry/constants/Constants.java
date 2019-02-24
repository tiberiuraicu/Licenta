package com.raspberry.constants;

import java.io.FileInputStream;
import java.util.Properties;

public class Constants {
	static final Properties prop = new Properties();
	// the exchange for sending info to server
	public static String SERVER_EXCHANGE_NAME;

	// the exchange for receiving data from sensors
	public static String DEVICES_EXCHANGE_NAME;

	// exchange type
	public static String EXCHANGE_TYPE;

	// exchange host
	public static String HOST;

	// rabbitmq broker credentials
	public static String RABBITMQ_USERNAME;
	public static String RABBITMQ_PASSWORD;
	public static int RABBITMQ_PORT;

	// the key with which the data sent to server is encrypted
	public static String CONSUMER_KEY;

	// the queues through which is received the data from sensors
	public static String QUEUE_OUTLET;
	public static String QUEUE_SWITCH;
	public static String QUEUE_SENSOR;

	// the keys with which the data received to from sensors is encrypted
	public static String OUTLET_KEY;
	public static String SWITCH_KEY;
	public static String SENSOR_KEY;
	
	//Queue through which we receive the instrunctions from server
	public static String QUEUE_INSTRUCTION;

	//the key with which the instrunctions from server are encrypted
	public static String INSTRUCTION_KEY;

	static {
		try {
			prop.load(new FileInputStream("config.config"));

			SERVER_EXCHANGE_NAME = prop.getProperty("SERVER_EXCHANGE_NAME");

			DEVICES_EXCHANGE_NAME = prop.getProperty("DEVICES_EXCHANGE_NAME");

			EXCHANGE_TYPE = prop.getProperty("EXCHANGE_TYPE");

			HOST = prop.getProperty("HOST");

			RABBITMQ_USERNAME = prop.getProperty("RABBITMQ_USERNAME");
			RABBITMQ_PASSWORD = prop.getProperty("RABBITMQ_PASSWORD");
		    RABBITMQ_PORT = Integer.parseInt(prop.getProperty("RABBITMQ_PORT"));

			CONSUMER_KEY = prop.getProperty("CONSUMER_KEY");

			QUEUE_OUTLET = prop.getProperty("QUEUE_OUTLET");
			QUEUE_SWITCH = prop.getProperty("QUEUE_SWITCH");
			QUEUE_SENSOR = prop.getProperty("QUEUE_SENSOR");

			OUTLET_KEY = prop.getProperty("OUTLET_KEY");
			SWITCH_KEY = prop.getProperty("SWITCH_KEY");
			SENSOR_KEY = prop.getProperty("SENSOR_KEY");
			
			QUEUE_INSTRUCTION=prop.getProperty("QUEUE_INSTRUCTION");
			
			INSTRUCTION_KEY=prop.getProperty("INSTRUCTION_KEY");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
