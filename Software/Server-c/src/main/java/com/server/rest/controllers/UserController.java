package com.server.rest.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.database.repositories.UserRepository;
import com.server.entites.Device;
import com.server.entites.User;
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.REST.RestFunctions;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	
	@RequestMapping(value = "/resources/totalPowerConsumed", method = RequestMethod.GET)
	public String getTotalPowerConsumed()
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		System.out.println("intra");
		return restFunctions.getTotalPowerConsumed();
	}

}
