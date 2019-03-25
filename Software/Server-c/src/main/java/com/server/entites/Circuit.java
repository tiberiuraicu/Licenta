package com.server.entites;

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

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Circuit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.REFRESH,
			CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "circuit")
	@Fetch(value = FetchMode.SUBSELECT)
	private java.util.List<Consumer> consumers = new Vector<Consumer>();

	private double powerConsumed;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "device_id")
	private Device device;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH,
			CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "powerSource_id")
	private PowerSource powerSource;

	@OneToMany(cascade ={ CascadeType.DETACH, CascadeType.REFRESH,
			CascadeType.REMOVE }, fetch = FetchType.EAGER, mappedBy = "circuit")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Sensor> sensors = new Vector<Sensor>();

	public Circuit() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.List<Consumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(java.util.List<Consumer> consumers) {
		this.consumers = consumers;
	}

	public double getPowerConsumed() {
		return powerConsumed;
	}

	public void setPowerConsumed(double powerConsumed) {
		this.powerConsumed = powerConsumed;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public PowerSource getPowerSource() {
		return powerSource;
	}

	public void setPowerSource(PowerSource powerSource) {
		this.powerSource = powerSource;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	@Override
	public String toString() {
		return "Circuit [id=" + id + ", consumers=" + consumers + ", powerConsumed=" + powerConsumed + ", sensors="
				+ sensors + "]";
	}

	
	

	


}
