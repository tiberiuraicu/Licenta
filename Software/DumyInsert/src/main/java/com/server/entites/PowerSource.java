package com.server.entites;

import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class PowerSource {

	@Id
	protected Integer id;

	@Override
	public String toString() {
		return "PowerSource [id=" + id + ", generatedPower=" + generatedPower + ", type=" + type + "]";
	}

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH,
			CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "powerSource")
	protected List<Circuit> circuits = new Vector<Circuit>();

	protected Double generatedPower;

	protected String type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	protected Device device;

	public PowerSource() {
	}

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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
