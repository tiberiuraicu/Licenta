package com.server.cep.subscriber;

import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class LuminaMiscareSubscriber {

	public String getStatement() {
		 String crtiticalEventExpression =" select avg(movement.state) as movementStateAvg, avg(switch.state) as switchStateAvg from MovementSensor.win:time_batch(1 sec) as movement, Switch.win:time_batch(1 sec) as switch having"
		 		+ " avg(movement.state)=0 and avg(switch.state)=1";
	        return crtiticalEventExpression;
	}

    public void update(Map<String,Integer> eventMap) {

    	System.out.println(eventMap.get("p")+"ceva");
    	System.out.println(eventMap.get("o")+"altceva");
       
    }

}
