package com.server.socket;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.server.database.repositories.PowerSourceRepository;
import com.server.processing.REST.RestFunctions;

@Component
public class DataBroadcaster {
	@Autowired
	RestFunctions restFunctions;

	@Autowired
	PowerSourceRepository powerSourceRepository;
	
	@Autowired
	private SimpMessagingTemplate template;

	Timer timer = new Timer();

	TimerTask timerTaskPowerConsumed;

	TimerTask timerTaskOutletPowerConsumed;

	public void sendTotalPowerConsumed(String id) {
		
		try {
			timerTaskPowerConsumed.cancel();
		} catch (Exception e) {
		}
		timerTaskPowerConsumed = new TimerTask() {
			@Override
			public void run() {
				try {
					
					template.convertAndSend("/totalPowerConsumed/" + id, restFunctions.getTotalPowerConsumed());
				} catch (MessagingException | ServletException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		};

		timer.schedule(timerTaskPowerConsumed, 1000, 1000);

	}

	public void sendOutletPower(String id) {
		try {
			timerTaskOutletPowerConsumed.cancel();
		} catch (Exception e) {
		}
		timerTaskOutletPowerConsumed = new TimerTask() {

			@Override
			public void run() {
				try {

					template.convertAndSend("/outletPowerConsumed/" + id,
							restFunctions.getLastRegistratedPowerConsumedForEveryOutlet());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		timer.schedule(timerTaskOutletPowerConsumed, 1000, 1000);
	}
}