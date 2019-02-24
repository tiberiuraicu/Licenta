package com.sender.constants;

import java.io.FileInputStream;
import java.util.Properties;

public class Constants {
	static final Properties prop = new Properties();

	// path to CSV which contains sensors and consumers data
	public static String SENSOR_DATA_CSV;

	// the exchange for sending and receiving info to/from raspberry
	public static String EXCHANGE_NAME;

	// exchange type
	public static String EXCHANGE_TYPE;

	// exchange host
	public static String HOST;

	// rabbitmq broker credentials
	public static String RABBITMQ_USERNAME;
	public static String RABBITMQ_PASSWORD;
	public static int RABBITMQ_PORT;

	// the keys with which the data from sensors is encrypted
	public static String OUTLET_KEY;
	public static String SWITCH_KEY;
	public static String SENSOR_KEY;

	// the key with which the instrunctions are encrypted
	public static String INSTRUCTION_KEY;

	// Queue through which we receive the instrunctions from raspberry
	public static String QUEUE_INSTRUCTION;

	static {
		try {
			prop.load(new FileInputStream("config.config"));

			SENSOR_DATA_CSV = prop.getProperty("SENSOR_DATA_CSV");

			EXCHANGE_NAME = prop.getProperty("EXCHANGE_NAME");

			EXCHANGE_TYPE = prop.getProperty("EXCHANGE_TYPE");

			HOST = prop.getProperty("HOST");

			RABBITMQ_USERNAME = prop.getProperty("RABBITMQ_USERNAME");
			RABBITMQ_PASSWORD = prop.getProperty("RABBITMQ_PASSWORD");
			RABBITMQ_PORT = Integer.parseInt(prop.getProperty("RABBITMQ_PORT"));

			OUTLET_KEY = prop.getProperty("OUTLET_KEY");
			SWITCH_KEY = prop.getProperty("SWITCH_KEY");
			SENSOR_KEY = prop.getProperty("SENSOR_KEY");

			INSTRUCTION_KEY = prop.getProperty("INSTRUCTION_KEY");

			QUEUE_INSTRUCTION = prop.getProperty("QUEUE_INSTRUCTION");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
