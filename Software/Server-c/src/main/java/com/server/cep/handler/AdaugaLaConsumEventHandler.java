package com.server.cep.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.AdaugaLaCosnumSubscriber;
import com.server.entites.Consumer;

@Component
@Scope(value = "singleton")
public class AdaugaLaConsumEventHandler implements InitializingBean {

	private EPServiceProvider epService;
	private EPStatement luminaMiscareStatement;


	@Autowired
	private AdaugaLaCosnumSubscriber adaugaLaCosnumSubscriber;

	public void initService() {

		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.server.entites");
		epService = EPServiceProviderManager.getDefaultProvider(config);
		createPrizaNefolositaCheckExpression();
	}

	private void createPrizaNefolositaCheckExpression() {

		luminaMiscareStatement = epService.getEPAdministrator().createEPL(adaugaLaCosnumSubscriber.getStatement());
		luminaMiscareStatement.setSubscriber(adaugaLaCosnumSubscriber);
		
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	public void handle(Consumer consumator) {

		epService.getEPRuntime().sendEvent(consumator);
		

	}

}
