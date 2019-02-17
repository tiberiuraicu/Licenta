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
import com.server.cep.subscriber.LuminaMiscareSubscriber;
import com.server.entites.Switch;
import com.server.entites.MovementSensor;
@Component
@Scope(value = "singleton")
public class LuminaMiscareEventHandler implements InitializingBean {


	private EPServiceProvider epService;
	private EPStatement luminaMiscareStatement;


	@Autowired

	private LuminaMiscareSubscriber luminaMiscareSubscriber;

	public void initService() {
		Configuration config = new Configuration();
		config.addEventTypeAutoName("com.server.entites");
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

	public void handle(MovementSensor m, Switch i) {

		// LOG.debug(m.toString());
		epService.getEPRuntime().sendEvent(m);
		epService.getEPRuntime().sendEvent(i);

	}

}
