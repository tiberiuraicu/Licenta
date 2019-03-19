package com.server.processing.Database;

import java.text.DecimalFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.server.devicesInstructionsSender.InstructionsSender;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Device;
import com.server.entites.Instruction;
import com.server.entites.Notification;
import com.server.entites.PowerSource;
import com.server.entites.Role;
import com.server.entites.Sensor;
import com.server.entites.User;
import com.server.socket.NotificationBroadcaster;

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
	NotificationBroadcaster notificationBroadcaster;
	
	
	DecimalFormat df = new DecimalFormat("####.##");
	
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
			// replace the old consumer with the new one
			consumersForCircuit.set(consumerToBeReplacedPosition, consumer);
		}

		// if the consumer doesn't exists
		else {
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

		// set the circuit new power source
		circuit.setPowerSource(powerSource);

		// save the new circuit configuration to the database
		circuitRepository.save(circuit);

		// get the power source powerd circuits
		List<Circuit> circuitsForPowerSource = powerSource.getCircuits();

		// add the given circuit
		circuitsForPowerSource.add(circuit);

		// and set the new set of circuits to be powerd by the power source
		powerSource.setCircuits(circuitsForPowerSource);
		
		return powerSource;

	}

	// used for changing the powerd circuits from the power source
	// *mostly used for setting the best configuration after calculating it
	public PowerSource setNewSetOfCircuitsToPowerSource(PowerSource powerSource, List<Circuit> circuits) {

		// TODO -> make this more optimized
		// first : get the normal power source entity form the database
		PowerSource normalPowerSource = powerSourceRepository.getPowerSourceById(2);

		// then set all circuits from home to this power source
		for (Circuit circuitFromDatabase : circuitRepository.findAll()) {
			boolean exists = false;
			for (Circuit circuitForSolarPanel : circuits) {
				if (circuitFromDatabase.getId().equals(circuitForSolarPanel.getId())) {
					exists = true;
				}
			}
			if (exists==false) {
				circuitFromDatabase.setPowerSource(normalPowerSource);

				normalPowerSource = makeCircuitAndPowerSourceConnection(circuitFromDatabase, normalPowerSource);
			}
		}

		// save in database the new configuration
		powerSourceRepository.save(normalPowerSource);

		// second : get the solar power source entity form the database
		PowerSource solarPowerSource = powerSourceRepository.getPowerSourceById(1);

		// set the given array of circuits to this power source
		for (Circuit circuitForSolarPanel : circuits) {
			boolean exists = false;
			for (Circuit circuitInSolarPanel : solarPowerSource.getCircuits()) {
				if (circuitInSolarPanel.getId().equals(circuitForSolarPanel.getId())) {
					exists = true;
				}
			}
			if(exists==false)
				solarPowerSource = makeCircuitAndPowerSourceConnection(circuitForSolarPanel, solarPowerSource);

		}

		// save in database the new configuration of circuits for solar power source
		powerSourceRepository.save(solarPowerSource);

		circuitPowerSourceChangedInstructionSender();
		
		notificationBroadcaster.sendOutletPower(
				"New power consumption : Solar panel : "+df.format(calculateConsumedPowerForPowerSource(solarPowerSource.getCircuits()))+ " kW -> " + 
		         calculatePercentage(calculateConsumedPowerForPowerSource(solarPowerSource.getCircuits()),
		        		 calculateConsumedPowerForPowerSource(solarPowerSource.getCircuits())
		        		 +calculateConsumedPowerForPowerSource(normalPowerSource.getCircuits()))+"%,"+
		        " Normal power source : "+df.format(calculateConsumedPowerForPowerSource(normalPowerSource.getCircuits()))+" kW -> " +
		        calculatePercentage(calculateConsumedPowerForPowerSource(normalPowerSource.getCircuits()),
		        		 calculateConsumedPowerForPowerSource(solarPowerSource.getCircuits())+
		        		 calculateConsumedPowerForPowerSource(normalPowerSource.getCircuits()))+"%,"
				);
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
			// replace the old sensor with the new one
			sensorsForCircuit.set(sensorPosition, sensor);

			// if the consumer doesn't exists
		} else {
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

	public User makeDeviceAndUserConnection(User user, Device device) {
		device.getUsers().add(user);
		user.getDevices().add(device);
		return user;

	}

	public Circuit calculateAndSetCircuitPowerConsumed(Circuit circuit) {

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

		return circuit;
	}

	public void circuitPowerSourceChangedInstructionSender() {

		// for every circuit from home
		for (Circuit circuit : circuitRepository.findAll()) {

			// create a new instruction
			Instruction instruction = new Instruction();

			// with the type PowerSourceChange
			instruction.setType("PowerSourceChange");

			// set the device name = the circuit ID
			instruction.setDeviceName(circuit.getId().toString());

			// if the circuit is powerd by solar panel
			if (circuit.getPowerSource().getType().equals("solarPanel")) {
				instruction.setPowerSource("solarPowerSource");

				// if the circuit is powerd by the normal power source
			} else if (circuit.getPowerSource().getType().equals("normalPowerSource")) {
				instruction.setPowerSource("normalPowerSource");
			}

			try {
				// send the instruction to the electric panel
				instructionsSender.instructionSender(instruction);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String calculatePercentage(double obtained, double total) {
		
        return df.format(obtained * 100 / total);
    }
	
	public double calculateConsumedPowerForPowerSource(List<Circuit> circuits) {

		// initially the consumed power is 0
		double consumedPower = 0;

		// iterate trough every circuit
		for (Circuit circuit : circuits) {
			// adds its consumed power to the total
			consumedPower += circuit.getPowerConsumed();
		}
		return consumedPower;
	}

}
