package com.server.devicesDataReceiver;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.processing.receiver.ReceiverFunctions;

@Component
public class Receiver {

	@Autowired
	ReceiverFunctions receiverFunctions;

	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String consumerDataReceiver(byte[] body) throws Exception {
		return receiverFunctions.consumerDataProcess(body);
	}

	@RabbitListener(queues = "Sensor")
	public String sensorDataReceiver(byte[] body) throws Exception {
		return receiverFunctions.sensorDataProcess(body);
	}

}