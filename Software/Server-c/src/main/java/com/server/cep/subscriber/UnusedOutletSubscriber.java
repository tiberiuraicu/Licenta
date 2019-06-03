package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.socket.NotificationBroadcaster;

@Component
public class UnusedOutletSubscriber {

	@Autowired
	InstructionsSender instructionsSender;
	
	@Autowired
	NotificationBroadcaster notificationBroadcaster;
	
	public String getStatement(String consumerName) {
		String crtiticalEventExpression = " select outlet.name " + "from Outlet(name='"+consumerName+"').win:time_batch(5 sec) as outlet having"
				+ " avg(outlet.powerConsumed)<0.44 and avg(outlet.state)=1";

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
		notificationBroadcaster.sendState("0", outletName);
		instructionsSender.turnOnOffTheDevice(outletName,"0");
		
	}

}
