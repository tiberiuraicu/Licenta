package com.server.cep.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.database.repositories.AlimentatorRepository;
import com.server.database.repositories.ConsumatorRepository;
import com.server.entites.PowerSource;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Switch;
import com.server.entites.SolarPanel;
import com.server.entites.Outlet;

public class FnctiiAjutor {

	@Autowired
	AlimentatorRepository alimentatorRepository;
	@Autowired
	ConsumatorRepository consumatorRepository;

	public Circuit getCircuit() {
		return null;
	}

	public PowerSource getAlimentator() {
		java.util.List<Consumer> consumatori1 = new Vector<Consumer>();
		java.util.List<Consumer> consumatori2 = new Vector<Consumer>();
		java.util.List<Consumer> consumatori3 = new Vector<Consumer>();
		List<Circuit> circuite = new Vector<Circuit>();

		Consumer unu = new Outlet();
		unu.setPowerConsumed(0.2);
		unu.setState(1);
		unu.setName("outletOne");
		Consumer doi = new Outlet();
		doi.setPowerConsumed(52.6);
		doi.setState(1);
		doi.setName("outletTwo");
		Consumer trei = new Outlet();
		trei.setPowerConsumed(58.6);
		trei.setState(1);
		trei.setName("p3");

		Consumer patru = new Switch();
		patru.setPowerConsumed(0.2);
		patru.setState(1);
		patru.setName("i1");
		Consumer cinci = new Switch();
		cinci.setPowerConsumed(55.6);
		cinci.setState(1);
		cinci.setName("i2");
		
		Consumer sase = new Switch();
		sase.setPowerConsumed(58.6);
		sase.setState(1);
		sase.setName("i3");
		

		consumatori1.add(unu);
		consumatori1.add(doi);
		consumatori2.add(trei);

		consumatori2.add(patru);
		consumatori3.add(cinci);
		consumatori3.add(sase);

		PowerSource alimentator = new SolarPanel();
		alimentator.setPutereGenerata(200.5);
		Circuit c1 = new Circuit();
		c1.setConsumers(consumatori1);
		c1.setPowerSource(alimentator);
		c1.setPowerConsumed(calculateCircuitPowerConsumption(c1));
		Circuit c2 = new Circuit();
		c2.setConsumers(consumatori2);
		c2.setPowerSource(alimentator);
		c2.setPowerConsumed(calculateCircuitPowerConsumption(c2));
		Circuit c3 = new Circuit();
		c3.setConsumers(consumatori3);
		c3.setPowerSource(alimentator);
		c3.setPowerConsumed(calculateCircuitPowerConsumption(c3));
		
		sase.setCircuit(c3);
		cinci.setCircuit(c3);
		patru.setCircuit(c2);
		trei.setCircuit(c2);
		doi.setCircuit(c1);
		unu.setCircuit(c1);
		

		c1.setPowerSource(alimentator);
		c2.setPowerSource(alimentator);
		c3.setPowerSource(alimentator);
		
		circuite.add(c1);
		circuite.add(c2);
		circuite.add(c3);

		
		alimentator.setCircuits(circuite);

		return alimentator;

	}
	public Double calculateCircuitPowerConsumption(Circuit circuit) {
		Double putereConsumata = 0.0;
		List<Consumer> consumatori=circuit.getConsumers();
		for(Consumer consumator :consumatori)
			putereConsumata+=consumator.getPowerConsumed();
		return putereConsumata;
	}

	// verifica daca puterea generata de panou solar este <=> decat noua putere
	// consumata de circuite
	public void verificareMarireConsum(SolarPanel panouSolar) {
		// preia puterea generata de panou
		double putereGenerata = panouSolar.getPutereGenerata();
		// calculeaza noua putere consumata de circuitele alimentate la panou
		double putereConsumata = calculeazaPutereConsumata(panouSolar.getCircuits());
System.out.println("putere consumata"+putereConsumata);
		if (putereGenerata > putereConsumata) {
			// informeaza frontend ca a fost marat consumul
		} else if (putereGenerata < putereConsumata) {
			// rearanjeaza circuitele in cel mai bun mod posibil
			// astfel incat puterea consumata de circuitele conectate
			// la panou sa fie mai mica decat puterea generata
			rearanjareCircuite(panouSolar);

		}
	}

	public double calculeazaPutereConsumata(List<Circuit> circuite) {
		double putereConsumata = 0;
		for (Circuit circuit : circuite) {
			putereConsumata += circuit.getPowerConsumed();
		}
		return putereConsumata;
	}

	public void rearanjareCircuite(SolarPanel panouSolar) {
		// lista cu combinatii de circuite si puterile consumate de fiecare combinatie
		HashMap<List<Circuit>, Double> puteriConsumate = new HashMap<List<Circuit>, Double>();

		// fiecare combinatie posibila de circuite
		List<List<Circuit>> diferiteCombinatiiDeCircuite = getAllSubsets(panouSolar.getCircuits());

		// calculeaza puterea consumata pentru toate combinatiile de circuite
		for (List<Circuit> listaCuCircuite : diferiteCombinatiiDeCircuite) {
			Double putereConsumata = 0.0;
			for (Circuit circuit : listaCuCircuite) {
				putereConsumata += circuit.getPowerConsumed();
			}
			puteriConsumate.put(listaCuCircuite, putereConsumata);
		}
		// calculeaza cea mai apropiata valoare a puterii consumate
		// a diferitelor combinatii fata de puterea generata de panou solar
		Double min = 10000.0;
		List<Circuit> nearest = null;
		for (List<Circuit> key : puteriConsumate.keySet()) {

			Double temp = panouSolar.getPutereGenerata() - puteriConsumate.get(key);

			if (temp > 0) {

				if (temp < min) {
					min = temp;
					nearest = key;
				}
			}
		}
		System.out.println(nearest);
		panouSolar.setCircuits(nearest);
		// save in database panou
//		 alimentatorRepository.save(panouSolar);
//		 for (Alimentator book :  alimentatorRepository.findAll()) {
//			System.out.println(book.toString());
//			
//			 
//			 }
	}
	
   //de modificat
	public List<List<Circuit>> getAllSubsets(List<Circuit> input) {
		int allMasks = 1 << input.size();
		List<List<Circuit>> output = new ArrayList<List<Circuit>>();
		for (int i = 0; i < allMasks; i++) {
			List<Circuit> sub = new ArrayList<Circuit>();
			for (int j = 0; j < input.size(); j++) {
				if ((i & (1 << j)) > 0) {
					sub.add(input.get(j));
				}
			}
			output.add(sub);
		}

		return output;
	}

}
