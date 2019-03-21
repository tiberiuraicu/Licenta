package com.server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationBroadcaster {

	@Autowired
	private SimpMessagingTemplate template;

	public void sendOutletPower(String notification, int id)  {
		this.template.convertAndSend("/notification/"+id, notification);

	}
	
}