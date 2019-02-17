package com.sender.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("config.properties")
public class Constants {
	@Value("${SENSOR_DATA_CSV}")
	public static String SENSOR_DATA_CSV="SensorData.csv";
	@Value("${EXCHANGE_NAME}")
	public static String EXCHANGE_NAME="direct_messages";
	@Value("${EXCHANGE_TYPE}")
	public static String EXCHANGE_TYPE="direct";
	@Value("${HOST}")
	public static String HOST="localhost";
	@Value("${RABBITMQ_USERNAME}")
	public static String RABBITMQ_USERNAME="test";
	@Value("${RABBITMQ_PASSWORD}")
	public static String RABBITMQ_PASSWORD="test";
	@Value("${CONSUMER_KEY}")
	public static String CONSUMER_KEY;
	@Value("${RABBITMQ_PORT}")
	public static int RABBITMQ_PORT=5672;
}
