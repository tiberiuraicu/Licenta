package com.server.processing.REST;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Sensor;

@Component
public class RestMapPageFunctions {

	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	CircuitRepository circuitRepository;
	@Autowired
	InstructionsSender instructionsSender;

	public String getStateForConsumers(@RequestBody List<Map<String, String>> consumers) {

		JsonArray consumersState = new JsonArray();

		for (Map<String, String> consumerData : consumers) {
			String consumerName = (String) consumerData.get("name");
			JsonObject consumer = new JsonObject();
			consumer.addProperty("name", consumerName);
			if (consumerName.contains("sensor")) {
				consumer.addProperty("value", getLast60ValuesForSensor(consumerName));
				int state = sensorRepository.findTopByNameOrderByIdDesc(consumerName).getState();
				if (state == 0) {
					consumer.addProperty("state", false);
				} else if (state == 1) {
					consumer.addProperty("state", true);
				}

			} else if (consumerName.contains("outlet") || consumerName.contains("switch")) {
				consumer.addProperty("value", getHalfHourValues(consumerName));
				int state = consumerRepository.findTopByNameOrderByIdDesc(consumerName).getState();
				if (state == 0) {
					consumer.addProperty("state", false);
				} else if (state == 1) {
					consumer.addProperty("state", true);
				}
			}
			consumersState.add(consumer);
		}
		return consumersState.toString();
	}

	public Double getHalfHourValues(String name) {

		Double consumedPower = 0.0;
		for (Consumer consumer : consumerRepository.findTop1800ByNameOrderByIdDesc(name)) {
			consumedPower += consumer.getPowerConsumed();
		}

		return consumedPower / 1800;
	}

	public Integer getLast60ValuesForSensor(String name) {

		sensorRepository.findTopByNameOrderByIdDesc(name).getTriggered();

		return sensorRepository.findTopByNameOrderByIdDesc(name).getTriggered();
	}

	public String getTodayConsumptionForConsumer(String name) {
		JsonObject powerConsumed = new JsonObject();
		Double powerCosnumedToday = 0.0;
		if (name.contains("sensor")) {
			int counter = 0;
			for (Sensor sensor : sensorRepository.findTop3600ByNameOrderByIdDesc(name)) {
				if (sensor.getTimestamp().toString().contains(LocalDate.now().toString())) {
					powerCosnumedToday += sensor.getPowerConsumed();
					counter++;
				}
			}
			powerCosnumedToday /= counter;
			powerConsumed.addProperty("lastHour", powerCosnumedToday);
			powerCosnumedToday *= 24;
			powerConsumed.addProperty("today", powerCosnumedToday);
			powerConsumed.addProperty("onOff", sensorRepository.findTopByNameOrderByIdDesc(name).getState());
		} else {
			int counter = 0;
			for (Consumer consumer : consumerRepository.findTop3600ByNameOrderByIdDesc(name)) {
				if (consumer.getTimestamp().toString().contains(LocalDate.now().toString())) {
					powerCosnumedToday += consumer.getPowerConsumed();
					counter++;
				}
			}
			powerCosnumedToday /= counter;
			powerConsumed.addProperty("lastHour", powerCosnumedToday);
			powerCosnumedToday *= 24;
			powerConsumed.addProperty("today", powerCosnumedToday);
			powerConsumed.addProperty("onOff", consumerRepository.findTopByNameOrderByIdDesc(name).getState());
		}
		return powerConsumed.toString();
	}

	public String getCircuits() {

		JsonArray allCircuits = new JsonArray();

		for (Circuit circuitFromDb : circuitRepository.findAll()) {

			JsonObject circuit = new JsonObject();

			circuit.addProperty("circuitId", circuitFromDb.getId());
			circuit.addProperty("powerSource", circuitFromDb.getPowerSource().getType());
			circuit.addProperty("powerSourceGeneratedPower", circuitFromDb.getPowerSource().getGeneratedPower());

//			JsonArray allOutlets = new JsonArray();
//
//			List<Consumer> consumers = circuit.getConsumers();
//
//			for (int i = 0; i < consumers.size(); i++) {
//				allOutlets.add(consumers.get(i).getName());
//			}
//
//			outletsForCircuit.add("outlets", allOutlets);

			allCircuits.add(circuit);

		}
		return allCircuits.toString();
	}

	public String getLocationAndConsumersForCircuit(String circuitId) {
		JsonObject circuit = new JsonObject();
		JsonArray locations = new JsonArray();
		Map<String, List<String>> outletsLocation = new HashMap<String, List<String>>();

		for (Consumer consumer : circuitRepository.getCircuitById(Integer.parseInt(circuitId)).getConsumers()) {

			if (outletsLocation.get(consumer.getLocation()) == null) {

				Vector<String> outlets = new Vector<String>();
				outlets.add(consumer.getName());
				outletsLocation.put(consumer.getLocation(), outlets);
			} else {

				List<String> outlets = outletsLocation.get(consumer.getLocation());
				if (!outlets.contains(consumer.getName()))
					outlets.add(consumer.getName());
			}

		}

		for (Sensor sensor : circuitRepository.getCircuitById(Integer.parseInt(circuitId)).getSensors()) {

			if (outletsLocation.get(sensor.getLocation()) == null) {

				Vector<String> sensors = new Vector<String>();
				sensors.add(sensor.getName());
				outletsLocation.put(sensor.getLocation(), sensors);
			} else {

				List<String> sensors = outletsLocation.get(sensor.getLocation());
				if (!sensors.contains(sensor.getName()))
					sensors.add(sensor.getName());
			}

		}

		Iterator it = outletsLocation.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			JsonObject location = new JsonObject();
			JsonArray consumers = new JsonArray();

			List<String> listOfOutletsNames = (List<String>) pair.getValue();

			for (int i = 0; i < listOfOutletsNames.size(); i++) {
				JsonObject consumer = new JsonObject();
				consumer.addProperty("name", listOfOutletsNames.get(i));
				consumers.add(consumer);
			}
			location.addProperty("name", pair.getKey().toString());
			location.add("children", consumers);
			locations.add(location);
		}
		circuit.addProperty("name", circuitId);
		circuit.add("children", locations);

		return circuit.toString();
	}

	public String changeSensorState(Map<String, String> sensor) {
		instructionsSender.turnOnOffTheDevice(sensor.get("name"), sensor.get("state"));
		return "Sensor state saved";
	}

	public String changeConsumerState(Map<String, String> consumer) {
		instructionsSender.turnOnOffTheDevice(consumer.get("name"), consumer.get("state"));
		return "Consumer state saved";
	}

	public Double getAllConsumedPowerFromHomeForToday() {

		Double consumerPowerConsumedToday = 0.0;
		Double sensorPowerConsumedToday = 0.0;

		for (int i = 1; i <= 24; i++) {
			String hourIndex = Integer.toString(i).length() == 2 ? Integer.toString(i) : "0" + i;
			
			Double consumerHourlyAverage = 0.0;
			int consumerHoulyAverageNumberOfRecords =1;
			
			for (Consumer consumer : consumerRepository.findAll()) {

				if (consumer.getTimestamp().toString().contains(
						LocalDateTime.now().toString().replace("T", " ").substring(0, 10) + " " + hourIndex)) {
				
					consumerHourlyAverage += consumer.getPowerConsumed();

					consumerHoulyAverageNumberOfRecords++;
				}
			}
			consumerHourlyAverage /= consumerHoulyAverageNumberOfRecords;
			consumerPowerConsumedToday += consumerHourlyAverage;
			
			Double sensorHourlyAverage = 0.0;
			int sensorHoulyAverageNumberOfRecords = 1;
			
			for (Sensor sensor : sensorRepository.findAll()) {

				if (sensor.getTimestamp().toString().contains(
						LocalDateTime.now().toString().replace("T", " ").substring(0, 10) + " " + hourIndex)) {

					sensorHourlyAverage += sensor.getPowerConsumed();
					
					sensorHoulyAverageNumberOfRecords++;
				}
			}
			sensorHourlyAverage /= sensorHoulyAverageNumberOfRecords;
			
			sensorPowerConsumedToday += sensorHourlyAverage;
		}

		return consumerPowerConsumedToday + sensorPowerConsumedToday;
	}

	public Double getAllConsumedPowerFromHomeForThisMonth() {

		return getAllConsumedPowerFromHomeForToday()*Integer.parseInt(LocalDateTime.now().toString().substring(8, 10));
		
	}

}
