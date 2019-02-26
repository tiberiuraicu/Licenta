package com.server.cep.subscriber;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.cep.processing.FunctiiAjutor;
import com.server.cep.processing.HelperFunctions;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.PowerSource;

@Component
public class AddToConsumptionSubscriber {

	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	CircuitRepository circuitReporitory;
	@Autowired
	FunctiiAjutor functiiAjutor;
	@Autowired
	HelperFunctions helperFunctions;

	public String getStatement() {

		String crtiticalEventExpression = "select consumerWithSpikedPowerConsumption from pattern [ every consumerWithNormalPowerConsumption = Consumer() -> "
				+ "consumerWithSpikedPowerConsumption = Consumer(name = consumerWithNormalPowerConsumption.name,"
				+ "powerConsumed > consumerWithNormalPowerConsumption.powerConsumed)]";

		return crtiticalEventExpression;
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 */
	public void update(Map<String, Consumer> eventMap) {
		System.out.println("subscriber");
		Consumer consumerWithSpikedPowerConsumption = eventMap.get("consumerWithSpikedPowerConsumption");
		
		Consumer consumerFromDB = consumerRepository.findTopByNameOrderByIdDesc(consumerWithSpikedPowerConsumption.getName());

		Circuit circuitFromDB = consumerFromDB.getCircuit();

		PowerSource powerSource = circuitFromDB.getPowerSource();
		

			
		functiiAjutor.verificareMarireConsum(powerSource);

		
		

	}

}
