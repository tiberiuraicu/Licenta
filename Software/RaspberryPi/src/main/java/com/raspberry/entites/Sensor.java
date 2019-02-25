package com.raspberry.entites;

import java.util.Date;

public class Sensor {

	protected Integer id;

	protected String name;

	protected String type;
	
	protected int state;
	
	protected int triggered;

	protected Date timestamp;
	
	protected Double powerConsumed;

	protected String location;
	
	public Sensor() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTriggered() {
		return triggered;
	}

	public void setTriggered(int triggered) {
		this.triggered = triggered;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Double getPowerConsumed() {
		return powerConsumed;
	}

	public void setPowerConsumed(Double powerConsumed) {
		this.powerConsumed = powerConsumed;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
