package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.PowerSourceRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.PowerSource;
import com.server.processing.CEP.CEPFunctions;
import com.server.processing.Database.DatabaseFunctions;

@Component
public class AddToConsumptionSubscriber {

	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	PowerSourceRepository powerSourceRepository;
	@Autowired
	CircuitRepository circuitReporitory;
	@Autowired
	CEPFunctions CEPFunctions;
	@Autowired
	DatabaseFunctions helperFunctions;

	public String getStatement() {

		String crtiticalEventExpression = "select consumerWithSpikedPowerConsumption from pattern [ every consumerWithNormalPowerConsumption = Consumer() -> "
				+ "consumerWithSpikedPowerConsumption = Consumer(name = consumerWithNormalPowerConsumption.name,"
				+ "powerConsumed > consumerWithNormalPowerConsumption.powerConsumed)]";

		return crtiticalEventExpression;
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 * @throws InterruptedException 
	 */
	public void update(Map<String, Consumer> eventMap) throws InterruptedException {
		
		Consumer consumerWithSpikedPowerConsumption = eventMap.get("consumerWithSpikedPowerConsumption");
		
		Consumer consumerFromDB = consumerRepository.findTopByNameOrderByIdDesc(consumerWithSpikedPowerConsumption.getName());

		Circuit circuitFromDB = consumerFromDB.getCircuit();

		PowerSource powerSource = circuitFromDB.getPowerSource();
		
		PowerSource solarPowerSource = powerSourceRepository.getPowerSourceById(1);
			
		CEPFunctions.energyStatusCheckForSolarPanel(solarPowerSource);

		
		

	}

}
