package com.server.cep.subscriber;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.server.cep.processing.FnctiiAjutor;
import com.server.entites.Consumer;


@Component
public class AdaugaLaCosnumSubscriber {
FnctiiAjutor fnctiiAjutor= new FnctiiAjutor();

public String getStatement() {

// Example using 'Match Recognise' syntax.
//String crtiticalEventExpression = "select * from Consumator "
//        + "match_recognize ( "
//        + "       measures A as consumator1, B as consumator2 "
//        + "       pattern (A B) " 
//        + "       define "
//        + "               A as A.putereConsumata >= " + 0.0 + ", "
//        + "               B as (A.putereConsumata < B.putereConsumata) "
//        + ")";
//
//return crtiticalEventExpression;
//}
	
//Example using 'Match Recognise' syntax.
String crtiticalEventExpression = "select * from pattern [ every a=Consumer()-> b=Consumer(name=a.name,powerConsumed>a.powerConsumed)   ]";

return crtiticalEventExpression;
}

	/**
	 * Listener method called when Esper has detected a pattern match.
	 */
	public void update(Map<String, Consumer> eventMap) {
		System.out.println(eventMap.toString());
	
//	
//		Consumator a = eventMap.get("a");
//        System.out.println(a.getPutereConsumata()+"--------------------------------------------------"+a.getNume());
//
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
