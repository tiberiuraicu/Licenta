package com.server.cep.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.google.common.collect.Sets;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.entites.PowerSource;
import com.server.entites.Sensor;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.NormalPowerSource;
import com.server.entites.Switch;
import com.server.entites.SolarPanel;
import com.server.entites.Outlet;

@Component
public class FunctiiAjutor {

	@Autowired
	PowerSourceRepository powerSourceRepository;
	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	CircuitRepository circuitReporitory;
	@Autowired
	HelperFunctions helperFunctions;

	private BlockingQueue<Set<Set<Circuit>>> blockQue = new ArrayBlockingQueue<Set<Set<Circuit>>>(1024);

	public PowerSource getAlimentator() {

		PowerSource powerSource = new SolarPanel();
		powerSource.setGeneratedPower(300.5);
		Circuit c1 = new Circuit();
		Circuit c2 = new Circuit();
		Circuit c3 = new Circuit();

		Consumer unu = new Outlet();
		unu.setPowerConsumed(0.2);
		unu.setState(1);
		unu.setName("outlet1");
		unu.setLocation("bathroom");
		Consumer doi = new Outlet();
		doi.setPowerConsumed(52.6);
		doi.setState(1);
		doi.setName("outlet2");
		doi.setLocation("bathroom");
		Consumer trei = new Outlet();
		trei.setPowerConsumed(58.6);
		trei.setState(1);
		trei.setName("outlet3");
		trei.setLocation("lobby");
		Consumer patru = new Outlet();
		patru.setPowerConsumed(0.2);
		patru.setState(1);
		patru.setName("outlet4");
		patru.setLocation("lobby");
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
		sensor1.setPowerConsumed(2.0);

		Sensor sensor2 = new Sensor();
		sensor2.setName("sensor2");
		sensor2.setLocation("bathroom");
		sensor2.setPowerConsumed(2.0);

		Circuit c4 = new Circuit();
		Circuit c5 = new Circuit();
		Circuit c6 = new Circuit();

		Consumer sapte = new Outlet();
		sapte.setPowerConsumed(0.2);
		sapte.setState(1);
		sapte.setName("outlet5");
		sapte.setLocation("bathroom");
		Consumer opt = new Outlet();
		opt.setPowerConsumed(52.6);
		opt.setState(1);
		opt.setName("outlet6");
		opt.setLocation("bathroom");
		Consumer noua = new Outlet();
		noua.setPowerConsumed(58.6);
		noua.setState(1);
		noua.setName("outlet7");
		noua.setLocation("lobby");
		Consumer zece = new Outlet();
		zece.setPowerConsumed(0.2);
		zece.setState(1);
		zece.setName("outlet8");
		zece.setLocation("lobby");
		Consumer unsprezece = new Switch();
		unsprezece.setPowerConsumed(55.6);
		unsprezece.setState(1);
		unsprezece.setName("switch3");
		unsprezece.setLocation("bathroom");

		Consumer doisprezece = new Switch();
		doisprezece.setPowerConsumed(58.6);
		doisprezece.setState(1);
		doisprezece.setName("switch4");
		doisprezece.setLocation("lobby");

		Sensor sensor3 = new Sensor();
		sensor3.setName("sensor3");
		sensor3.setLocation("lobby");
		sensor3.setPowerConsumed(2.0);

		Sensor sensor4 = new Sensor();
		sensor4.setName("sensor4");
		sensor4.setLocation("bathroom");
		sensor4.setPowerConsumed(2.0);

		c1 = helperFunctions.makeSensorAndCircuitConnection(sensor1, c1);
		c1 = helperFunctions.makeSensorAndCircuitConnection(sensor2, c1);
		c2 = helperFunctions.makeConsumerAndCircuitConnection(unu, c2);
		c2 = helperFunctions.makeConsumerAndCircuitConnection(doi, c2);
		c3 = helperFunctions.makeConsumerAndCircuitConnection(trei, c3);
		c3 = helperFunctions.makeConsumerAndCircuitConnection(patru, c3);
		c1 = helperFunctions.makeConsumerAndCircuitConnection(cinci, c1);
		c2 = helperFunctions.makeConsumerAndCircuitConnection(sase, c2);

		c4 = helperFunctions.makeSensorAndCircuitConnection(sensor3, c4);
		c4 = helperFunctions.makeSensorAndCircuitConnection(sensor4, c4);
		c5 = helperFunctions.makeConsumerAndCircuitConnection(sapte, c5);
		c5 = helperFunctions.makeConsumerAndCircuitConnection(opt, c5);
		c6 = helperFunctions.makeConsumerAndCircuitConnection(noua, c6);
		c6 = helperFunctions.makeConsumerAndCircuitConnection(zece, c6);
		c4 = helperFunctions.makeConsumerAndCircuitConnection(unsprezece, c4);
		c5 = helperFunctions.makeConsumerAndCircuitConnection(doisprezece, c5);

		c1.setPowerConsumed(calculateCircuitPowerConsumption(c1));
		c2.setPowerConsumed(calculateCircuitPowerConsumption(c2));
		c3.setPowerConsumed(calculateCircuitPowerConsumption(c3));
		c4.setPowerConsumed(calculateCircuitPowerConsumption(c4));
		c5.setPowerConsumed(calculateCircuitPowerConsumption(c5));
		c6.setPowerConsumed(calculateCircuitPowerConsumption(c6));

		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c1, powerSource);
		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c2, powerSource);
		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c3, powerSource);
		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c4, powerSource);
		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c5, powerSource);
		powerSource = helperFunctions.makeCircuitAndPowerSourceConnection(c6, powerSource);

		return powerSource;

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
	public void verificareMarireConsum(PowerSource powerSource) throws InterruptedException {
		// preia puterea generata de panou
		double putereGenerata = powerSourceRepository.getPowerSourceById(1).getGeneratedPower();
		// calculeaza noua putere consumata de circuitele alimentate la panou
		double putereConsumata = calculeazaPutereConsumata(powerSourceRepository.getPowerSourceById(1).getCircuits());

		if (putereGenerata > putereConsumata) {

			verificaDacaSeMaiPoateAdaugaConsumatorLaPanou();
			// TODO informeaza frontend ca a fost marat consumul
		} else if (putereGenerata < putereConsumata) {
//			System.out.println("subscriber");
			// rearanjeaza circuitele in cel mai bun mod posibil
			// astfel incat puterea consumata de circuitele conectate
			// la panou sa fie mai mica decat puterea generata
			rearanjareCircuite(powerSource);

		}
	}

	public void verificaDacaSeMaiPoateAdaugaConsumatorLaPanou() throws InterruptedException {
		PowerSource powerSource = powerSourceRepository.getPowerSourceById(1);
		List<Circuit> circuitsForPowerSource = powerSource.getCircuits();
		double putereConsumata = calculeazaPutereConsumata(circuitsForPowerSource);
		List<Circuit> allCircuitsFormHome = circuitReporitory.findAll();

		Double powerAvailable = powerSource.getGeneratedPower() - putereConsumata;
		if (powerAvailable > 0) {

			for (Circuit circuit : allCircuitsFormHome) {
				if (circuit.getPowerConsumed() < powerAvailable) {
//				   System.out.println("subscriber");
					rearanjareCircuite(powerSource);
					break;
				}
			}
		}

	}

	public double calculeazaPutereConsumata(List<Circuit> circuite) {
		double putereConsumata = 0;
		for (Circuit circuit : circuite) {
			putereConsumata += circuit.getPowerConsumed();
		}
		return putereConsumata;
	}

	public void rearanjareCircuite(PowerSource powerSource) throws InterruptedException {
		// lista cu combinatii de circuite si puterile consumate de fiecare combinatie
		HashMap<List<Circuit>, Double> puteriConsumate = new HashMap<List<Circuit>, Double>();

		Set<Circuit> set = new HashSet<Circuit>(circuitReporitory.findAll());

		Set<Set<Circuit>> result = Sets.powerSet(Sets.newHashSet(set));

		// calculeaza puterea consumata pentru toate combinatiile de circuite
		for (Set<Circuit> setCuCircuite : result) {

			List<Circuit> listaCuCircuite = new Vector<Circuit>();
			listaCuCircuite.addAll(setCuCircuite);
			Double putereConsumata = 0.0;
			for (Circuit circuit : listaCuCircuite) {
				putereConsumata += circuit.getPowerConsumed();
			}
			
			puteriConsumate.put(listaCuCircuite, putereConsumata);
		}
		
//		// calculeaza cea mai apropiata valoare a puterii consumate
		// a diferitelor combinatii fata de puterea generata de panou solar

		Double min = 10000.0;
		List<Circuit> nearest = null;
		for (List<Circuit> key : puteriConsumate.keySet()) {

			Double temp = powerSource.getGeneratedPower() - puteriConsumate.get(key);

			if (temp > 0) {

				if (temp < min) {
					min = temp;
					nearest = key;

				}
			}
		}

		helperFunctions.setNewSetOfCircuitsToPowerSource(powerSourceRepository.getPowerSourceById(1), nearest);
	}

}
