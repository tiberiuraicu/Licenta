package com.server.entites;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Consumer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	protected String type;

	protected Integer deviceId;

	protected double powerConsumed;
	
	protected String location;

	protected Date timestamp;

	protected int state;// aprins/inchis

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "circuit_id")
	protected Circuit circuit;
	
	protected String name;

	public Consumer(double powerConsumed, int state, String name) {
		super();
		this.powerConsumed = powerConsumed;
		this.state = state;
		this.name=name;
	}

	public Consumer() {
		// TODO Auto-generated constructor stub
	}

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

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public double getPowerConsumed() {
		return powerConsumed;
	}

	public void setPowerConsumed(double powerConsumed) {
		this.powerConsumed = powerConsumed;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Circuit getCircuit() {
		return circuit;
	}

	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Consumer [id=" + id + ", type=" + type + ", deviceId=" + deviceId + ", powerConsumed=" + powerConsumed
				+ ", location=" + location + ", timestamp=" + timestamp + ", state=" + state + ", name=" + name + "]";
	}
	
}
