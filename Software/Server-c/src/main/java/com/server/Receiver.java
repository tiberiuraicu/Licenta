package com.server;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AdaugaLaConsumEventHandler;
import com.server.entites.Consumer;
import com.server.entites.Outlet;
import com.server.entites.Switch;

@Component
public class Receiver {

	@Autowired
	private AdaugaLaConsumEventHandler adaugaLaConsumEventHandler;

	ObjectMapper mapper = new ObjectMapper();

	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String consumerDataReceiver(byte[] body) throws Exception {
		try {

			String consumerMessage = new String(body, "UTF-8");

			if (consumerMessage.contains("outlet")) {

				Consumer outlet = mapper.readValue(consumerMessage, Outlet.class);

				adaugaLaConsumEventHandler.handle(outlet);

			}
			if (consumerMessage.contains("switch")) {

				Consumer switcher = mapper.readValue(consumerMessage, Switch.class);

				adaugaLaConsumEventHandler.handle(switcher);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Consumer message received.";
	}

}