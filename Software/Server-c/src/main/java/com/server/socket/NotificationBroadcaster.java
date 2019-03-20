package com.server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationBroadcaster {

	@Autowired
	private SimpMessagingTemplate template;

	public void sendOutletPower(String notification)  {
	System.out.println("da");
		this.template.convertAndSend("/notification", notification);

	}
	
}