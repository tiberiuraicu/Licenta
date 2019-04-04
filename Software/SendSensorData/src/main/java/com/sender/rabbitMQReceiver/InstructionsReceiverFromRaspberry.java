package com.sender.rabbitMQReceiver;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sender.entites.Instruction;

@Component
public class InstructionsReceiverFromRaspberry {

	ObjectMapper mapper = new ObjectMapper();

	Properties prop = new Properties();

	@RabbitListener(queues = "queue_instruction")
	public String consumerDataReceiver(byte[] body) throws Exception {

		String instructionMessage = new String(body, "UTF-8");

		Instruction instruction = mapper.readValue(instructionMessage, Instruction.class);
		
		System.out.println(instruction);
		// if instructruction type is for turning on and off rewrite devicesState file.
		if (instruction.getType().equals("OnOff")) {
		System.out.println(instruction);
			prop.load(new FileInputStream("devicesState.config"));
			prop.setProperty(instruction.getDeviceName(), instruction.getOnOffValue());
			prop.store(new FileWriter("devicesState.config"), null);
			
		}
		
		// if instructruction type is for changing the circuit power source rewrite
		// circuitState file.
		if (instruction.getType().equals("PowerSourceChange")) {
			
			prop.load(new FileInputStream("circuitState.config"));
			prop.setProperty(instruction.getDeviceName(), instruction.getPowerSource());
			prop.store(new FileWriter("circuitState.config"), null);
			
		}

		return "Instruction message received.";
	}
}
