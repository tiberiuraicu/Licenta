package com.server.cep.processing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.PowerSourceRepository;
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

	public Circuit makeConsumerAndCircuitConnection(Consumer consumer, Circuit circuit) {

		consumer.setCircuit(circuit);

		List<Consumer> consumersForCircuit = circuit.getConsumers();
		try {
			consumersForCircuit.remove(consumer);
		} catch (Exception e) {
			System.out.println("exceptie-----------------------");
		}

		consumersForCircuit.add(consumer);

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
		try {
			circuitsForPowerSource.remove(circuit);
		} catch (Exception e) {

		}
		circuitsForPowerSource.add(circuit);

		powerSource.setCircuits(circuitsForPowerSource);

		return powerSource;

	}

	public void setNewSetOfCircuitsToPowerSource(PowerSource powerSource, List<Circuit> circuits) {
		
		PowerSource normalPowerSource = powerSourceRepository.getPowerSourceById(2);
		
		for (Circuit circuit : powerSource.getCircuits()) {
			
			circuit.setPowerSource(normalPowerSource);
			
			normalPowerSource = makeCircuitAndPowerSourceConnection(circuit, normalPowerSource);
		}
		
		powerSourceRepository.save(normalPowerSource);
		
		for (Circuit circuit : circuits) {

			powerSource = makeCircuitAndPowerSourceConnection(circuit, powerSource);
		}

		powerSourceRepository.save(powerSource);
	}

	public Circuit makeSensorAndCircuitConnection(Sensor sensor, Circuit circuit) {

		sensor.setCircuit(circuit);

		List<Sensor> senosrsForCircuit = circuit.getSensors();

		senosrsForCircuit.add(sensor);

		circuit.setSensors(senosrsForCircuit);

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
