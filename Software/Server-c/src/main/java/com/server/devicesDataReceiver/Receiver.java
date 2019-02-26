package com.server.devicesDataReceiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AddToConsumptionEventHandler;
import com.server.cep.handler.LightAndMovementEventHandler;
import com.server.cep.processing.HelperFunctions;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.entites.Circuit;
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
	
	@Autowired
	CircuitRepository circuitRepository;
	
	@Autowired
	HelperFunctions helperFunctions;

	ObjectMapper mapper = new ObjectMapper();

	final static Logger logger = Logger.getLogger(Receiver.class);

	@RabbitListener(queues = "Consumer")
	public String consumerDataReceiver(byte[] body) throws Exception {
		try {

			String consumerMessage = new String(body, "UTF-8");

			if (consumerMessage.contains("outlet")) {

				Consumer outlet = mapper.readValue(consumerMessage, Outlet.class);
				outlet.setLocation(consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getLocation());
				
				Circuit circuit=helperFunctions.makeConsumerAndCircuitConnection(outlet, consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getCircuit());
				circuit = helperFunctions.calculateAndSetCircuitPowerConsumed(circuit);
				circuitRepository.save(circuit);
				
				addToConsumptionEventHandler.handle(outlet);

			}
			if (consumerMessage.contains("switch")) {

				Consumer switcher = mapper.readValue(consumerMessage, Switch.class);
			
				switcher.setLocation(consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getLocation());
				
				Circuit circuit=helperFunctions.makeConsumerAndCircuitConnection(switcher, consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getCircuit());
				circuit = helperFunctions.calculateAndSetCircuitPowerConsumed(circuit);
				circuitRepository.save(circuit);

				addToConsumptionEventHandler.handle(switcher);

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

				sensor.setLocation(sensorRepository.findTopByNameOrderByIdDesc(sensor.getName()).getLocation());
				
				sensor.setPowerConsumed(sensorRepository.findTopByNameOrderByIdDesc(sensor.getName()).getPowerConsumed());
				
				Circuit circuit=helperFunctions.makeSensorAndCircuitConnection(sensor, sensorRepository.findTopByNameOrderByIdDesc(sensor.getName()).getCircuit());
				circuit = helperFunctions.calculateAndSetCircuitPowerConsumed(circuit);
				circuitRepository.save(circuit);
		

				lightAndMovementEventHandler.handle(sensor);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Sensor message received.";
	}

}