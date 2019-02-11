package com.server.Server.entites;

import java.util.List;
import java.util.Vector;


public class User {

	private Integer id;

	
	private Rol rol;


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