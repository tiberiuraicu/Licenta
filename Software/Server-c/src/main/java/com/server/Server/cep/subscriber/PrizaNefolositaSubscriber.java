package com.server.Server.cep.subscriber;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.server.Server.entites.Priza;


@Component
public class PrizaNefolositaSubscriber {

	public String getStatement() {
		 String crtiticalEventExpression =" select avg(priza.putereConsumata) as p, avg(priza.stare) as o from Priza.win:time_batch(1 sec) as priza having"
		 		+ " avg(priza.putereConsumata)<0.44 and avg(priza.stare)=1";
		
	        return crtiticalEventExpression;
	}
	 /**
     * Listener method called when Esper has detected a pattern match.
     */
    public void update(Map<String, Priza> eventMap) {

    	System.out.println("priza nefolosita");
    
    }

}
