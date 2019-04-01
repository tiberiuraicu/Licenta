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
public class AuthentificationFunctions {

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
	public int getID(String email) {
		return userRepository.getUserByEmail(email).getId();

	}

}
