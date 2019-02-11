package com.sender.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:config.config")
public class CEPConstants {
	@Value("${SENSOR_DATA_CSV}")
	public static String SENSOR_DATA_CSV="SensorData.csv";
	@Value("${EXCHANGE_NAME}")
	public static String EXCHANGE_NAME;
	@Value("${EXCHANGE_TYPE}")
	public static String EXCHANGE_TYPE;
	@Value("${HOST}")
	public static String HOST;
	@Value("${RABBITMQ_USERNAME}")
	public static String RABBITMQ_USERNAME;
	@Value("${RABBITMQ_PASSWORD}")
	public static String RABBITMQ_PASSWORD;
	@Value("${CONSUMATOR_KEY}")
	public static String CONSUMATOR_KEY;

	
}
