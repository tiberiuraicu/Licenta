package com.server.Server.cep.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;


import com.server.Server.database.repositories.AlimentatorRepository;
import com.server.Server.database.repositories.ConsumatorRepository;
import com.server.Server.entites.Alimentator;
import com.server.Server.entites.Circuit;
import com.server.Server.entites.Consumator;
import com.server.Server.entites.Intrerupator;
import com.server.Server.entites.PanouSolar;
import com.server.Server.entites.Priza;

public class FnctiiAjutor {

	@Autowired
	AlimentatorRepository alimentatorRepository;
	@Autowired
	ConsumatorRepository consumatorRepository;

	public Circuit getCircuit() {
		return null;
	}

	public Alimentator getAlimentator() {
		java.util.List<Consumator> consumatori1 = new Vector<Consumator>();
		java.util.List<Consumator> consumatori2 = new Vector<Consumator>();
		java.util.List<Consumator> consumatori3 = new Vector<Consumator>();
		List<Circuit> circuite = new Vector<Circuit>();

		Consumator unu = new Priza();
		unu.setPutereConsumata(0.2);
		unu.setStare(1);
		unu.setNume("outletOne");
		Consumator doi = new Priza();
		doi.setPutereConsumata(52.6);
		doi.setStare(1);
		doi.setNume("outletTwo");
		Consumator trei = new Priza();
		trei.setPutereConsumata(58.6);
		trei.setStare(1);
		trei.setNume("p3");

		Consumator patru = new Intrerupator();
		patru.setPutereConsumata(0.2);
		patru.setStare(1);
		patru.setNume("i1");
		Consumator cinci = new Intrerupator();
		cinci.setPutereConsumata(55.6);
		cinci.setStare(1);
		cinci.setNume("i2");
		
		Consumator sase = new Intrerupator();
		sase.setPutereConsumata(58.6);
		sase.setStare(1);
		sase.setNume("i3");
		

		consumatori1.add(unu);
		consumatori1.add(doi);
		consumatori2.add(trei);

		consumatori2.add(patru);
		consumatori3.add(cinci);
		consumatori3.add(sase);

		Alimentator alimentator = new PanouSolar();
		alimentator.setPutereGenerata(200.5);
		Circuit c1 = new Circuit();
		c1.setConsumatori(consumatori1);
		c1.setAlimentator(alimentator);
		c1.setPutereConsumata(calculateCircuitPowerConsumption(c1));
		Circuit c2 = new Circuit();
		c2.setConsumatori(consumatori2);
		c2.setAlimentator(alimentator);
		c2.setPutereConsumata(calculateCircuitPowerConsumption(c2));
		Circuit c3 = new Circuit();
		c3.setConsumatori(consumatori3);
		c3.setAlimentator(alimentator);
		c3.setPutereConsumata(calculateCircuitPowerConsumption(c3));
		
		sase.setCircuit(c3);
		cinci.setCircuit(c3);
		patru.setCircuit(c2);
		trei.setCircuit(c2);
		doi.setCircuit(c1);
		unu.setCircuit(c1);
		

		c1.setAlimentator(alimentator);
		c2.setAlimentator(alimentator);
		c3.setAlimentator(alimentator);
		
		circuite.add(c1);
		circuite.add(c2);
		circuite.add(c3);

		
		alimentator.setCircuite(circuite);

		return alimentator;

	}
	public Double calculateCircuitPowerConsumption(Circuit circuit) {
		Double putereConsumata = 0.0;
		List<Consumator> consumatori=circuit.getConsumatori();
		for(Consumator consumator :consumatori)
			putereConsumata+=consumator.getPutereConsumata();
		return putereConsumata;
	}

	// verifica daca puterea generata de panou solar este <=> decat noua putere
	// consumata de circuite
	public void verificareMarireConsum(PanouSolar panouSolar) {
		// preia puterea generata de panou
		double putereGenerata = panouSolar.getPutereGenerata();
		// calculeaza noua putere consumata de circuitele alimentate la panou
		double putereConsumata = calculeazaPutereConsumata(panouSolar.getCircuite());
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
			putereConsumata += circuit.getPutereConsumata();
		}
		return putereConsumata;
	}

	public void rearanjareCircuite(PanouSolar panouSolar) {
		// lista cu combinatii de circuite si puterile consumate de fiecare combinatie
		HashMap<List<Circuit>, Double> puteriConsumate = new HashMap<List<Circuit>, Double>();

		// fiecare combinatie posibila de circuite
		List<List<Circuit>> diferiteCombinatiiDeCircuite = getAllSubsets(panouSolar.getCircuite());

		// calculeaza puterea consumata pentru toate combinatiile de circuite
		for (List<Circuit> listaCuCircuite : diferiteCombinatiiDeCircuite) {
			Double putereConsumata = 0.0;
			for (Circuit circuit : listaCuCircuite) {
				putereConsumata += circuit.getPutereConsumata();
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
		panouSolar.setCircuite(nearest);
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
