package com.server.entites;

import javax.persistence.Entity;

import org.springframework.context.annotation.Scope;

@Entity
@Scope(value="Singleton")
public class NormalPowerSource extends PowerSource {
	public NormalPowerSource() {
		setType("normalPowerSource");
		setId(2);
	}
}
