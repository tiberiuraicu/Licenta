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

		prop.load(new FileInputStream("devicesState.config"));

		String instructionMessage = new String(body, "UTF-8");

		Instruction instruction = mapper.readValue(instructionMessage, Instruction.class);

		prop.setProperty(instruction.getDeviceName(), instruction.getOnOffValue());

		prop.store(new FileWriter("devicesState.config"), null);

		return "Instruction message received.";
	}
}
