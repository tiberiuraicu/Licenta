package com.server.processing.REST;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.SensorRepository;
import com.server.database.repositories.UserRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.Sensor;
import com.server.entites.User;
import com.server.processing.Database.DatabaseFunctions;
import com.server.socket.DataBroadcaster;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class RestFunctions {

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	UserRepository userRepository;
	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	CircuitRepository circuitRepository;
	@Autowired
	HttpServletRequest httpServletRequest;
	@Autowired
	HttpServletResponse httpServletResponse;
	@Autowired
	DatabaseFunctions databaseFunctions;

	public String login(Map<String, String> json) throws ServletException {
		if (json.get("email") == null || json.get("password") == null) {
			throw new ServletException("Please fill in username and password");
		}

		String email = json.get("email");
		String password = json.get("password");
		System.out.println(email + " " + password);

		User user = userRepository.getUserByEmail(email);
		if (user != null)
			if (user.getPassword().equals(password))
				return Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + 36000000))
						.signWith(SignatureAlgorithm.HS256, "este1234").compact();

		return "nu exista";
	}

	public String registerUser(Map<String, String> userForm) {

		User user = new User();
		user.setEmail(userForm.get("email"));
		user.setPassword(userForm.get("password"));

		Device device = new Device();
		device.setId(Integer.parseInt(userForm.get("deviceId")));

		user = databaseFunctions.makeDeviceAndUserConnection(user, device);

		userRepository.save(user);

		return "user inregistrat";

	}

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
				oneOutletLocationJson.addProperty(Integer.toString(i), listOfOutletsNames.get(i));
			}
			outletsAndLocationJson.add(pair.getKey().toString(), oneOutletLocationJson);
		}

		return outletsAndLocationJson.toString();
	}

	public String getAllOutletsAndLocationsForMapPage(String circuitId) {
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

	public int getID(String email) {
		return userRepository.getUserByEmail(email).getId();

	}

	public String getAllCircuits() {

		JsonArray allCircuits = new JsonArray();

		for (Circuit circuit : circuitRepository.findAll()) {

			JsonObject outletsForCircuit = new JsonObject();

			outletsForCircuit.addProperty("circuitId", circuit.getId());

			JsonArray allOutlets = new JsonArray();

			List<Consumer> consumers = circuit.getConsumers();

			for (int i = 0; i < consumers.size(); i++) {
				allOutlets.add(consumers.get(i).getName());
			}

			outletsForCircuit.add("outlets", allOutlets);

			allCircuits.add(outletsForCircuit);

		}
		return allCircuits.toString();
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
		System.out.println(powerConsumed);
		return powerConsumed.toString();
	}

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

		return consumedPower/1800;
	}

	public Integer getLast60ValuesForSensor(String name) {
		
		sensorRepository.findTopByNameOrderByIdDesc(name).getTriggered();
	
		return sensorRepository.findTopByNameOrderByIdDesc(name).getTriggered();
	}

}
