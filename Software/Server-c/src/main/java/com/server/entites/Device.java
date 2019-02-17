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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Transactional
@Proxy(lazy = false)
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private boolean sendNotifications;

	private String name;

	private String country;

	private String locality;

	private String street;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="device")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Circuit> circuits = new Vector<Circuit>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="device")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Sensor> sensors = new Vector<Sensor>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="device")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Notification> notifications = new Vector<Notification>();

	@ManyToMany(mappedBy = "devices", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<User> users = new Vector<User>();

	public Device() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isSendNotifications() {
		return sendNotifications;
	}

	public void setSendNotifications(boolean sendNotifications) {
		this.sendNotifications = sendNotifications;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public List<Circuit> getCircuits() {
		return circuits;
	}

	public void setCircuits(List<Circuit> circuits) {
		this.circuits = circuits;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
