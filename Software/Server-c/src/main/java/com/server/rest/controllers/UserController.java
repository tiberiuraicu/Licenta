package com.server.rest.controllers;

import java.io.IOException;
import java.util.Map;
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
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.REST.RestFunctions;
import com.server.socket.DataBroadcaster;


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
	
	@Autowired
	DataBroadcaster dataBroadcaster;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody Map<String, String> json) throws ServletException {
		return restFunctions.login(json);
	}
	
	@RequestMapping(value = "/getId", method = RequestMethod.POST)
	public int getId(@RequestBody String email) throws ServletException {
		return restFunctions.getID(email);
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
		
		return restFunctions.getAllOutletsAndLocations();
	}
	
	@RequestMapping(value = "/resources/pieChart", method = RequestMethod.POST)
	public void initializePieChart(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {

		dataBroadcaster.sendTotalPowerConsumed(json.get("userId"));
		
	}
	@RequestMapping(value = "/resources/lineChart", method = RequestMethod.POST)
	public void initializeLineChart(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		dataBroadcaster.sendOutletPower(json.get("userId"));
		
	}
	
	

}
