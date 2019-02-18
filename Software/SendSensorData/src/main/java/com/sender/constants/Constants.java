package com.sender.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("config.properties")
public class Constants {
	
	//path to CSV which contains sensors and consumers data
	@Value("${SENSOR_DATA_CSV}")
	public static String SENSOR_DATA_CSV="SensorData.csv";
	
	//the exchange for sending info to raspberry
	@Value("${EXCHANGE_NAME}")
	public static String EXCHANGE_NAME="colector_exchange";
	
	//exchange type
	@Value("${EXCHANGE_TYPE}")
	public static String EXCHANGE_TYPE="direct";
	
	//exchange host
	@Value("${HOST}")
	public static String HOST="localhost";
	
	//rabbitmq broker credentials
	@Value("${RABBITMQ_USERNAME}")
	public static String RABBITMQ_USERNAME="test";
	@Value("${RABBITMQ_PASSWORD}")
	public static String RABBITMQ_PASSWORD="test";
	@Value("${RABBITMQ_PORT}")
	public static int RABBITMQ_PORT=5672;
	
	//the keys with which the data from sensors is encrypted
	@Value("${OUTLET_KEY}")
	public static String OUTLET_KEY="outlet_key";
	@Value("${SWITCH_KEY}")
	public static String SWITCH_KEY="switch_key";
	@Value("${SENSOR_KEY}")
	public static String SENSOR_KEY="sensor_key";
}
