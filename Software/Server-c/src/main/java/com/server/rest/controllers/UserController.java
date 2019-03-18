package com.server.rest.controllers;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.database.repositories.UserRepository;
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.REST.RestFunctions;


@RestController
@RequestMapping("/user")
public class UserController {

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	UserRepository userRepository;

	@Autowired
	DatabaseFunctions databaseFunctions;

	@Autowired
	RestFunctions restFunctions;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@RequestBody Map<String, String> json) throws ServletException {
		return restFunctions.login(json);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@RequestBody Map<String, String> userForm)
			throws JsonParseException, JsonMappingException, IOException {
		return restFunctions.registerUser(userForm);
	}
	
	@RequestMapping(value = "/resources/last60Consumers", method = RequestMethod.POST)
	public String getTotalPowerConsumed(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		
		return restFunctions.getLast60ConsumersPowerConsumed(json);
	}
	
	@RequestMapping(value = "/resources/getOutlets", method = RequestMethod.GET)
	public String getOutlets()
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		return restFunctions.getAllOutlets().toString();
	}
	
	

}
