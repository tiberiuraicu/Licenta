package com.server.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Scenario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	protected String sensorName;
	
	protected String switchName;
	
	protected double sensorRegisterTime;
	
	protected double switchRegisterTime;

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getSwitchName() {
		return switchName;
	}

	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}

	public double getSensorRegisterTime() {
		return sensorRegisterTime;
	}

	public void setSensorRegisterTime(double sensorRegisterTime) {
		this.sensorRegisterTime = sensorRegisterTime;
	}

	public double getSwitchRegisterTime() {
		return switchRegisterTime;
	}

	public void setSwitchRegisterTime(double switchRegisterTime) {
		this.switchRegisterTime = switchRegisterTime;
	}
	
	
}
