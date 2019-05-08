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
import com.server.database.repositories.ConsumerRepository;
import com.server.entites.Consumer;

@Component
@Scope(value = "singleton")
public class UnusedOutletEventHandler implements InitializingBean {

	// CEP service
	private EPServiceProvider epService;
	// CEP query for detecting a increase in power consumed
	private EPStatement unusedOutletStatement;

	@Autowired
	ConsumerRepository consumerRepository;

	// Auto initialized (by Spring) -> the class who contains the statement
	@Autowired
	private UnusedOutletSubscriber unusedOutletSubscriber;

	public void initService() {

		Configuration config = new Configuration();

		// Specifying from which package to get the necessary objects for Query
		config.addEventTypeAutoName("com.server.entites");

		// Specifying which configuration to follow
		epService = EPServiceProviderManager.getDefaultProvider(config);

		for (String consumerName : consumerRepository.findAllNotNull()) {
			Thread thread = new Thread() {
				public void run() {
					createUnusedOutletCheckExpression(consumerName);
				}
			};
			thread.start();
		}

	}

	private void createUnusedOutletCheckExpression(String consumerName) {

		// create the the actual statement
		unusedOutletStatement = epService.getEPAdministrator()
				.createEPL(unusedOutletSubscriber.getStatement(consumerName));

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
