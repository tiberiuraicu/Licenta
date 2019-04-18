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

	public String sendTotalPowerConsumed(String id) throws ServletException, IOException {

		return homePageFunctions.getTotalPowerConsumed();
	}

	public String sendOutletPower(String id) {


		return homePageFunctions.getLastRegistratedPowerConsumedForEveryOutlet();
	}
}