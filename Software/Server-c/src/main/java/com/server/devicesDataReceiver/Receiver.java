package com.server.devicesDataReceiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AddToConsumptionEventHandler;
import com.server.cep.handler.LightAndMovementEventHandler;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.entites.Consumer;
import com.server.entites.Outlet;
import com.server.entites.Sensor;
import com.server.entites.Switch;

@Component
public class Receiver {

	@Autowired
	private AddToConsumptionEventHandler addToConsumptionEventHandler;

	@Autowired
	private LightAndMovementEventHandler lightAndMovementEventHandler;

	@Autowired
	private SensorRepository sensorRepository;

	@Autowired
	ConsumerRepository consumerRepository;

	ObjectMapper mapper = new ObjectMapper();

	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String consumerDataReceiver(byte[] body) throws Exception {
		try {

			String consumerMessage = new String(body, "UTF-8");

			if (consumerMessage.contains("outlet")) {

				Consumer outlet = mapper.readValue(consumerMessage, Outlet.class);
				//consumerRepository.save(outlet);
				addToConsumptionEventHandler.handle(outlet);

			}
			if (consumerMessage.contains("switch")) {

				Consumer switcherForConsumptionAdding = mapper.readValue(consumerMessage, Switch.class);

//				List<Consumer> switcherForLightOpenTesting =  consumerRepository
//						.getConsumerByName(switcherForConsumptionAdding.getName());
//
//				switcherForConsumptionAdding.setLocation(switcherForLightOpenTesting.get(switcherForLightOpenTesting.size()-1).getLocation());
				
				//TODO change this
				Consumer switcher = consumerRepository.getConsumerByName(switcherForConsumptionAdding.getName());
				switcherForConsumptionAdding.setLocation(switcher.getLocation());
						
				//consumerRepository.save(switcherForConsumptionAdding);
				
				addToConsumptionEventHandler.handle(switcherForConsumptionAdding);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Consumer message received.";
	}

	@RabbitListener(queues = "Sensor")
	public String sensorDataReceiver(byte[] body) throws Exception {
		try {

			String sensorMessage = new String(body, "UTF-8");

			if (sensorMessage.contains("movement")) {

				Sensor sensor = mapper.readValue(sensorMessage, Sensor.class);

//				List<Sensor> sensorFromDB = sensorRepository.getSensorByName(sensor.getName());
//
//				sensor.setLocation(sensorFromDB.get(sensorFromDB.size()-1).getLocation());
				//TODO change this
				Sensor sensorFromDB = sensorRepository.getSensorByName(sensor.getName());
				sensor.setLocation(sensorFromDB.getLocation());
				//sensorRepository.save(sensor);
				
				lightAndMovementEventHandler.handle(sensor);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sensor message received.";
	}

}