package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.devicesInstructionsSender.InstructionsSender;

@Component
public class UnusedOutletSubscriber {

	@Autowired
	InstructionsSender instructionsSender;

	public String getStatement() {
		String crtiticalEventExpression = " select outlet.name " + "from Outlet.win:time_batch(3 sec) as outlet having"
				+ " avg(outlet.powerConsumed)<0.44 and outlet.state=1";

		return crtiticalEventExpression;
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 * 
	 * @throws JsonProcessingException
	 */
	public void update(Map<String, String> eventMap) throws JsonProcessingException {


		String outletName = (String) eventMap.get("outlet.name");
		System.out.println(outletName);
		instructionsSender.turnOnOffTheDevice(outletName,"0");
	}

}
