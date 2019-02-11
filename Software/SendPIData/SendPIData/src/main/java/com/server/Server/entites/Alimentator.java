package com.server.Server.entites;

import java.util.List;



public class Alimentator {


	protected Integer id;
	

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
