package com.server.entites;

import javax.persistence.Entity;

@Entity
public class NormalPowerSource extends PowerSource {
	public NormalPowerSource() {
		setType("normalPowerSource");
		setId(2);
	}
}
