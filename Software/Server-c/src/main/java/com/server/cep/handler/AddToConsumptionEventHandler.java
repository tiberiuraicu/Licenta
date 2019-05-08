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

	// CEP service
	private EPServiceProvider epService;

	// CEP query for detecting a increase in power consumed
	private EPStatement addToConsumptionStatement;

	// Auto initialized (by Spring) -> the class who contains the statement
	@Autowired
	private AddToConsumptionSubscriber addToConsumptionSubscriber;

	public void initService() {
		Thread thread = new Thread() {
			public void run() {
				Configuration config = new Configuration();

				// Specifying from which package to get the necessary objects for Query
				config.addEventTypeAutoName("com.server.entites");

				// Specifying which configuration to follow
				epService = EPServiceProviderManager.getDefaultProvider(config);

				createAddToConsumptionCheckExpression();

			}
		};

		thread.start();
	}

	private void createAddToConsumptionCheckExpression() {
		// create the the actual statement
		addToConsumptionStatement = epService.getEPAdministrator().createEPL(addToConsumptionSubscriber.getStatement());

		// adding a method to get the result in case the query gives one
		addToConsumptionStatement.setSubscriber(addToConsumptionSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	// Send the event to query
	public void handle(Consumer consumer) {

		epService.getEPRuntime().sendEvent(consumer);
	}
}
