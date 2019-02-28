package com.sender.rabbitMQSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sender.constants.Constants;
import com.sender.entites.Consumer;
import com.sender.entites.Outlet;
import com.sender.entites.Sensor;
import com.sender.entites.Switch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class DataInfoSenderToRaspberry {

	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange exchange;

	
	final Properties prop = new Properties();

	// JSON to POJO and POJO to JSON
	ObjectMapper mapper = new ObjectMapper();

	String[] tableHeadValues;

	public void sendData() throws JsonProcessingException, InterruptedException {

		String cvsSplitBy = ",";

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.SENSOR_DATA_CSV))) {

			String line = "";
			// iterate trough every line from CSV
			while ((line = bufferedReader.readLine()) != null) {
				String[] lineValues = line.split(cvsSplitBy);
				if (lineValues[0].equals("name"))
					tableHeadValues = lineValues;
				if (lineValues[0].equals("values"))
					sendOneLine(lineValues);
				// send every 1s
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendOneLine(String[] lineValues) throws NumberFormatException, FileNotFoundException, IOException {

		for (int i = 1; i < lineValues.length; i++) {
			System.out.println(tableHeadValues[i]);
			if (tableHeadValues[i].contains("outlet"))
				sendOutletValues(Double.parseDouble(lineValues[i]), tableHeadValues[i]);
			else if (tableHeadValues[i].contains("switch"))
				sendSwitchValues(Double.parseDouble(lineValues[i]), tableHeadValues[i]);
			else if (tableHeadValues[i].contains("sensor"))
				sendSensorValues(lineValues[i], tableHeadValues[i]);
		}

	}

	private void sendOutletValues(Double powerConsumed, String outletName) throws FileNotFoundException, IOException {
		
		prop.load(new FileInputStream("devicesState.config"));
		
		Consumer outlet = new Outlet();
		outlet.setName(outletName);
		outlet.setPowerConsumed(powerConsumed);
		outlet.setTimestamp(new Date());
		outlet.setState(Integer.parseInt(prop.getProperty(outletName)));

		String outletAsJSON = mapper.writeValueAsString(outlet);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(),
				Constants.OUTLET_KEY, outletAsJSON.toString().getBytes());

		System.out.println(callBackMessage);

	}

	private void sendSwitchValues(Double powerConsumed, String switchName) throws FileNotFoundException, IOException {
		
		prop.load(new FileInputStream("devicesState.config"));

		Consumer switcher = new Switch();
		switcher.setName(switchName);
		switcher.setPowerConsumed(powerConsumed);
		switcher.setTimestamp(new Date());
		switcher.setState(Integer.parseInt(prop.getProperty(switchName)));

		String switchAsJSON = mapper.writeValueAsString(switcher);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(),
				Constants.SWITCH_KEY, switchAsJSON.toString().getBytes());

		System.out.println(callBackMessage);

	}

	private void sendSensorValues(String motionDetected, String sensorName) throws FileNotFoundException, IOException {
		
		prop.load(new FileInputStream("devicesState.config"));
		
		Sensor sensor = new Sensor();
		sensor.setName(sensorName);
		sensor.setTimestamp(new Date());
		sensor.setTriggered(Integer.parseInt(motionDetected));
		sensor.setState(Integer.parseInt(prop.getProperty(sensorName)));
		sensor.setType("movement");

		String sensorAsJSON = mapper.writeValueAsString(sensor);

		String callBackMessage = (String) this.template.convertSendAndReceive(this.exchange.getName(),
				Constants.SENSOR_KEY, sensorAsJSON.toString().getBytes());

		System.out.println(callBackMessage);

	}
}
