package com.server.cep.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.UnusedOutletSubscriber;
import com.server.entites.Consumer;

@Component
@Scope(value = "singleton")
public class UnusedOutletEventHandler implements InitializingBean {

	// CEP service
	private EPServiceProvider epService;
	// CEP query for detecting a increase in power consumed
	private EPStatement unusedOutletStatement;

	// Auto initialized (by Spring) -> the class who contains the statement
	@Autowired
	private UnusedOutletSubscriber unusedOutletSubscriber;

	public void initService() {

		Thread thread = new Thread() {
			public void run() {
				Configuration config = new Configuration();

				// Specifying from which package to get the necessary objects for Query
				config.addEventTypeAutoName("com.server.entites");

				// Specifying which configuration to follow
				epService = EPServiceProviderManager.getDefaultProvider(config);

				createUnusedOutletCheckExpression();
			}
		};

		thread.start();

	}

	private void createUnusedOutletCheckExpression() {

		// create the the actual statement
		unusedOutletStatement = epService.getEPAdministrator().createEPL(unusedOutletSubscriber.getStatement());

		// adding a method to get the result in case the query gives one
		unusedOutletStatement.setSubscriber(unusedOutletSubscriber);
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
