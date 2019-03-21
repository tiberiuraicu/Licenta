package com.server.cep.processing;

import java.util.ArrayList;
import java.util.Date;
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
import com.server.database.repositories.ScenarioRepository;
import com.server.database.repositories.SensorRepository;
import com.server.database.repositories.UserRepository;
import com.google.common.collect.Sets;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.DieviceRepository;
import com.server.entites.PowerSource;
import com.server.entites.Scenario;
import com.server.entites.Sensor;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.NormalPowerSource;
import com.server.entites.Switch;
import com.server.entites.User;
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
	ScenarioRepository scenarioRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DieviceRepository deviceRepository;
	@Autowired
	HelperFunctions helperFunctions;

	public PowerSource getAlimentator() {

		registerScenarioToDatabase();

		PowerSource powerSource = new SolarPanel();
		powerSource.setGeneratedPower(300.5);

		User user = new User();
		user.setEmail("t");
		user.setPassword("t");
		userRepository.save(user);

		Circuit c1 = new Circuit();
		Circuit c2 = new Circuit();
		Circuit c3 = new Circuit();

		Consumer unu = new Outlet();
		unu.setPowerConsumed(0.2);
		unu.setState(1);
		unu.setName("outlet1");
		unu.setLocation("bathroom");
		unu.setTimestamp(new Date());
		Consumer doi = new Outlet();
		doi.setPowerConsumed(52.6);
		doi.setState(1);
		doi.setName("outlet2");
		doi.setLocation("bathroom");
		doi.setTimestamp(new Date());
		Consumer trei = new Outlet();
		trei.setPowerConsumed(58.6);
		trei.setState(1);
		trei.setName("outlet3");
		trei.setLocation("lobby");
		trei.setTimestamp(new Date());
		Consumer patru = new Outlet();
		patru.setPowerConsumed(0.2);
		patru.setState(1);
		patru.setName("outlet4");
		patru.setLocation("lobby");
		patru.setTimestamp(new Date());
		Consumer cinci = new Switch();
		cinci.setPowerConsumed(55.6);
		cinci.setState(1);
		cinci.setName("switch1");
		cinci.setLocation("bathroom");
		cinci.setTimestamp(new Date());

		Consumer sase = new Switch();
		sase.setPowerConsumed(58.6);
		sase.setState(1);
		sase.setName("switch2");
		sase.setLocation("lobby");
		sase.setTimestamp(new Date());

		Sensor sensor1 = new Sensor();
		sensor1.setName("sensor1");
		sensor1.setLocation("lobby");
		sensor1.setPowerConsumed(2.0);
		sensor1.setTimestamp(new Date());

		Sensor sensor2 = new Sensor();
		sensor2.setName("sensor2");
		sensor2.setLocation("bathroom");
		sensor2.setPowerConsumed(2.0);
		sensor2.setTimestamp(new Date());

		Circuit c4 = new Circuit();
		Circuit c5 = new Circuit();
		Circuit c6 = new Circuit();

		Consumer sapte = new Outlet();
		sapte.setPowerConsumed(0.2);
		sapte.setState(1);
		sapte.setName("outlet5");
		sapte.setLocation("bathroom");
		sapte.setTimestamp(new Date());

		Consumer opt = new Outlet();
		opt.setPowerConsumed(52.6);
		opt.setState(1);
		opt.setName("outlet6");
		opt.setLocation("bathroom");
		opt.setTimestamp(new Date());

		Consumer noua = new Outlet();
		noua.setPowerConsumed(58.6);
		noua.setState(1);
		noua.setName("outlet7");
		noua.setLocation("lobby");
		noua.setTimestamp(new Date());
		Consumer zece = new Outlet();
		zece.setPowerConsumed(0.2);
		zece.setState(1);
		zece.setName("outlet8");
		zece.setLocation("lobby");
		zece.setTimestamp(new Date());
		Consumer unsprezece = new Switch();
		unsprezece.setPowerConsumed(55.6);
		unsprezece.setState(1);
		unsprezece.setName("switch3");
		unsprezece.setLocation("bathroom");
		unsprezece.setTimestamp(new Date());
		Consumer doisprezece = new Switch();
		doisprezece.setPowerConsumed(58.6);
		doisprezece.setState(1);
		doisprezece.setName("switch4");
		doisprezece.setLocation("lobby");
		doisprezece.setTimestamp(new Date());
		Sensor sensor3 = new Sensor();
		sensor3.setName("sensor3");
		sensor3.setLocation("lobby");
		sensor3.setPowerConsumed(2.0);
		sensor3.setTimestamp(new Date());
		Sensor sensor4 = new Sensor();
		sensor4.setName("sensor4");
		sensor4.setLocation("bathroom");
		sensor4.setPowerConsumed(2.0);
		sensor4.setTimestamp(new Date());
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

		Device device = new Device();
		device.setId(5);
		device = helperFunctions.makeCircuitAndDeviceConnection(c1, device);
		device = helperFunctions.makeCircuitAndDeviceConnection(c2, device);
		device = helperFunctions.makeCircuitAndDeviceConnection(c3, device);
		device = helperFunctions.makeCircuitAndDeviceConnection(c4, device);
		device = helperFunctions.makeCircuitAndDeviceConnection(c5, device);
		device = helperFunctions.makeCircuitAndDeviceConnection(c6, device);

		user = helperFunctions.makeDeviceAndUserConnection(user, device);
		userRepository.save(user);

		NormalPowerSource normalPowerSource=new NormalPowerSource();
		normalPowerSource.setDevice(device);
		powerSourceRepository.save(normalPowerSource);
		
		powerSource.setDevice(device);
		
		return powerSource;

	}

	public Double calculateCircuitPowerConsumption(Circuit circuit) {
		Double putereConsumata = 0.0;
		List<Consumer> consumatori = circuit.getConsumers();
		for (Consumer consumator : consumatori)
			putereConsumata += consumator.getPowerConsumed();
		return putereConsumata;
	}

	public double calculeazaPutereConsumata(List<Circuit> circuite) {
		double putereConsumata = 0;
		for (Circuit circuit : circuite) {
			putereConsumata += circuit.getPowerConsumed();
		}
		return putereConsumata;
	}

	public void registerScenarioToDatabase() {

		Scenario scenarioOne = returnScenario("sensor1", "switch1", 10, 10);
		Scenario scenarioTwo = returnScenario("sensor2", "switch2", 10, 10);
		Scenario scenarioThree = returnScenario("sensor3", "switch3", 10, 10);
		Scenario scenarioFour = returnScenario("sensor4", "switch4", 10, 10);

		scenarioRepository.save(scenarioOne);
		scenarioRepository.save(scenarioTwo);
		scenarioRepository.save(scenarioThree);
		scenarioRepository.save(scenarioFour);

	}

	public Scenario returnScenario(String sensorName, String switchName, double sensorRegisterTime,
			double switchRegisterTime) {
		Scenario scenario = new Scenario();
		scenario.setSensorName(sensorName);
		scenario.setSwitchName(switchName);
		scenario.setSensorRegisterTime(sensorRegisterTime);
		scenario.setSwitchRegisterTime(switchRegisterTime);
		return scenario;

	}

}
