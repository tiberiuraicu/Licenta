package com.sender.rabbitMQReceiver;

import java.io.FileOutputStream;
import java.io.FileReader;
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

		FileReader reader = new FileReader("devicesState.config");

		
//		InputStream in1 = getClass().getResourceAsStream("/circuitState.config"); 
//		BufferedReader reader1 = new BufferedReader(new InputStreamReader(in1));
		
		String instructionMessage = new String(body, "UTF-8");

		Instruction instruction = mapper.readValue(instructionMessage, Instruction.class);
		
		// if instructruction type is for turning on and off rewrite devicesState file.
		if (instruction.getType().equals("OnOff")) {
			
			prop.load(reader);
			
			prop.setProperty(instruction.getDeviceName(), instruction.getOnOffValue());
			
			prop.store(new FileOutputStream("devicesState.config"), null);
			
		}
		
		// if instructruction type is for changing the circuit power source rewrite
		// circuitState file.
		if (instruction.getType().equals("PowerSourceChange")) {
			
//			prop.load(reader1);
//			prop.setProperty(instruction.getDeviceName(), instruction.getPowerSource());
//			prop.store(new FileOutputStream("/circuitState.config"), null);
			
		}

		return "Instruction message received.";
	}
}
