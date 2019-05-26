package com.server.processing.REST;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.database.repositories.UserRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;

@Component
public class HomePageFunctions {
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	CircuitRepository circuitRepository;
	
	// get the total power consumed by the circuits connected to every power source
	public String getTotalPowerConsumed() throws ServletException, IOException {

		// declare the variables which will
		// contain the total power consumed
		Double powerConsumedFromSolarPanel = 0.0;
		Double powerConsumedFromNormalPowerSource = 0.0;

		JsonObject powerConsumptionInfo = new JsonObject();

		for (Circuit circuit : circuitRepository.findAll()) {
			if (circuit.getPowerSource().getType().equals("solarPanel")) {
				powerConsumedFromSolarPanel += circuit.getPowerConsumed();
			} else if (circuit.getPowerSource().getType().equals("normalPowerSource")) {
				powerConsumedFromNormalPowerSource += circuit.getPowerConsumed();
			}
		}
		powerConsumptionInfo.addProperty("powerConsumedFromSolarPanel", powerConsumedFromSolarPanel);
		powerConsumptionInfo.addProperty("powerConsumedFromNormalPowerSource", powerConsumedFromNormalPowerSource);

		return powerConsumptionInfo.toString();
	}

	public String getLast60ConsumersPowerConsumed(Map<String, String> json) throws ServletException, IOException {

		JsonObject outletPowerConsumptionInfo = new JsonObject();
		for (Consumer consumer : consumerRepository.findTop60ByNameOrderByIdDesc(json.get("outletName"))) {

			outletPowerConsumptionInfo.addProperty(consumer.getTimestamp().toString(), consumer.getPowerConsumed());
		}

		return outletPowerConsumptionInfo.toString();
	}

	public String getAllOutletsAndLocations() {
		Map<String, List<String>> outletsLocation = new HashMap<String, List<String>>();

		for (Consumer consumer : consumerRepository.findAll()) {
			if (consumer.getType().equals("outlet")) {

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
		}
		JsonObject outletsAndLocationJson = new JsonObject();
		Iterator it = outletsLocation.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			List<String> listOfOutletsNames = (List<String>) pair.getValue();
			JsonObject oneOutletLocationJson = new JsonObject();
			for (int i = 0; i < listOfOutletsNames.size(); i++) {
				oneOutletLocationJson.addProperty(Integer.toString(i), listOfOutletsNames.get(i).replace("outlet", "Priză "));
			}
			outletsAndLocationJson.add(pair.getKey().toString(), oneOutletLocationJson);
		}

		return outletsAndLocationJson.toString();
	}

	
	public List<String> getAllOutlets() {
		List<String> outlets = new Vector<String>();
		for (Consumer consumer : consumerRepository.findAll()) {
			if (consumer.getType().equals("outlet")) {
				if (!outlets.contains(consumer.getName()))
					outlets.add(consumer.getName());
			}
		}
		return outlets;
	}

	public String getLastRegistratedPowerConsumedForEveryOutlet() {
		JsonObject outletPowerConsumptionInfo = new JsonObject();
		List<String> outletNames = getAllOutlets();
		for (String outletName : outletNames) {
			JsonObject outletChartData = new JsonObject();
			outletChartData.addProperty("powerConsumed",
					consumerRepository.findTopByNameOrderByIdDesc(outletName).getPowerConsumed());
			outletChartData.addProperty("timestamp",
					consumerRepository.findTopByNameOrderByIdDesc(outletName).getTimestamp().toString());
			outletPowerConsumptionInfo.add(outletName, outletChartData);

		}
		return outletPowerConsumptionInfo.toString();

	}


	public Double getAllConsumedPowerFromHomeForToday() {
	

		Double consumerPowerConsumedToday = 0.0;
		Double sensorPowerConsumedToday = 0.0;

			for (String consumerName : consumerRepository.findAllNotNull()) {
	
				consumerPowerConsumedToday = consumerRepository.findSumConsumerRecordAtSpecificDay(
							consumerName,
							LocalDateTime.now().toString().replace("T", " ").substring(0, 10));
			}

			for (String sensorName : sensorRepository.findAllNotNull()) {
		
					sensorPowerConsumedToday = sensorRepository.findAvgOfPowerConsumedSensorAtSpecificHour(
							sensorName,
							LocalDateTime.now().toString().replace("T", " ").substring(0, 10));
		}

		return consumerPowerConsumedToday + sensorPowerConsumedToday;
	}
	public String getAllConsumedPowerFromHomeForTodayAndThisMonth() {

		JsonObject todayAndThisMonthConsumption = new JsonObject();
		Double value = getAllConsumedPowerFromHomeForToday();
		todayAndThisMonthConsumption.addProperty("today", value);
		todayAndThisMonthConsumption.addProperty("thisMonth",
				value * Integer.parseInt(LocalDateTime.now().toString().substring(8, 10)));
		return todayAndThisMonthConsumption.toString();

	}

}
