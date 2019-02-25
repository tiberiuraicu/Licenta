package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.server.cep.processing.FunctiiAjutor;
import com.server.cep.processing.HelperFunctions;
import com.server.entites.Consumer;

@Component
public class AddToConsumptionSubscriber {
	FunctiiAjutor fnctiiAjutor = new FunctiiAjutor();
	HelperFunctions helperFunctions = new HelperFunctions();

	public String getStatement() {

		String crtiticalEventExpression = "select firstConsumer from pattern [ every firstConsumer=Consumer()-> secondConsumer=Consumer(name=firstConsumer.name,powerConsumed>firstConsumer.powerConsumed)]";

		return crtiticalEventExpression;
	}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 */
	public void update(Map<String, Consumer> eventMap) {
		System.out.println(eventMap.toString());

		Consumer a = eventMap.get("firstConsumer");
		
		System.out.println(a.getPowerConsumed() + "--------------------------------------------------" + a.getName());

		
		
//		Consumator con=consumatorRepository.findByNume(a.getNume());
//	
//		
//		System.out.println(con);
//		con.setPutereConsumata(a.getPutereConsumata());
//		
//		consumatorRepository.save(con);
//	
//		Circuit circuit=con.getCircuit();
//		System.out.println("****");
//		
//		Alimentator alimentator=  circuit.getAlimentator();
//
//		if(alimentator.getClass().equals(PanouSolar.class)) {
//		
//			fnctiiAjutor.verificareMarireConsum((PanouSolar)alimentator);
//		}

	}

}
