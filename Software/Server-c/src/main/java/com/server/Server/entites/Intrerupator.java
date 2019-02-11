package com.server.Server.entites;

import javax.persistence.Entity;

@Entity
public class Intrerupator extends Consumator {

	public Intrerupator() {
		
		setTip("intrerupator");
	}
	
	@Override
	public String toString() {
		return "IntrerupatorEvent [stare=" + stare + "]";
	}

}
