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
import com.server.processing.REST.HomePageFunctions;
import com.server.processing.REST.AuthentificationFunctions;

@Component
public class DataBroadcaster {
	@Autowired
	AuthentificationFunctions restFunctions;

	@Autowired
	PowerSourceRepository powerSourceRepository;
	
	@Autowired
	HomePageFunctions homePageFunctions;
	
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
					
					template.convertAndSend("/totalPowerConsumed/" + id, homePageFunctions.getTotalPowerConsumed());
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
							homePageFunctions.getLastRegistratedPowerConsumedForEveryOutlet());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		timer.schedule(timerTaskOutletPowerConsumed, 1000, 1000);
	}
}