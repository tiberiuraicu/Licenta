package com.raspberry.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raspberry.constants.Constants;
import com.raspberry.entites.Consumer;
import com.raspberry.entites.Sensor;

import java.io.IOException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class DataInfoSender {

	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange senderExchange;

	// JSON to POJO and POJO to JSON
	ObjectMapper mapper = new ObjectMapper();

	public void sendConsumerData(String consumerAsJson) throws InterruptedException, IOException {

		//create consumer object from data received
		Consumer consumer = mapper.readValue(consumerAsJson, Consumer.class);
        
		//transform Object to JSON
		String consumerAsJSON = mapper.writeValueAsString(consumer);

		//send the JSON to server
		String callBackMessage = (String) this.template.convertSendAndReceive(senderExchange.getName(),
				Constants.CONSUMER_KEY, consumerAsJSON.toString().getBytes());

		System.out.println(callBackMessage + "de la server");
	}	
	public void sendSensorData(String sensorAsJson) throws InterruptedException, IOException {

		//create sensor object from data received
		Sensor sensor = mapper.readValue(sensorAsJson, Sensor.class);
        
		//transform Object to JSON
		String sensorAsJSON = mapper.writeValueAsString(sensor);

		//send the JSON to server
		String callBackMessage = (String) this.template.convertSendAndReceive(senderExchange.getName(),
				Constants.SENSOR_KEY, sensorAsJSON.toString().getBytes());

		System.out.println(callBackMessage + "de la server");
	}	
}
