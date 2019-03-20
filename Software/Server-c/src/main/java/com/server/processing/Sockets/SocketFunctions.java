package com.server.processing.Sockets;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.entites.Circuit;
import com.server.socket.NotificationBroadcaster;

@Component
public class SocketFunctions {

	@Autowired
	NotificationBroadcaster notificationBroadcaster;

	public double calculatePercentage(double obtained, double total) {

		return (double) Math.round((obtained * 100 / total)* 100) / 100;
	}

	public void sendNewPowerConsumptionNotification(List<Circuit> solarCircuits, List<Circuit> normalCircuits) {
		Double solarPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(solarCircuits) * 100) / 100;
		Double normalPowerConsumed = (double) Math.round(calculateConsumedPowerForPowerSource(normalCircuits) * 100) / 100;

		notificationBroadcaster.sendOutletPower("New power consumption : Solar panel : " + solarPowerConsumed
				+ " kW -> " + calculatePercentage(solarPowerConsumed, solarPowerConsumed + normalPowerConsumed) + "%,"
				+ " Normal power source : " + normalPowerConsumed + " kW -> "
				+ calculatePercentage(normalPowerConsumed, solarPowerConsumed + normalPowerConsumed) + "%,");
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
