package com.server.processing.MqReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AddToConsumptionEventHandler;
import com.server.cep.handler.LightAndMovementEventHandler;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Outlet;
import com.server.entites.Sensor;
import com.server.entites.Switch;
import com.server.processing.CEP.CEPFunctions;
import com.server.processing.Database.DatabaseFunctions;

@Component
public class MqReceiverFunctions {

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
	DatabaseFunctions databaseFunctions;
	@Autowired
	CEPFunctions cepFunctions;

	ObjectMapper mapper = new ObjectMapper();

	public String consumerDataProcess(byte[] consumerMessageBody) {
		System.out.println("-------");
		String returnMessage = null;
		try {
			// transform the received message from byte to String
			String consumerMessage = new String(consumerMessageBody, "UTF-8");
			// if the message is from an outlet
			if (consumerMessage.contains("outlet")) {

				// convert the json message to an Outlet object
				Consumer outlet = mapper.readValue(consumerMessage, Outlet.class);

				// get its location from database(by name) and set it
				outlet.setLocation(consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getLocation());

//				// get its state from database(by name) and set it
//				outlet.setState(consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getState());

//				 //if the outlet is off
//				 if (outlet.getState() == 0)
//					//it's power consumed is zero
//					 outlet.setPowerConsumed(0.0);

				// create the link between the new arrived information of the outlet and its
				// circuit
				Circuit circuit = databaseFunctions.makeConsumerAndCircuitConnection(outlet,
						consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getCircuit());

				// calculate the circuit new power consumed
				circuit = databaseFunctions.calculateAndSetCircuitPowerConsumed(circuit);

				// save the new circuit configuration in database
				circuitRepository.save(circuit);

				// add the object to be handled by CEP
				addToConsumptionEventHandler.handle(outlet);

				returnMessage = "Consumer message received -> outlet.";
			}
			// if the message is from an switch
			if (consumerMessage.contains("switch")) {

				// convert the json message to an Switch object
				Consumer switcher = mapper.readValue(consumerMessage, Switch.class);

				// get its location from database(by name) and set it
				switcher.setLocation(consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getLocation());

//				// get its state from database(by name) and set it
//				switcher.setState(consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getState());

//				 //if the switcher is off
//				 if (switcher.getState() == 0)
//					 //it's power consumed is zero
//					 switcher.setPowerConsumed(0.0);
//				

				// create the link between the new arrived information of the switch and its
				// circuit
				Circuit circuit = databaseFunctions.makeConsumerAndCircuitConnection(switcher,
						consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getCircuit());

				// calculate the circuit new power consumed
				circuit = databaseFunctions.calculateAndSetCircuitPowerConsumed(circuit);

				// save the new circuit configuration in database
				circuitRepository.save(circuit);

				// add the object to be handled by CEP
				addToConsumptionEventHandler.handle(switcher);

				returnMessage = "Consumer message received -> switch.";
			}

			// if all it's good send the following response :
			return returnMessage;

		} catch (Exception e) {
			e.printStackTrace();

			// if an exception occured send the following response :
			return "Error during data processing";
		}

	}

	public String sensorDataProcess(byte[] sensorMessageBody) {

		String returnMessage = null;

		try {
			// transform the received message from byte to String
			String sensorMessage = new String(sensorMessageBody, "UTF-8");

			// if the sensor is of type movement
			if (sensorMessage.contains("movement")) {

				// convert the json message to an Sensor object
				Sensor sensor = mapper.readValue(sensorMessage, Sensor.class);

				// get its location from database(by name) and set it
				sensor.setLocation(sensorRepository.findTopByNameOrderByIdDesc(sensor.getName()).getLocation());
		
				// create the link between the new arrived information of the sensor and its
				// circuit
				Circuit circuit = databaseFunctions.makeSensorAndCircuitConnection(sensor,
						sensorRepository.findTopByNameOrderByIdDesc(sensor.getName()).getCircuit());

				// calculate the circuit new power consumed
				circuit = databaseFunctions.calculateAndSetCircuitPowerConsumed(circuit);

				// save the new circuit configuration in database
				circuitRepository.save(circuit);

				// add the object to be handled by CEP
				lightAndMovementEventHandler.handle(sensor);

				returnMessage = "Sensor message received -> movement.";
			}

			return returnMessage;

		} catch (Exception e) {

			e.printStackTrace();
			return "Error during data processing.";
		}

	}

}
