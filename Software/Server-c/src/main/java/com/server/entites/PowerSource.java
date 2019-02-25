package com.server.entites;

import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class PowerSource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy="powerSource")
	@Fetch(value = FetchMode.SUBSELECT)
	protected List<Circuit> circuits= new Vector<Circuit>();
	
	protected Double generatedPower;

	protected String type;
	
	public PowerSource() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Circuit> getCircuits() {
		return circuits;
	}

	public void setCircuits(List<Circuit> circuits) {
		this.circuits = circuits;
	}

	public Double getPutereGenerata() {
		return generatedPower;
	}

	public void setPutereGenerata(Double putereGenerata) {
		this.generatedPower = putereGenerata;
	}

	public Double getGeneratedPower() {
		return generatedPower;
	}

	public void setGeneratedPower(Double generatedPower) {
		this.generatedPower = generatedPower;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
