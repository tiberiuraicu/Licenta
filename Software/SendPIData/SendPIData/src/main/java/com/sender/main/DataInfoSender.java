package com.sender.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sender.constants.CEPConstants;
import com.sender.entites.Consumator;
import com.sender.entites.Priza;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class DataInfoSender {

	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange exchange;

	// JSON to POJO and POJO to JSON
	ObjectMapper mapper = new ObjectMapper();

	public void sendData() throws JsonProcessingException, InterruptedException {

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CEPConstants.SENSOR_DATA_CSV))) {
			String line = "";
			// iterate trough every line from CSV
			while ((line = bufferedReader.readLine()) != null) {
				// send data from sensors at a specified timestamp
				sendOneLine(line);
				// send every 0.5s
				Thread.sleep(500);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void sendOneLine(String line) throws JsonProcessingException {

		String[] tableHeadValues = {"Timestamp","priza1","priza2"};

		String cvsSplitBy = ",";

		String[] lineValues = line.split(cvsSplitBy);

		if (!line.contains("Timestamp")) {
		System.out.println(lineValues.length);
			for (int i = 1; i < lineValues.length; i++) {
				sendOutletValues(Double.parseDouble(lineValues[i]), tableHeadValues[i]);
			}
		}
	}

	private void sendOutletValues(Double powerConsumed, String outletName) throws JsonProcessingException {

		Consumator outlet = new Priza();
		outlet.setNume(outletName);
		outlet.setPutereConsumata(powerConsumed);

		String outletAsJSON = mapper.writeValueAsString(outlet);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(), CEPConstants.CONSUMATOR_KEY,
				outletAsJSON.toString().getBytes());

		System.out.println(callBackMessage);
		
	}
}
