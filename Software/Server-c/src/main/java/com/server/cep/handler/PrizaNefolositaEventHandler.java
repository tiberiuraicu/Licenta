package com.server.cep.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.PrizaNefolositaSubscriber;
import com.server.entites.Outlet;

@Component
@Scope(value = "singleton")
public class PrizaNefolositaEventHandler implements InitializingBean {

	private EPServiceProvider epService;
	private EPStatement luminaMiscareStatement;


	@Autowired

	private PrizaNefolositaSubscriber prizaNefolositaSubscriber;

	public void initService() {

		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.server.entites");
		epService = EPServiceProviderManager.getDefaultProvider(config);
		createPrizaNefolositaCheckExpression();
	}

	private void createPrizaNefolositaCheckExpression() {

		luminaMiscareStatement = epService.getEPAdministrator().createEPL(prizaNefolositaSubscriber.getStatement());
		luminaMiscareStatement.setSubscriber(prizaNefolositaSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	public void handle(Outlet prizaEvent) {

		// LOG.debug(m.toString());
		epService.getEPRuntime().sendEvent(prizaEvent);

	}

}
