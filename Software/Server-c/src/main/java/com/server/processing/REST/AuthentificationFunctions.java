package com.server.processing.REST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

	Properties prop = new Properties();
	String baseServerStockUrl = "ServerStock";

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

	public String registerUser(Map<String, String> userForm)
			throws JsonParseException, JsonMappingException, IOException {

		User user = new User();
		user.setEmail(userForm.get("email"));
		user.setPassword(userForm.get("password"));
		user.setCountry(userForm.get("country"));
		user.setLocality(userForm.get("locality"));
		user.setFirstName(userForm.get("firstName"));
		user.setLastName(userForm.get("lastName"));
		user.setPhoneNumber(Integer.parseInt(userForm.get("phoneNumber")));

		Device device = new Device();
		device.setId(Integer.parseInt(userForm.get("deviceId")));

		user = databaseFunctions.makeDeviceAndUserConnection(user, device);

		userRepository.save(user);

		return "user inregistrat";

	}

	public int getID(String email) {
		return userRepository.getUserByEmail(email).getId();

	}

	public String updateUser(Map<String, String> userForm) {

		User user = userRepository.findById(Integer.parseInt(userForm.get("userId")));
		if (userForm.get("email") != "" && userForm.get("email") != null)
			user.setEmail(userForm.get("email"));
		if (userForm.get("password") != "" && userForm.get("password") != null)
			user.setPassword(userForm.get("password"));
		if (userForm.get("country") != "" && userForm.get("country") != null)
			user.setCountry(userForm.get("country"));
		if (userForm.get("locality") != "" && userForm.get("locality") != null)
			user.setLocality(userForm.get("locality"));
		if (userForm.get("firstName") != "" && userForm.get("firstName") != null)
			user.setFirstName(userForm.get("firstName"));
		if (userForm.get("lastName") != "" && userForm.get("lastName") != null)
			user.setLastName(userForm.get("lastName"));
		if (userForm.get("phoneNumber") != "" && userForm.get("phoneNumber") != null)
			user.setPhoneNumber(Integer.parseInt(userForm.get("phoneNumber")));

		userRepository.save(user);

		return null;
	}

	public ResponseEntity<Object> setProfilePicture(MultipartFile file, String userId) throws IOException {

		File profilePicture = new File(baseServerStockUrl + userId + "/" + file.getOriginalFilename());
		profilePicture.getParentFile().mkdirs();
		profilePicture.createNewFile();
		FileOutputStream fout = new FileOutputStream(profilePicture);
		fout.write(file.getBytes());
		fout.close();

		FileOutputStream fileOutputStream = new FileOutputStream(
				new File(baseServerStockUrl + userId + "/profile.properties"));
		prop.load(new FileInputStream(baseServerStockUrl + userId + "/profile.properties"));
		prop.setProperty("profile-picture", file.getOriginalFilename());
		prop.store(fileOutputStream, "Properties");

		return new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);

	}

	public ResponseEntity<byte[]> getProfilePicture(String userId) throws IOException {

		prop.load(new FileInputStream(baseServerStockUrl + userId + "/profile.properties"));
		File profilePicture = new File(baseServerStockUrl + userId + "/" + prop.getProperty("profile-picture"));

		FileSystemResource fileResource = new FileSystemResource(profilePicture);
		byte[] base64ProfilePicture = Base64.getEncoder().encode(IOUtils.toByteArray(fileResource.getInputStream()));

		HttpHeaders headers = new HttpHeaders();
		headers.add("profile-picture", fileResource.getFilename());

		return ResponseEntity.ok().headers(headers).body(base64ProfilePicture);

	}

}
