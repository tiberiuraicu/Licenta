package com.server.Server.entites;

import java.util.List;
import java.util.Vector;




public class Circuit {


	protected Integer id;
	
	private java.util.List<Consumator> consumatori = new Vector<Consumator>();

	private double putereConsumata;
	

	private Dispozitiv dispozitiv;
	

	private Alimentator alimentator;

	public Circuit() {
		super();
		this.consumatori = consumatori;
		this.putereConsumata = putereConsumata;
		this.alimentator = alimentator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Alimentator getAlimentator() {
		return alimentator;
	}

	public void setAlimentator(Alimentator alimentator) {
		this.alimentator = alimentator;
	}

	public java.util.List<Consumator> getConsumatori() {
		return consumatori;
	}

	public void setConsumatori(java.util.List<Consumator> consumatori) {
		this.consumatori = consumatori;
	}

	public double getPutereConsumata() {
		return putereConsumata;
	}

	public void setPutereConsumata(double putereConsumata) {
		this.putereConsumata = putereConsumata;
	}

	@Override
	public String toString() {
		return "Circuit [id=" + id + ", putereConsumata=" + putereConsumata + ", dispozitiv=" + dispozitiv
				+ ", alimentator="  + "]";
	}

}
