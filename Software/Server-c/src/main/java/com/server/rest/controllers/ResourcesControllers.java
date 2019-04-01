package com.server.rest.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.server.processing.REST.RestMapPageFunctions;
import com.server.processing.REST.HomePageFunctions;
import com.server.processing.REST.AuthentificationFunctions;
import com.server.socket.DataBroadcaster;

@RestController
@RequestMapping("/resources")
public class ResourcesControllers {
	
	@Autowired
	AuthentificationFunctions restFunctions;
	
	@Autowired
	RestMapPageFunctions restMapPageFunctions;
	
	@Autowired
	HomePageFunctions homePageFunctions;
	
	@Autowired
	DataBroadcaster dataBroadcaster;

	@RequestMapping(value = "/last60Consumers", method = RequestMethod.POST)
	public String getTotalPowerConsumed(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		return homePageFunctions.getLast60ConsumersPowerConsumed(json);
	}

	@RequestMapping(value = "/getOutlets", method = RequestMethod.GET)
	public String getOutlets() throws JsonParseException, JsonMappingException, IOException, ServletException {

		return homePageFunctions.getAllOutletsAndLocations();
	}

	@RequestMapping(value = "/pieChart", method = RequestMethod.POST)
	public void initializePieChart(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		dataBroadcaster.sendTotalPowerConsumed(json.get("userId"));
	}

	@RequestMapping(value = "/lineChart", method = RequestMethod.POST)
	public void initializeLineChart(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		dataBroadcaster.sendOutletPower(json.get("userId"));
	}

	
	
	@RequestMapping(value = "/getCircuits", method = RequestMethod.GET)
	public String getCircuits() throws JsonParseException, JsonMappingException, IOException, ServletException {
		return restMapPageFunctions.getCircuits();
	}

	@RequestMapping(value = "/getCircuitsForMapPage", method = RequestMethod.POST)
	public String getCircuitsForMapPage(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		return restMapPageFunctions.getAllOutletsAndLocationsForMapPage(json.get("circuitId"));
	}

	@RequestMapping(value = "/getTodayConsumptionForConsumer", method = RequestMethod.POST)
	public String getTodayConsumptionForConsumer(@RequestBody Map<String, String> json)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		return restMapPageFunctions.getTodayConsumptionForConsumer(json.get("consumerName"));
	}
	
	@RequestMapping(value = "/getStateForConsumers", method = RequestMethod.POST)
	public String getStateForConsumers(@RequestBody List<Map<String, String>> consumers)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		return restMapPageFunctions.getStateForConsumers(consumers);
	}

}
