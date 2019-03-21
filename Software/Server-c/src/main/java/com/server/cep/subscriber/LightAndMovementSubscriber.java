package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.devicesInstructionsSender.InstructionsSender;

@Component
public class LightAndMovementSubscriber {

	@Autowired
	InstructionsSender instructionsSender;

	public String getStatement(String sensorName, String switchName, double sensorRegisterTime,
			double switchRegisterTime) {

		String crtiticalEventExpression = " select distinct movement.name as sensorName  " + "from Sensor(name='"
				+ sensorName + "').win:time_batch(" + sensorRegisterTime + " sec) as movement having"
				+ " avg(movement.state)=1 and avg(movement.triggered)=0";

		return crtiticalEventExpression;
	}

	public void update(Map<String, String> eventMap) throws JsonProcessingException {

		String sensorName = eventMap.get("sensorName");
		
		instructionsSender.turnOffTheLight(sensorName);

	}
}
