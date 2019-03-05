package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.entites.Instruction;

@Component
public class LightAndMovementSubscriber {

	@Autowired
	InstructionsSender instructionsSender;

	public String getStatement(String sensorName, String switchName, double sensorRegisterTime,
			double switchRegisterTime) {

		String crtiticalEventExpression = " select distinct movement.name as sensorName  " + "from Sensor(name='"
				+ sensorName + "').win:time_batch(" + sensorRegisterTime + " sec) as movement, " + "Switch(name='"
				+ switchName + "').win:time_batch(" + switchRegisterTime + " sec) as switcher having"
				+ " avg(movement.state)=1 and avg(movement.triggered)=0 and avg(switcher.powerConsumed)>0 ";

		return crtiticalEventExpression;
	}

	public void update(Map<String, String> eventMap) throws JsonProcessingException {

		System.out.println(eventMap);

//		Instruction switchStop = new Instruction();
//		switchStop.setType("OnOff");
//		switchStop.setDeviceName(eventMap.get("switcherName"));
//		switchStop.setOnOffValue("0");
//
//		instructionsSender.instructionSender(switchStop);
	}
}
