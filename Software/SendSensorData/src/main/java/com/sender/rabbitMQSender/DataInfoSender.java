package com.sender.rabbitMQSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sender.constants.Constants;
import com.sender.entites.Consumer;
import com.sender.entites.Outlet;
import com.sender.entites.Switch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

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

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.SENSOR_DATA_CSV))) {
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

		String[] tableHeadValues = { "outlet1", "outlet1", "outlet3", "outlet4", "switch1", "switch2" };

		String cvsSplitBy = ",";

		String[] lineValues = line.split(cvsSplitBy);

		System.out.println(lineValues.length);
		if (!line.contains("outlet1"))
			for (int i = 0; i < lineValues.length; i++) {
				if (i <= 3)
					sendOutletValues(Double.parseDouble(lineValues[i]), tableHeadValues[i]);
				else if (i > 3 && i <= 5)
					sendSwitchValues(Double.parseDouble(lineValues[i]), tableHeadValues[i]);
			}

	}

	private void sendOutletValues(Double powerConsumed, String outletName) throws JsonProcessingException {

		Consumer outlet = new Outlet();
		outlet.setName(outletName);
		outlet.setPowerConsumed(powerConsumed);
		outlet.setTimestamp(new Date());
		outlet.setState(1);

		String outletAsJSON = mapper.writeValueAsString(outlet);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(),
				Constants.OUTLET_KEY, outletAsJSON.toString().getBytes());

		System.out.println(callBackMessage);

	}

	private void sendSwitchValues(Double powerConsumed, String switchName) throws JsonProcessingException {

		Consumer switcher = new Switch();
		switcher.setName(switchName);
		switcher.setPowerConsumed(powerConsumed);
		switcher.setTimestamp(new Date());
		switcher.setState(1);
		
		String switchAsJSON = mapper.writeValueAsString(switcher);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(),
				Constants.SWITCH_KEY, switchAsJSON.toString().getBytes());

		System.out.println(callBackMessage);

	}
}
