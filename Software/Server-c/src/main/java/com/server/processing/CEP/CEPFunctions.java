package com.server.processing.CEP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.database.repositories.PowerSourceRepository;
import com.server.database.repositories.SensorRepository;
import com.google.common.collect.Sets;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.entites.PowerSource;
import com.server.processing.Database.DatabaseFunctions;
import com.server.entites.Circuit;

@Component
public class CEPFunctions {

	@Autowired
	PowerSourceRepository powerSourceRepository;
	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	SensorRepository sensorRepository;
	@Autowired
	CircuitRepository circuitReporitory;
	@Autowired
	DatabaseFunctions databaseFunctions;

	// checks if the power generated by the solar panel is greater,
	// less than or equal to the new power consumed by the circuits
	public void energyStatusCheckForSolarPanel(PowerSource powerSource) throws InterruptedException {

		// gets the solar panel generated power
		double generatedPower = powerSourceRepository.getPowerSourceById(1).getGeneratedPower();

		// calculates the new power consumed by the circuits connected to the power
		// source
		double consumedPower = calculateConsumedPowerForPowerSource(powerSource.getCircuits());

		if (generatedPower > consumedPower) {
			// if the power generated is greater then the one consumed
			// see if a circuit can be added for consumption
			checkIfACircuitCanBeAddedToPowerSource(powerSource, consumedPower);

		} else if (generatedPower < consumedPower) {
			// if the power consumed is greater then the one generated
			// rearrange circuits for a maximum usage of the power from solar panel
			rearrangePowerSourceCircuits(powerSource);
		}
	}

	public void checkIfACircuitCanBeAddedToPowerSource(PowerSource powerSource, double consumedPower)
			throws InterruptedException {

		// gets all circuits from home
		List<Circuit> allCircuitsFormHome = circuitReporitory.findAll();

		// calculates if exists unused generated power
		Double powerAvailable = powerSource.getGeneratedPower() - consumedPower;
		
        //TODO -> make this a function
		// if there is unused power
		if (powerAvailable > 0) {
			// iterate trough every circuit from home
			for (Circuit circuit : allCircuitsFormHome) {
				// and if a circuit is found with the consumed power
				// lower then the available power
				if (circuit.getPowerConsumed() < powerAvailable) {
					// rearrange circuits for a maximum
					// usage of the power from solar panel
					databaseFunctions.makeCircuitAndPowerSourceConnection(circuit, powerSource);
					powerSourceRepository.save(powerSource);
					//rearrangePowerSourceCircuits(powerSource);
					break;
				}
			}
		}
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

	public void rearrangePowerSourceCircuits(PowerSource powerSource) throws InterruptedException {

		// the list of circuit combinations and the power consumed by each combination
		HashMap<Set<Circuit>, Double> powerConsumedForEveryCombinationOfCircuits = new HashMap<Set<Circuit>, Double>();

		// generates every possible combination of this home circuits
		Set<Set<Circuit>> everyCircuitCombination = Sets
				.powerSet(Sets.newHashSet(new HashSet<Circuit>(circuitReporitory.findAll())));

		// calculates the consumed power for
		// every possible combination of circuits
		for (Set<Circuit> circuitsSet : everyCircuitCombination) {

			// initially the consumed power is 0
			Double consumedPower = 0.0;
			for (Circuit circuit : circuitsSet) {
				// adds its consumed power to the total
				consumedPower += circuit.getPowerConsumed();
			}
			// puts the list with corresponding power consumed in a HashMap
			powerConsumedForEveryCombinationOfCircuits.put(circuitsSet, consumedPower);
		}

		// calculates the nearest value of power consumption o
		// to the power generated by the solar panel
		
		// first : initialize a min vriable with a random big value
		Double min = 10000.0;
		Set<Circuit> nearest = null;
		// iterate trough the set of lists with their coresponding power consumed
		for (Set<Circuit> key : powerConsumedForEveryCombinationOfCircuits.keySet()) {

			// calculate the difference between the generated power and the consumed power
			// of the list
			Double difference = powerSource.getGeneratedPower() - powerConsumedForEveryCombinationOfCircuits.get(key);
			// if the difference is bigger than zero
			if (difference > 0) {
				//and the difference is less than the minimum 
				if (difference < min) {
					//that difference becomes the new minimum
					min = difference;
					//and the corresponding list becomes the 
					//best configuration of circuits to be powerd by solar panel
					nearest = key;
				}
			}
		}
		//transforms the set in a list
		List<Circuit> nearestAsList = new Vector<Circuit>(nearest);

		//sets the new circuit configuration to be powerd by solar panel
		databaseFunctions.setNewSetOfCircuitsToPowerSource(powerSourceRepository.getPowerSourceById(1), nearestAsList);
	}

}
