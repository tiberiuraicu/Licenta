package com.raspberry.devicesDataSenderAndReceiver;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DevicesDataReceiver {

	@Autowired
	DevicesDataSender dataInfoSender;

	// for string -> object
	ObjectMapper mapper = new ObjectMapper();

	// logging
	final static Logger logger = Logger.getLogger(DevicesDataReceiver.class);

	//listening for the outlet data
	@RabbitListener(queues = "queue_outlet")
	public String outletDataReceiver(byte[] body) throws Exception {
		try {
			// change byte -> String
			String outletData = new String(body, "UTF-8");

			// processes and sends data to server
			dataInfoSender.sendConsumerData(outletData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Outlet message has been received.";
	}
	
	//listening for the outlet data
	@RabbitListener(queues = "queue_switch")
	public String switchDataReceiver(byte[] body) throws Exception {
		try {
			// change byte -> String
			String switchData = new String(body, "UTF-8");

			// processes and sends data to server
			dataInfoSender.sendConsumerData(switchData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Switch message has been received.";
	}

	
	//listening for sensor data
	@RabbitListener(queues = "queue_sensor")
	public String sensorDataReceiver(byte[] body) throws Exception {
		try {
			// change byte -> String
			String sensorData = new String(body, "UTF-8");

			// processes and sends data to server
			dataInfoSender.sendSensorData(sensorData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sensor message has been received.";
	}

}