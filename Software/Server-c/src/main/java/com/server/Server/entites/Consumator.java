package com.server.Server.entites;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Consumator {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	protected String tip;

	protected Integer idDispozitiv;

	protected double putereConsumata;

	protected Date dataOra;

	protected int stare;// aprins/inchis

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "circuit_id")
	protected Circuit circuit;
	
	protected String nume;

	public Consumator(double putereConsumata, int stare, String nume) {
		super();
		this.putereConsumata = putereConsumata;
		this.stare = stare;
		this.nume=nume;
	}

	public Consumator() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getIdDispozitiv() {
		return idDispozitiv;
	}

	public void setIdDispozitiv(Integer idDispozitiv) {
		this.idDispozitiv = idDispozitiv;
	}

	public double getPutereConsumata() {
		return putereConsumata;
	}

	public void setPutereConsumata(double putereConsumata) {
		this.putereConsumata = putereConsumata;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public int getStare() {
		return stare;
	}

	public void setStare(int stare) {
		this.stare = stare;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return "Consumator [id=" + id + ", tip=" + tip + ", idDispozitiv=" + idDispozitiv + ", putereConsumata="
				+ putereConsumata + ", dataOra=" + dataOra + ", stare=" + stare + ", circuit=" + circuit + "]";
	}

}
