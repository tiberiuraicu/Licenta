package com.server.Server.entites;

import javax.persistence.Entity;

@Entity
public class Priza extends Consumator  {
	
		
	public Priza() {

		setTip("priza");
	}

	@Override
	public String toString() {
		return "Priza [id=" + id + ", tip=" + tip + ", idDispozitiv=" + idDispozitiv + ", putereConsumata="
				+ putereConsumata + ", dataOra=" + dataOra + ", stare=" + stare + ", circuit=" + circuit + ", nume="
				+ nume + "]";
	}

		
		

	
}
