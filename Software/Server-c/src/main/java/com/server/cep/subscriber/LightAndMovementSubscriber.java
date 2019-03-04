package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LightAndMovementSubscriber {

	public String getStatement() {
		 String crtiticalEventExpression =" select avg(movement.state), last(movement.name) as n "
		 		+ "from Sensor(name='sensor1').win:time_batch(10 sec) as movement, "
		 		+ "Switch(name='switch1').win:time_batch(10 sec) as switcher having"
		 		+ " avg(movement.state)=1 and avg(movement.triggered)=1 and avg(switcher.powerConsumed)>0 ";
		 		
	        return crtiticalEventExpression;
	}

    public void update(Map<String,Integer> eventMap) {

    
    	System.out.println(eventMap.get("n"));
    	
    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
    	
    }
}
