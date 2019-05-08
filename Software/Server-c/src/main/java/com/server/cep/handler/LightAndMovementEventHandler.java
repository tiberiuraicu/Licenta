package com.server.cep.handler;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.server.cep.subscriber.LightAndMovementSubscriber;
import com.server.database.repositories.ScenarioRepository;
import com.server.entites.Scenario;
import com.server.entites.Sensor;

@Component
@Scope(value = "singleton")
public class LightAndMovementEventHandler implements InitializingBean {

	// CEP service
	private EPServiceProvider epService;

	// CEP query for detecting a increase in power consumed
	private EPStatement lightAndMovementStatement;

	// Auto initialized (by Spring) -> the class who contains the statement
	@Autowired
	private LightAndMovementSubscriber lightAndMovementSubscriber;

	@Autowired
	private ScenarioRepository scenarioRepository;

	public void initService() {
		Thread thread = new Thread() {
			public void run() {
				Configuration config = new Configuration();

				// Specifying from which package to get the necessary objects for Query
				config.addEventTypeAutoName("com.server.entites");

				// Specifying which configuration to follow
				epService = EPServiceProviderManager.getDefaultProvider(config);

				List<Scenario> scenarios = scenarioRepository.findAll();

				for (Scenario scenario : scenarios) {
					createLightAndMovementCheckExpression(scenario.getSensorName(), scenario.getSwitchName(),
							scenario.getSensorRegisterTime(), scenario.getSwitchRegisterTime());
				}

			}
		};

		thread.start();

	}

	private void createLightAndMovementCheckExpression(String sensorName, String switchName, double sensorRegisterTime,
			double switchRegisterTime) {

		// create the the actual statement
		lightAndMovementStatement = epService.getEPAdministrator().createEPL(lightAndMovementSubscriber
				.getStatement(sensorName, switchName, sensorRegisterTime, switchRegisterTime));

		// adding a method to get the result in case the query gives one
		lightAndMovementStatement.setSubscriber(lightAndMovementSubscriber);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		initService();
	}

	// Send the event to query
	public void handle(Sensor movementSensor) {

		epService.getEPRuntime().sendEvent(movementSensor);
	}
}
