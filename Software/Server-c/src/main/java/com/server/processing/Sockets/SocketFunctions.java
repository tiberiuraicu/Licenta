package com.server.processing.Sockets;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.database.repositories.PowerSourceRepository;
import com.server.entites.Circuit;
import com.server.entites.PowerSource;
import com.server.socket.NotificationBroadcaster;

@Component
public class SocketFunctions {

	@Autowired
	NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	PowerSourceRepository powerSourceRepository;

	public double calculatePercentage(double obtained, double total) {

		return (double) Math.round((obtained * 100 / total)* 100) / 100;
	}

	public void sendNewPowerConsumptionNotification(PowerSource solarPowerSource, PowerSource normalPowerSource) {
		Double solarPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(solarPowerSource.getCircuits()) * 100) / 100;
		Double normalPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(normalPowerSource.getCircuits()) * 100) / 100;

		//Double solarPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(powerSourceRepository.getPowerSourceById(1).getCircuits()) * 100) / 100;
		//System.out.println(powerSourceRepository.getPowerSourceById(1).getCircuits());
		//Double normalPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(powerSourceRepository.getPowerSourceById(2).getCircuits()) * 100) / 100;
		//System.out.println(powerSourceRepository.getPowerSourceById(2).getCircuits());

		
		//System.out.println(solarPowerConsumed.toString() + ' '+ normalPowerConsumed.toString()+ ' '+solarPowerConsumed1.toString()+' '+normalPowerConsumed1.toString());
		notificationBroadcaster.sendOutletPower("New power consumption : Solar panel : " + solarPowerConsumed
				+ " kW -> " + calculatePercentage(solarPowerConsumed, solarPowerConsumed + normalPowerConsumed) + "%,"
				+ " Normal power source : " + normalPowerConsumed + " kW -> "
				+ calculatePercentage(normalPowerConsumed, solarPowerConsumed + normalPowerConsumed) + "%,",solarPowerSource.getDevice().getUsers().get(0).getId());
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
