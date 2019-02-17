package com.server;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AdaugaLaConsumEventHandler;
import com.server.entites.Consumer;
import com.server.entites.Outlet;

@Component
public class Receiver {

	@Autowired
	private AdaugaLaConsumEventHandler adaugaLaConsumEventHandler;

	ObjectMapper mapper = new ObjectMapper();
	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String procesorDataReceiver(byte[] body) throws Exception {
		try {

			String procesorMessage = new String(body, "UTF-8");


			Consumer outlet = mapper.readValue(procesorMessage, Outlet.class);
			outlet.setState(1);

			adaugaLaConsumEventHandler.handle(outlet);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "merge";
	}

	

}