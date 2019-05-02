package com.server.rest.controllers;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.UserRepository;
import com.server.entites.User;
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.REST.AuthentificationFunctions;

@RestController
@RequestMapping("/user")
public class UserController {

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	UserRepository userRepository;

	@Autowired
	DatabaseFunctions databaseFunctions;

	@Autowired
	AuthentificationFunctions authentificationFunctions;

	@Autowired
	ConsumerRepository consumerRepository;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody Map<String, String> json) throws ServletException {
		return authentificationFunctions.login(json);
	}

	@RequestMapping(value = "/getId", method = RequestMethod.POST)
	public int getId(@RequestBody String email) throws ServletException {
		return authentificationFunctions.getID(email);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@RequestBody Map<String, String> userForm)
			throws JsonParseException, JsonMappingException, IOException {
		return authentificationFunctions.registerUser(userForm);
	}

	@RequestMapping(value = "/getUserDetails", method = RequestMethod.POST)
	public String getUserDetails(@RequestBody Map<String, String> userForm)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println(Integer.parseInt(userForm.get("userId")));
		User user = userRepository.findById(Integer.parseInt(userForm.get("userId")));
		JsonObject userDetails = new JsonObject();
		userDetails.addProperty("firstName", user.getFirstName());
		userDetails.addProperty("lastName", user.getLastName());
		userDetails.addProperty("location", user.getCountry() + " " + user.getLocality());
		userDetails.addProperty("phoneNumber", user.getPhoneNumber());
		userDetails.addProperty("email", user.getEmail());
		return userDetails.toString();
	}
	
	@RequestMapping(value = "/updateUserDetails", method = RequestMethod.POST)
	public String updateUserDetails(@RequestBody Map<String, String> userForm)
			throws JsonParseException, JsonMappingException, IOException {
		return authentificationFunctions.updateUser(userForm);
	}
	
	@RequestMapping(value = "/updateUserProfilePicture", method = RequestMethod.POST)
	public ResponseEntity<Object> setProfilePicture(@RequestParam("file") MultipartFile file,@RequestHeader HttpHeaders headers) throws IOException {
	
		return authentificationFunctions.setProfilePicture(file, headers.get("userId").get(0));
	}
	@RequestMapping(value = "/getProfilePicture", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getProfilePicture(@RequestHeader HttpHeaders headers) throws IOException {
	
	    return authentificationFunctions.getProfilePicture(headers.get("userId").get(0));
	}
}
