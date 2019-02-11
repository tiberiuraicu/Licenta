package com.server.Server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.server.Server.cep.handler.AdaugaLaConsumEventHandler;
import com.server.Server.entites.Consumator;
import com.server.Server.entites.Priza;

@Component
public class Receiver {

	@SuppressWarnings("unused")
	@Autowired
	private RabbitTemplate template;
	@SuppressWarnings("unused")
	@Autowired
	private DirectExchange exchange;

	@Autowired
	private AdaugaLaConsumEventHandler adaugaLaConsumEventHandler;

	ObjectMapper mapper = new ObjectMapper();
	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumator")
	public String procesorDataReceiver(byte[] body) throws Exception {
		try {

			String procesorMessage = new String(body, "UTF-8");

			System.out.println(procesorMessage);

			Consumator priza = mapper.readValue(procesorMessage, Priza.class);
			priza.setStare(1);

			adaugaLaConsumEventHandler.handle(priza);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "merge";
	}

	// @RabbitListener(queues = "Alarm")
	// public String alarmDataReceiver(byte[] body) throws
	// UnsupportedEncodingException {
	// DataProcess dataProcess = new DataProcess();
	// String alarmMessage = new String(body, "UTF-8");
	// dataProcess.alarmDataProcess(alarmMessage);
	// return "merge";
	// }

}