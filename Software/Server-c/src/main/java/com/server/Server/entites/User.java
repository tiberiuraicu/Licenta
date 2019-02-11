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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Proxy;
import org.springframework.transaction.annotation.Transactional;

@Entity // This tells Hibernate to make a table out of this class
@Transactional
@Proxy(lazy = false)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "rol_id")
	private Rol rol;

	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@JoinColumn(name = "dispozitiv_id")
	private List<Dispozitiv> dispozitive = new Vector<Dispozitiv>();

	private String nume;

	private String prenume;

	private Integer numarDeTelefon;

	private String tara;

	private String localitatea;

	private String strada;

	private String email;

	public User() {
	}

	public User(Rol rol, String nume, List<Dispozitiv> dispozitive) {
		super();
		this.rol = rol;
		this.nume = nume;
		this.dispozitive=dispozitive;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	

	public List<Dispozitiv> getDispozitive() {
		return dispozitive;
	}

	public void setDispozitive(List<Dispozitiv> dispozitive) {
		this.dispozitive = dispozitive;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public Integer getNumarDeTelefon() {
		return numarDeTelefon;
	}

	public void setNumarDeTelefon(Integer numarDeTelefon) {
		this.numarDeTelefon = numarDeTelefon;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", rol=" + rol + ", nume=" + nume + ", prenume="
				+ prenume + ", numarDeTelefon=" + numarDeTelefon + ", tara=" + tara + ", localitatea=" + localitatea
				+ ", strada=" + strada + ", email=" + email + "]";
	


	}

}