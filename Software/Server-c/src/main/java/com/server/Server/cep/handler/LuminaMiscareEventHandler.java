package com.server.Server.cep.handler;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import com.server.Server.cep.subscriber.LuminaMiscareSubscriber;
import com.server.Server.entites.Intrerupator;
import com.server.Server.entites.Miscare;
@Component
@Scope(value = "singleton")
public class LuminaMiscareEventHandler implements InitializingBean {


	private EPServiceProvider epService;
	private EPStatement luminaMiscareStatement;


	@Autowired

	private LuminaMiscareSubscriber luminaMiscareSubscriber;

	public void initService() {
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.server.Server.entites");
		epService = EPServiceProviderManager.getDefaultProvider(config);
		createLuminaMiscareCheckExpression();
	}

	private void createLuminaMiscareCheckExpression() {


		luminaMiscareStatement = epService.getEPAdministrator().createEPL(luminaMiscareSubscriber.getStatement());
		luminaMiscareStatement.setSubscriber(luminaMiscareSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	public void handle(Miscare m, Intrerupator i) {

		// LOG.debug(m.toString());
		epService.getEPRuntime().sendEvent(m);
		epService.getEPRuntime().sendEvent(i);

	}

}
