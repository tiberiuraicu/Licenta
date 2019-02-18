package com.raspberry.entites;

import java.util.Date;

public class Consumer {

	protected Integer id;

	protected String type;

	protected double powerConsumed;

	protected Date timestamp;

	protected int state;// aprins/inchis

	protected String name;

	public Consumer(double powerConsumed, int state, String name) {
		super();
		this.powerConsumed = powerConsumed;
		this.state = state;
		this.name = name;
	}

	public Consumer() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPowerConsumed() {
		return powerConsumed;
	}

	public void setPowerConsumed(double powerConsumed) {
		this.powerConsumed = powerConsumed;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
