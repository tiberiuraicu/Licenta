package com.server.socket;

import java.io.IOException;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.server.processing.REST.RestFunctions;

@Component
public class DataBroadcaster {
	@Autowired
	RestFunctions restFunctions;

	@Autowired
	private SimpMessagingTemplate template;

	@Scheduled(fixedDelay = 1000)
	public void sendTotalPowerConsumed() throws MessagingException, ServletException, IOException {
		this.template.convertAndSend("/totalPowerConsumed", restFunctions.getTotalPowerConsumed());
	}

	@Scheduled(fixedDelay = 1000)
	public void sendOutletPower() throws MessagingException, ServletException, IOException {
			this.template.convertAndSend("/outletPowerConsumed",
					restFunctions.getLastRegistratedPowerConsumedForEveryOutlet());
		
	}
}