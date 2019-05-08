package com.server.processing.MqReceiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.cep.handler.AddToConsumptionEventHandler;
import com.server.cep.handler.LightAndMovementEventHandler;
import com.server.cep.handler.UnusedOutletEventHandler;
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
	UnusedOutletEventHandler unusedOutletEventHandler;

	@Autowired
	DatabaseFunctions databaseFunctions;
	@Autowired
	CEPFunctions cepFunctions;

	ObjectMapper mapper = new ObjectMapper();

	public String consumerDataProcess(byte[] consumerMessageBody) {
		String returnMessage = null;
		try {
			Thread thread = new Thread() {
				public void run() {
					// transform the received message from byte to String
					String consumerMessage = null;
					try {
						consumerMessage = new String(consumerMessageBody, "UTF-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// if the message is from an outlet
					if (consumerMessage.contains("outlet")) {

						// convert the json message to an Outlet object
						Consumer outlet = null;
						try {
							outlet = mapper.readValue(consumerMessage, Outlet.class);
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// get its location from database(by name) and set it
						outlet.setLocation(
								consumerRepository.findTopByNameOrderByIdDesc(outlet.getName()).getLocation());

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
						
						unusedOutletEventHandler.handle(outlet);

						
					}
					// if the message is from an switch
					if (consumerMessage.contains("switch")) {

						// convert the json message to an Switch object
						Consumer switcher = null;
						try {
							switcher = mapper.readValue(consumerMessage, Switch.class);
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// get its location from database(by name) and set it
						switcher.setLocation(
								consumerRepository.findTopByNameOrderByIdDesc(switcher.getName()).getLocation());

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

					}
				}
			};

			returnMessage = "Consumer message received -> switch.";
			thread.start();

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
			Thread thread = new Thread() {
				public void run() {
					// transform the received message from byte to String
					String sensorMessage = null;
					try {
						sensorMessage = new String(sensorMessageBody, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// if the sensor is of type movement
					if (sensorMessage.contains("movement")) {

						// convert the json message to an Sensor object
						Sensor sensor = null;
						try {
							sensor = mapper.readValue(sensorMessage, Sensor.class);
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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
					}
				}

			};

			thread.start();

			returnMessage = "Sensor message received -> movement.";

			return returnMessage;

		} catch (Exception e) {

			e.printStackTrace();
			return "Error during data processing.";
		}

	}

}
