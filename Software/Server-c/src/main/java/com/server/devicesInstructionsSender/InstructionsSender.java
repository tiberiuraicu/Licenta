package com.server.devicesInstructionsSender;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.entites.Instruction;

@Component
public class InstructionsSender {
	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange exchange;

	ObjectMapper mapper = new ObjectMapper();

	public void instructionSender(Instruction instruction) throws JsonProcessingException {

		// transform Object to JSON
		String instructionAsJSON = mapper.writeValueAsString(instruction);

		// send the JSON to devices
		//TODO
		String callBackMessage = (String) this.template.convertSendAndReceive(exchange.getName(),
				"instruction_key", instructionAsJSON.getBytes());

		//System.out.println(callBackMessage);
	}
}
