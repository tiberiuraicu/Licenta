package com.server.Server.entites;

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
public class Dispozitiv {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private boolean trimiteNotificari;

	private String name;

	private String tara;

	private String localitatea;

	private String strada;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="dispozitiv")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Circuit> circuite = new Vector<Circuit>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="dispozitiv")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Senzor> senzori = new Vector<Senzor>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="dispozitiv")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Notificare> notificari = new Vector<Notificare>();

	@ManyToMany(mappedBy = "dispozitive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
