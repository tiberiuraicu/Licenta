package com.server.cep.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.entites.PowerSource;
import com.server.entites.Sensor;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.Switch;
import com.server.entites.SolarPanel;
import com.server.entites.Outlet;

public class FunctiiAjutor {

	@Autowired
	PowerSourceRepository alimentatorRepository;
	@Autowired
	ConsumerRepository consumatorRepository;
	@Autowired
	SensorRepository sensorRepository;

	HelperFunctions helperFunctions = new HelperFunctions();

	public Circuit getCircuit() {
		return null;
	}

	public PowerSource getAlimentator() {
		PowerSource alimentator = new SolarPanel();
		alimentator.setPutereGenerata(200.5);
		Circuit c1 = new Circuit();
		Circuit c2 = new Circuit();
		Circuit c3 = new Circuit();
		


		Consumer unu = new Outlet();
		unu.setPowerConsumed(0.2);
		unu.setState(1);
		unu.setName("outlet1");
		Consumer doi = new Outlet();
		doi.setPowerConsumed(52.6);
		doi.setState(1);
		doi.setName("outlet2");
		Consumer trei = new Outlet();
		trei.setPowerConsumed(58.6);
		trei.setState(1);
		trei.setName("outlet3");

		Consumer patru = new Outlet();
		patru.setPowerConsumed(0.2);
		patru.setState(1);
		patru.setName("outlet4");

		Consumer cinci = new Switch();
		cinci.setPowerConsumed(55.6);
		cinci.setState(1);
		cinci.setName("switch1");
		cinci.setLocation("bathroom");

		Consumer sase = new Switch();
		sase.setPowerConsumed(58.6);
		sase.setState(1);
		sase.setName("switch2");
		sase.setLocation("lobby");

		Sensor sensor1 = new Sensor();
		sensor1.setName("sensor1");
		sensor1.setLocation("lobby");

		Sensor sensor2 = new Sensor();
		sensor2.setName("sensor2");
		sensor2.setLocation("bathroom");


		c1=helperFunctions.makeSensorAndCircuitConnection(sensor1, c1);
		System.out.println("---------------------------");
		c1=helperFunctions.makeSensorAndCircuitConnection(sensor2, c1);
		System.out.println("---------------------------");
		c2=helperFunctions.makeConsumerAndCircuitConnection(unu, c2);
		System.out.println("---------------------------");
		c2=helperFunctions.makeConsumerAndCircuitConnection(doi, c2);
		System.out.println("---------------------------");
		c3=helperFunctions.makeConsumerAndCircuitConnection(trei, c3);
		System.out.println("---------------------------");
		c3=helperFunctions.makeConsumerAndCircuitConnection(patru, c3);
		System.out.println("---------------------------");
		c1=helperFunctions.makeConsumerAndCircuitConnection(cinci, c1);
		System.out.println("---------------------------");
		c2=helperFunctions.makeConsumerAndCircuitConnection(sase, c2);
		c1.setPowerConsumed(calculateCircuitPowerConsumption(c1));
		c2.setPowerConsumed(calculateCircuitPowerConsumption(c2));
		c3.setPowerConsumed(calculateCircuitPowerConsumption(c3));


		System.out.println("---------------------------");
		alimentator=helperFunctions.makeCircuitAndPowerSourceConnection(c1, alimentator);
		System.out.println("---------------------------");
		alimentator=helperFunctions.makeCircuitAndPowerSourceConnection(c2, alimentator);
		System.out.println("---------------------------");
		alimentator=helperFunctions.makeCircuitAndPowerSourceConnection(c3, alimentator);
		System.out.println("---------------------------");

		return alimentator;

	}

	public Double calculateCircuitPowerConsumption(Circuit circuit) {
		Double putereConsumata = 0.0;
		List<Consumer> consumatori = circuit.getConsumers();
		for (Consumer consumator : consumatori)
			putereConsumata += consumator.getPowerConsumed();
		return putereConsumata;
	}

	// verifica daca puterea generata de panou solar este <=> decat noua putere
	// consumata de circuite
	public void verificareMarireConsum(SolarPanel panouSolar) {
		// preia puterea generata de panou
		double putereGenerata = panouSolar.getPutereGenerata();
		// calculeaza noua putere consumata de circuitele alimentate la panou
		double putereConsumata = calculeazaPutereConsumata(panouSolar.getCircuits());

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

	// de modificat
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
