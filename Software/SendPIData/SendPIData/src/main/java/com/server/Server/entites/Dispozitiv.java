package com.server.Server.entites;

import java.util.List;
import java.util.Vector;


import org.springframework.transaction.annotation.Transactional;


public class Dispozitiv {


	private Integer id;

	private boolean trimiteNotificari;

	private String name;

	private String tara;

	private String localitatea;

	private String strada;

	private List<Circuit> circuite = new Vector<Circuit>();
	

	private List<Senzor> senzori = new Vector<Senzor>();


	private List<Notificare> notificari = new Vector<Notificare>();

	private List<User> useri = new Vector<User>();

	public Dispozitiv() {

	}

	public Dispozitiv(boolean trimiteNotificari, String tara, String localitatea, String strada) {
		super();
		this.trimiteNotificari = trimiteNotificari;
		this.tara = tara;
		this.localitatea = localitatea;
		this.strada = strada;
	}

	public boolean isTrimiteNotificari() {
		return trimiteNotificari;
	}

	public void setTrimiteNotificari(boolean trimiteNotificari) {
		this.trimiteNotificari = trimiteNotificari;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTara() {
		return tara;
	}

	public void setTara(String tara) {
		this.tara = tara;
	}

	public String getLocalitatea() {
		return localitatea;
	}

	public void setLocalitatea(String localitatea) {
		this.localitatea = localitatea;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public List<User> getUseri() {
		return useri;
	}

	public void setUseri(List<User> user) {
		this.useri = user;
	}

	public List<Senzor> getSenzori() {
		return senzori;
	}

	public void setSenzori(List<Senzor> senzori) {
		this.senzori = senzori;
	}

	public List<Notificare> getNotificari() {
		return notificari;
	}

	public void setNotificari(List<Notificare> notificari) {
		this.notificari = notificari;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Dispozitiv [useri=");
		for(User user : this.useri)
		{
			sb.append(user.toString()).append(";");
		}
		sb.append("]");
		return  sb.toString();
	}

	

	

}
