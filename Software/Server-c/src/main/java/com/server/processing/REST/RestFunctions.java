package com.server.processing.REST;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.UserRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.User;
import com.server.processing.Database.DatabaseFunctions;
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
		System.out.println(json.get("name"));
		for (Consumer consumer : consumerRepository.findTop60ByNameOrderByIdDesc(json.get("name"))) {

			outletPowerConsumptionInfo.addProperty(consumer.getTimestamp().toString(), consumer.getPowerConsumed());
		}

		return outletPowerConsumptionInfo.toString();
	}

	public String getLastConsumersPowerConsumed(Map<String, String> json) throws ServletException, IOException {

		JsonObject last60recordsOfPowerConsumptionforOutlet = new JsonObject();

		for (Consumer consumer : consumerRepository.findTop60ByNameOrderByIdDesc(json.get("name"))) {

			last60recordsOfPowerConsumptionforOutlet.addProperty(consumer.getTimestamp().toString(), consumer.getPowerConsumed());
		}

		return last60recordsOfPowerConsumptionforOutlet.toString();
	}

	public List<String> getAllOutlets() {
		List<String> outlets = new Vector<String>();
		for (Consumer consumer : consumerRepository.findAll()) {
			if (consumer.getType().equals("outlet")) {
				if(!outlets.contains(consumer.getName()))
					outlets.add(consumer.getName());
			}
		}
		return outlets;
	}

	public String getLastRegistratedPowerConsumedForEveryOutlet() {
		JsonObject outletPowerConsumptionInfo = new JsonObject();
		List<String> outletNames = getAllOutlets();
		for(String outletName : outletNames ) {
			JsonObject outletChartData= new JsonObject();
			outletChartData.addProperty(
					"powerConsumed",
					consumerRepository.findTopByNameOrderByIdDesc(outletName).getPowerConsumed());
			outletChartData.addProperty(
					"timestamp",
					consumerRepository.findTopByNameOrderByIdDesc(outletName).getTimestamp().toString());
			outletPowerConsumptionInfo.add(outletName, outletChartData);
		}
		return outletPowerConsumptionInfo.toString();
		
	}
}
