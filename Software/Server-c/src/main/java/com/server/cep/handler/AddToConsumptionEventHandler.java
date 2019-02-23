package com.server.cep.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.AddToConsumptionSubscriber;
import com.server.entites.Consumer;

@Component
@Scope(value = "singleton")
public class AddToConsumptionEventHandler implements InitializingBean {

	private EPServiceProvider epService;
	private EPStatement addToConsumptionStatement;

	@Autowired
	private AddToConsumptionSubscriber addToConsumptionSubscriber;

	public void initService() {
		
		Configuration config = new Configuration();
		
		config.addEventTypeAutoName("com.server.entites");
		
		epService = EPServiceProviderManager.getDefaultProvider(config);
		
		createAddToConsumptionCheckExpression();
	}

	private void createAddToConsumptionCheckExpression() {
		
		addToConsumptionStatement = epService.getEPAdministrator().createEPL(addToConsumptionSubscriber.getStatement());
		
		addToConsumptionStatement.setSubscriber(addToConsumptionSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		initService();
	}

	public void handle(Consumer consumer) {
		
		epService.getEPRuntime().sendEvent(consumer);
	}
}
