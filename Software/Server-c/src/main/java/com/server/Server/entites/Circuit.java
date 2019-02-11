package com.server.Server.entites;

import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Circuit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy="circuit")
	@Fetch(value = FetchMode.SUBSELECT)
	private java.util.List<Consumator> consumatori = new Vector<Consumator>();

	private double putereConsumata;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "dispozitiv_id")
	private Dispozitiv dispozitiv;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "alimentator_id")
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
