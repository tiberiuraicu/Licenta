package com.server.entites;

import javax.persistence.Entity;

@Entity
public class SolarPanel extends PowerSource {

	public SolarPanel() {
		super();
		setType("solarPanel");
	}
	

}
