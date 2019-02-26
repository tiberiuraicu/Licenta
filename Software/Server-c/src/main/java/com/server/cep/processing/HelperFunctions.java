package com.server.cep.processing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.NormalPowerSource;
import com.server.entites.Notification;
import com.server.entites.PowerSource;
import com.server.entites.Role;
import com.server.entites.Sensor;
import com.server.entites.User;

//TODO metoda de verificare (daca apare de mai multe ori) dupa id
@Component
public class HelperFunctions {

	@Autowired
	CircuitRepository circuitRepository;
	@Autowired
	PowerSourceRepository powerSourceRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	ConsumerRepository consumerRepository;

	public Circuit makeConsumerAndCircuitConnection(Consumer consumer, Circuit circuit) {

		consumer.setCircuit(circuit);

		List<Consumer> consumersForCircuit = circuit.getConsumers();
		Consumer consumerFromCircuit = null;
		int consumerPosition = 0;

		for (int i = 0; i < consumersForCircuit.size(); i++) {
			if (consumersForCircuit.get(i).getName().equals(consumer.getName())) {

				consumerFromCircuit = consumersForCircuit.get(i);
				consumerPosition = i;
				break;

			}
		}
		if (consumerFromCircuit != null) {
			consumerFromCircuit.setCircuit(null);

			consumerRepository.save(consumerFromCircuit);
			consumersForCircuit.set(consumerPosition, consumer);
		} else {

			consumersForCircuit.add(consumer);
		}

		circuit.setConsumers(consumersForCircuit);

		return circuit;
	}

	public Device makeCircuitAndDeviceConnection(Circuit circuit, Device device) {

		circuit.setDevice(device);

		List<Circuit> circuitsForDevice = device.getCircuits();

		circuitsForDevice.add(circuit);

		device.setCircuits(circuitsForDevice);

		return device;

	}

	public PowerSource makeCircuitAndPowerSourceConnection(Circuit circuit, PowerSource powerSource) {

		circuit.setPowerSource(powerSource);

		List<Circuit> circuitsForPowerSource = powerSource.getCircuits();
		
//		System.out.println(circuitsForPowerSource.toString());
//		System.out.println("______________________________");
//		for (int i = 0; i < circuitsForPowerSource.size(); i++)
//			if (circuitsForPowerSource.get(i).getId().equals(circuit.getId())) {
//				circuitsForPowerSource.get(i).setPowerSource(powerSourceRepository.getPowerSourceById(2));
//				circuitRepository.save(circuitsForPowerSource.get(i));
//				circuitsForPowerSource.set(i, circuit);
//			}
		circuitsForPowerSource.add(circuit);

		powerSource.setCircuits(circuitsForPowerSource);

		return powerSource;

	}

	public void setNewSetOfCircuitsToPowerSource(PowerSource powerSource, List<Circuit> circuits) {

		PowerSource normalPowerSource = powerSourceRepository.getPowerSourceById(2);

		for (Circuit circuit :circuitRepository.findAll()) {

			circuit.setPowerSource(normalPowerSource);

			normalPowerSource = makeCircuitAndPowerSourceConnection(circuit, normalPowerSource);
		}

		powerSourceRepository.save(normalPowerSource);
		PowerSource solarPowerSource = powerSourceRepository.getPowerSourceById(1);
		for (Circuit circuit : circuits) {

			powerSource = makeCircuitAndPowerSourceConnection(circuit, powerSource);
		}
System.out.println(solarPowerSource.getCircuits().toString());
		powerSourceRepository.save(solarPowerSource);
	}

	public Circuit makeSensorAndCircuitConnection(Sensor sensor, Circuit circuit) {

		sensor.setCircuit(circuit);

		List<Sensor> sensorsForCircuit = circuit.getSensors();

		Sensor sensorFromCircuit = null;
		int sensorPosition = 0;

		for (int i = 0; i < sensorsForCircuit.size(); i++) {
			if (sensorsForCircuit.get(i).getName().equals(sensor.getName())) {

				sensorFromCircuit = sensorsForCircuit.get(i);
				sensorPosition = i;
				break;

			}
		}
		if (sensorFromCircuit != null) {
			sensorFromCircuit.setCircuit(null);

			sensorRepository.save(sensorFromCircuit);
			sensorsForCircuit.set(sensorPosition, sensor);
		} else {

			sensorsForCircuit.add(sensor);
		}
		circuit.setSensors(sensorsForCircuit);

		return circuit;
	}

	public Device makeNotificationAndDeviceConnection(Notification notification, Device device) {

		notification.setDevice(device);

		List<Notification> notificationsForDevice = device.getNotifications();

		notificationsForDevice.add(notification);

		device.setNotifications(notificationsForDevice);

		return device;

	}

	public User makeNotificationAndDeviceConnection(User user, Role role) {

		role.setUser(user);

		user.setRole(role);

		return user;

	}

	public Circuit calculateAndSetCircuitPowerConsumed(Circuit circuit) {

		Double cumulatedPowerConsumed = 0.0;

		for (Sensor sensor : circuit.getSensors())
			cumulatedPowerConsumed += sensor.getPowerConsumed();

		for (Consumer consumer : circuit.getConsumers())
			cumulatedPowerConsumed += consumer.getPowerConsumed();

		circuit.setPowerConsumed(cumulatedPowerConsumed);

		return circuit;
	}

	public Double calculatePowerSourceCircuitsPowerConsumed(PowerSource powerSource) {

		Double cumulatedPowerConsumed = 0.0;

		for (Circuit circuit : powerSource.getCircuits())
			cumulatedPowerConsumed += circuit.getPowerConsumed();

		return cumulatedPowerConsumed;
	}

}
