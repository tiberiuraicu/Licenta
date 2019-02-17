package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.stereotype.Component;

import com.server.entites.Outlet;


@Component
public class PrizaNefolositaSubscriber {

	public String getStatement() {
		 String crtiticalEventExpression =" select avg(outlet.powerConsumed) as p, avg(outlet.state) as o from Outlet.win:time_batch(1 sec) as outlet having"
		 		+ " avg(outlet.powerConsumed)<0.44 and avg(outlet.state)=1";
		
	        return crtiticalEventExpression;
	}
	 /**
     * Listener method called when Esper has detected a pattern match.
     */
    public void update(Map<String, Outlet> eventMap) {

    	System.out.println("priza nefolosita");
    
    }

}
