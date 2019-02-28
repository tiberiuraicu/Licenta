package com.server.processing.Database;

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
import com.server.entites.Notification;
import com.server.entites.PowerSource;
import com.server.entites.Role;
import com.server.entites.Sensor;
import com.server.entites.User;

@Component
public class DatabaseFunctions {

	@Autowired
	CircuitRepository circuitRepository;
	@Autowired
	PowerSourceRepository powerSourceRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	ConsumerRepository consumerRepository;

	// make the connection between Consumer and a Circuit or,
	// if it already exists in circuit update its dates
	public Circuit makeConsumerAndCircuitConnection(Consumer consumer, Circuit circuit) {

		// sets the circuit for the specific Consumer
		consumer.setCircuit(circuit);

		// gets all consumers for the circuit
		List<Consumer> consumersForCircuit = circuit.getConsumers();

		Consumer consumerToBeReplaced = null;

		int consumerToBeReplacedPosition = 0;

		// iterate trough all consumers from circuit
		for (int i = 0; i < consumersForCircuit.size(); i++) {
			// and if one is found with the same name
			if (consumersForCircuit.get(i).getName().equals(consumer.getName())) {
               //store it in an intermediate variabile
				consumerToBeReplaced = consumersForCircuit.get(i);
				 //and store its position in an intermediate variabile
				consumerToBeReplacedPosition = i;
				break;
			}
		}	
		
		if (consumerToBeReplaced != null) {
			//remove the link with the Circuit
			consumerToBeReplaced.setCircuit(null);
			//save in database 
			consumerRepository.save(consumerToBeReplaced);
			//replace the old consumer with the new one
			consumersForCircuit.set(consumerToBeReplacedPosition, consumer);
		} 
		
		//if the consumer doesn't exists 
		else {
            //add the consumer to array
			consumersForCircuit.add(consumer);
		}
		
		//seteaza lista actualizata de consumatori pentru circuit
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

		circuitRepository.save(circuit);

		List<Circuit> circuitsForPowerSource = powerSource.getCircuits();

		circuitsForPowerSource.add(circuit);

		powerSource.setCircuits(circuitsForPowerSource);

		return powerSource;

	}

	public void setNewSetOfCircuitsToPowerSource(PowerSource powerSource, List<Circuit> circuits) {

		PowerSource normalPowerSource = powerSourceRepository.getPowerSourceById(2);

		for (Circuit circuit : circuitRepository.findAll()) {

			circuit.setPowerSource(normalPowerSource);

			normalPowerSource = makeCircuitAndPowerSourceConnection(circuit, normalPowerSource);
		}

		powerSourceRepository.save(normalPowerSource);

		PowerSource solarPowerSource = powerSourceRepository.getPowerSourceById(1);

		for (Circuit circuit : circuits) {

			solarPowerSource = makeCircuitAndPowerSourceConnection(circuit, solarPowerSource);
		}

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
