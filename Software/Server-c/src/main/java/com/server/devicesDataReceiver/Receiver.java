package com.server.devicesDataReceiver;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.database.repositories.PowerSourceRepository;
import com.server.processing.MqReceiver.MqReceiverFunctions;

@Component
public class Receiver {

	@Autowired
	MqReceiverFunctions receiverFunctions;
	@Autowired
	PowerSourceRepository powerSourceRepository;
	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String consumerDataReceiver(byte[] body) throws Exception {
		System.out.println("------------------------------------------------------");
		System.out.println(powerSourceRepository.getPowerSourceById(1).getCircuits());
		return receiverFunctions.consumerDataProcess(body);
	}

	@RabbitListener(queues = "Sensor")
	public String sensorDataReceiver(byte[] body) throws Exception {
		return receiverFunctions.sensorDataProcess(body);
	}

}