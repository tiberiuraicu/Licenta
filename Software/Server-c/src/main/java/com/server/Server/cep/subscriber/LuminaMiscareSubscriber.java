package com.server.Server.cep.subscriber;

import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class LuminaMiscareSubscriber {

	public String getStatement() {
		 String crtiticalEventExpression =" select avg(miscare.stare) as p, avg(intrerupator.stare) as o from Miscare.win:time_batch(1 sec) as miscare, Intrerupator.win:time_batch(1 sec) as intrerupator having"
		 		+ " avg(miscare.stare)=0 and avg(intrerupator.stare)=1";
	        return crtiticalEventExpression;
	}

    public void update(Map<String,Integer> eventMap) {

    	System.out.println(eventMap.get("p")+"ceva");
    	System.out.println(eventMap.get("o")+"altceva");
       
    }

}
