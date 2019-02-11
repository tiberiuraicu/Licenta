package com.server.Server.entites;

import java.util.Date;



public class Senzor {


	protected Integer id;

	protected String nume;

	protected int stare;

	protected Date dataOra;

	protected String locatia;

	protected String circuit;


	private Dispozitiv dispozitiv;
	
	public Senzor() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public int getStare() {
		return stare;
	}

	public void setStare(int stare) {
		this.stare = stare;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public String getLocatia() {
		return locatia;
	}

	public void setLocatia(String locatia) {
		this.locatia = locatia;
	}

	public String getCircuit() {
		return circuit;
	}

	public void setCircuit(String circuit) {
		this.circuit = circuit;
	}
	
	public Dispozitiv getDispozitiv() {
		return dispozitiv;
	}

	public void setDispozitiv(Dispozitiv dispozitiv) {
		this.dispozitiv = dispozitiv;
	}

	@Override
	public String toString() {
		return "Senzor [nume=" + nume + ", stare=" + stare + ", dataOra=" + dataOra + ", locatia=" + locatia
				+ ", circuit=" + circuit + "]";
	}

}
