package com.server.cep.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.LightAndMovementSubscriber;
import com.server.entites.Sensor;

@Component
@Scope(value = "singleton")
public class LightAndMovementEventHandler implements InitializingBean {


	private EPServiceProvider epService;
	private EPStatement lightAndMovementStatement;


	@Autowired

	private LightAndMovementSubscriber lightAndMovementSubscriber;

	public void initService() {
		
		Configuration config = new Configuration();
		
		config.addEventTypeAutoName("com.server.entites");
		
		epService = EPServiceProviderManager.getDefaultProvider(config);
		
		createLightAndMovementCheckExpression();
	}

	private void createLightAndMovementCheckExpression() {

        
		
		lightAndMovementStatement = epService.getEPAdministrator().createEPL(lightAndMovementSubscriber.getStatement());
		
		lightAndMovementStatement.setSubscriber(lightAndMovementSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	public void handle(Sensor movementSensor) {

		// LOG.debug(m.toString());
		
		epService.getEPRuntime().sendEvent(movementSensor);
		
	}


}
