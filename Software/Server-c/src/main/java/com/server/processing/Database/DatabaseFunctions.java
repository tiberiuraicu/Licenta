package com.server.processing.Database;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.Notification;
import com.server.entites.PowerSource;
import com.server.entites.Role;
import com.server.entites.Sensor;
import com.server.entites.User;
import com.server.processing.Sockets.SocketFunctions;

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
	@Autowired
	InstructionsSender instructionsSender;
	@Autowired
	SocketFunctions socketFunctions;

	// make the connection between Consumer and a Circuit or,
	// if it already exists in circuit update its dates

	public Circuit makeConsumerAndCircuitConnection(Consumer consumer, Circuit circuit) {

		// sets the circuit for the specific Consumer
		consumer.setCircuit(circuit);

		// gets all consumers for the circuit
		List<Consumer> consumersForCircuit;

		consumersForCircuit = circuit.getConsumers();

		Consumer consumerToBeReplaced = null;

		int consumerToBeReplacedPosition = 0;

		// iterate trough all consumers from circuit
		for (int i = 0; i < consumersForCircuit.size(); i++) {
			// and if one is found with the same name
			if (consumersForCircuit.get(i).getName().equals(consumer.getName())) {
				// store it in an intermediate variabile
				consumerToBeReplaced = consumersForCircuit.get(i);
				// and store its position in an intermediate variabile
				consumerToBeReplacedPosition = i;
				// break out of the loop
				break;
			}
		}

		if (consumerToBeReplaced != null) {
			// remove the link with the Circuit
			consumerToBeReplaced.setCircuit(null);
			// save in database
			consumerRepository.save(consumerToBeReplaced);
			// sets the circuit for the specific Consumer
			consumer.setCircuit(circuit);

			consumerRepository.save(consumer);
			// replace the old consumer with the new one
			consumersForCircuit.set(consumerToBeReplacedPosition, consumer);
		}

		// if the consumer doesn't exists
		else if (consumerToBeReplaced == null) {
			// add the consumer to array
			consumersForCircuit.add(consumer);
		}

		// set the new list of consumers for circuit
		circuit.setConsumers(consumersForCircuit);

		return circuit;
	}

	// TODO *test
	public Device makeCircuitAndDeviceConnection(Circuit circuit, Device device) {

		circuit.setDevice(device);

		List<Circuit> circuitsForDevice = device.getCircuits();

		circuitsForDevice.add(circuit);

		device.setCircuits(circuitsForDevice);

		return device;

	}

	// add the circuit to the power source array (power it by solar panel)
	// set the power source of the circuit to the given power source
	public PowerSource makeCircuitAndPowerSourceConnection(Circuit circuit, PowerSource powerSource) {

		// get the power source powerd circuits
		List<Circuit> circuitsForPowerSource = powerSource.getCircuits();

		boolean exists = false;

		// iterate trough the circuits that will be
		// powerd by solar panel
		for (Circuit circuitFromPowerSource : circuitsForPowerSource) {

			// if the circuit exists in the new configuration we'll leave it there
			if (circuitFromPowerSource.getId().equals(circuit.getId())) {
				exists = true;
			}
		}

		// if not, change its power source to normal power source
		if (exists == false) {

			// set the circuit new power source
			circuit.setPowerSource(powerSource);

			// save the new circuit configuration to the database
			circuitRepository.save(circuit);

			// add the given circuit
			circuitsForPowerSource.add(circuit);

			// and set the new set of circuits to be powerd by the power source
			powerSource.setCircuits(circuitsForPowerSource);
		}
		return powerSource;

	}

	// used for changing the powerd circuits from the power source
	// *mostly used for setting the best configuration after calculating it
	@Transactional
	public PowerSource setNewSetOfCircuitsToPowerSource(PowerSource powerSource, List<Circuit> circuits) {

		// first : get the normal power source entity form the database
		PowerSource normalPowerSource = powerSourceRepository.getPowerSourceById(2);

		// second : get the solar power source entity form the database
		PowerSource solarPowerSource = powerSourceRepository.getPowerSourceById(1);

		// then set all circuits that are not in the new configuration
		// to the normal power source
		for (Circuit circuitFromDatabase : circuitRepository.findAll()) {

			// initially we suppose the selected circuit
			// from database is not powerd by solar panel
			boolean exists = false;

			// iterate trough the circuits that will be
			// powerd by solar panel
			for (Circuit circuitForSolarPanel : circuits) {

				// if the circuit exists in the new configuration we'll leave it there
				if (circuitFromDatabase.getId().equals(circuitForSolarPanel.getId())) {
					exists = true;
				}
			}

			// if not, change its power source to normal power source
			if (exists == false) {
				// but first verify and eliminate the circuits from the other power source array
				// this is for not being the same circuit in both arrays of the power spurces
				List<Circuit> list = solarPowerSource.getCircuits();

				for (Iterator<Circuit> it = list.iterator(); it.hasNext();) {

					if (it.next().getId().equals(circuitFromDatabase.getId())) {

						it.remove();
					}
				}

				solarPowerSource.setCircuits(list);

				powerSourceRepository.save(solarPowerSource);

				normalPowerSource = makeCircuitAndPowerSourceConnection(circuitFromDatabase, normalPowerSource);
			}
		}

		// save in database the new configuration
		powerSourceRepository.save(normalPowerSource);

		// set the given array of circuits to this power source
		for (Circuit circuitForSolarPanel : circuits) {

			// initially we suppose the selected circuit
			// from database is not powerd by solar panel
			boolean exists = false;

			for (Circuit circuitInSolarPanel : solarPowerSource.getCircuits()) {

				// if the circuit is already powerd by solar panel we will not add it again
				if (circuitInSolarPanel.getId().equals(circuitForSolarPanel.getId())) {
					exists = true;
				}
			}

			// if not,change its power source to solar panel
			if (exists == false) {
				// but first verify and eliminate the circuits from the other power source array
				// this is for not being the same circuit in both arrays of the power spurces
				List<Circuit> list = normalPowerSource.getCircuits();

				for (Iterator<Circuit> it = list.iterator(); it.hasNext();) {

					if (it.next().getId().equals(circuitForSolarPanel.getId())) {

						it.remove();
					}
				}

				normalPowerSource.setCircuits(list);

				powerSourceRepository.save(normalPowerSource);

				solarPowerSource = makeCircuitAndPowerSourceConnection(circuitForSolarPanel, solarPowerSource);
			}
		}

		// save in database the new configuration of circuits for solar power source
		powerSourceRepository.save(solarPowerSource);

		// send instruction to the electric panel with the new configuration
		instructionsSender.changeCircuitPowerSource();

		// send notification to front end with the new power source consumption
		socketFunctions.sendNewPowerConsumptionNotification(solarPowerSource, normalPowerSource);

		return solarPowerSource;
	}

	// make the connection between Sensor and a Circuit or,
	// if it already exists in circuit update its dates
	public Circuit makeSensorAndCircuitConnection(Sensor sensor, Circuit circuit) {

		// sets the circuit for the specific Sensor
		sensor.setCircuit(circuit);

		// gets all sensors for the circuit
		List<Sensor> sensorsForCircuit = circuit.getSensors();
		Sensor sensorFromCircuit = null;
		int sensorPosition = 0;

		// iterate trough all sensors from circuit
		for (int i = 0; i < sensorsForCircuit.size(); i++) {
			// and if one is found with the same name
			if (sensorsForCircuit.get(i).getName().equals(sensor.getName())) {
				// store it in an intermediate variabile
				sensorFromCircuit = sensorsForCircuit.get(i);
				// and store its position in an intermediate variabile
				sensorPosition = i;
				// break out of the loop
				break;
			}
		}
		if (sensorFromCircuit != null) {
			// remove the link with the Circuit
			sensorFromCircuit.setCircuit(null);
			// save in database
			sensorRepository.save(sensorFromCircuit);

			// sets the circuit for the specific Consumer
			sensor.setCircuit(circuit);

			sensorRepository.save(sensor);

			// replace the old sensor with the new one
			sensorsForCircuit.set(sensorPosition, sensor);

			// if the consumer doesn't exists
		} else if (sensorFromCircuit == null) {

			// add the consumer to array
			sensorsForCircuit.add(sensor);
		}
		// set the new list of sensors for circuit
		circuit.setSensors(sensorsForCircuit);

		return circuit;
	}

	// TODO *test
	public Device makeNotificationAndDeviceConnection(Notification notification, Device device) {

		notification.setDevice(device);

		List<Notification> notificationsForDevice = device.getNotifications();

		notificationsForDevice.add(notification);

		device.setNotifications(notificationsForDevice);

		return device;

	}

	// TODO *test
	public User makeRoleAndUserConnection(User user, Role role) {

		role.setUser(user);

		user.setRole(role);

		return user;

	}

	// TODO *test
	public User makeDeviceAndUserConnection(User user, Device device) {
		device.getUsers().add(user);
		user.getDevices().add(device);
		return user;

	}

	public Circuit calculateAndSetCircuitPowerConsumed(Circuit circuit) {
		try {
			// initialize the total power with zero
			Double cumulatedPowerConsumed = 0.0;

			// iterate trough every sensor
			for (Sensor sensor : circuit.getSensors())
				// add its power consumed to the cumulated power
				cumulatedPowerConsumed += sensor.getPowerConsumed();

			// iterate trough every consumer
			for (Consumer consumer : circuit.getConsumers())
				// add its power consumed to the cumulated power
				cumulatedPowerConsumed += consumer.getPowerConsumed();

			// set the circuit its new consumed power
			circuit.setPowerConsumed(cumulatedPowerConsumed);
		} catch (Exception e) {

		}
		return circuit;
	}

}
