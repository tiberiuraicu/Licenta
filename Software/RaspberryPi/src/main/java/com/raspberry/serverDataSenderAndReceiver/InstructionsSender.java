package com.raspberry.serverDataSenderAndReceiver;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raspberry.constants.Constants;

@Component
public class InstructionsSender {
	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange devicesExchange;

	public void instructionSender(byte[] instructionBody) {
		
		// send the JSON to devices
		String callBackMessage = (String) this.template.convertSendAndReceive(devicesExchange.getName(),
				Constants.INSTRUCTION_KEY, instructionBody);

		System.out.println(callBackMessage);
	}
}
