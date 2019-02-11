package com.server.Server.entites;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Alimentator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy="alimentator")
	@Fetch(value = FetchMode.SUBSELECT)
	protected List<Circuit> circuite;
	
	protected Double putereGenerata;

	public Alimentator() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Circuit> getCircuite() {
		return circuite;
	}

	public void setCircuite(List<Circuit> circuite) {
		this.circuite = circuite;
	}

	public Double getPutereGenerata() {
		return putereGenerata;
	}

	public void setPutereGenerata(Double putereGenerata) {
		this.putereGenerata = putereGenerata;
	}

	@Override
	public String toString() {
		return "Alimentator [circuite=" + circuite + "]";
	}

}
