package com.raspberry.serverDataSenderAndReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstructionsReceiver {
@Autowired
InstructionsSender instructionsSender;

	@RabbitListener(queues = "queue_instruction")
	public String consumerDataReceiver(byte[] instructionBody) throws Exception {
	
		instructionsSender.instructionSender(instructionBody);
		
		return "Instruction message received.";
	}
}
