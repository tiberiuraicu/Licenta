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

@Component
@Scope(value = "singleton")
public class UnusedOutletEventHandler implements InitializingBean {

	private EPServiceProvider epService;
	private EPStatement unusedOutletStatement;

	@Autowired
	private UnusedOutletSubscriber unusedOutletSubscriber;

	public void initService() {

		Configuration config = new Configuration();
		
		config.addEventTypeAutoName("com.server.entites");
		
		epService = EPServiceProviderManager.getDefaultProvider(config);
		
		createUnusedOutletCheckExpression();
	}

	private void createUnusedOutletCheckExpression() {

		unusedOutletStatement = epService.getEPAdministrator().createEPL(unusedOutletSubscriber.getStatement());
		
		unusedOutletStatement.setSubscriber(unusedOutletSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	
}
