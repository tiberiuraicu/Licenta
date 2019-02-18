package com.raspberry.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("config.properties")
public class Constants {
	//the exchange for sending info to server
	@Value("${SENDER_EXCHANGE_NAME}")
	public static String SENDER_EXCHANGE_NAME="direct_messages";
	
	//the exchange for receiving data from sensors
	@Value("${RECEIVER_EXCHANGE_NAME}")
	public static String RECEIVER_EXCHANGE_NAME="colector_exchange";
	
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
	
	//the key with which the data sent to server is encrypted
	@Value("${CONSUMER_KEY}")
	public static String CONSUMER_KEY="consumer_key";
	
	//the queues through which is received the data from sensors
	@Value("${QUEUE_OUTLET}")
	public static String QUEUE_OUTLET="queue_outlet";
	@Value("${QUEUE_SWITCH}")
	public static String QUEUE_SWITCH="queue_switch";
	@Value("${QUEUE_SENSOR}")
	public static String QUEUE_SENSOR="queue_sensor";
	
	//the keys with which the data received to from sensors is encrypted
	@Value("${OUTLET_KEY}")
	public static String OUTLET_KEY="outlet_key";
	@Value("${SWITCH_KEY}")
	public static String SWITCH_KEY="switch_key";
	@Value("${SENSOR_KEY}")
	public static String SENSOR_KEY="sensor_key";
}
