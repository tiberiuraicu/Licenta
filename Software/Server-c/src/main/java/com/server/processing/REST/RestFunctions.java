package com.server.processing.REST;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.UserRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
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
						.setExpiration(new Date(System.currentTimeMillis() + 3600000))
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

	public String getTotalPowerConsumed() throws ServletException, IOException {

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

	@SuppressWarnings("unchecked")
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
			for (int i =0;i<listOfOutletsNames.size();i++) {
				oneOutletLocationJson.addProperty(Integer.toString(i), listOfOutletsNames.get(i));
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

	public int getID(String email) {
		return userRepository.getUserByEmail(email).getId();

	}

}
