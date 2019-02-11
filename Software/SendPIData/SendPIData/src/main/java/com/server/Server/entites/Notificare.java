package com.server.Server.entites;

import java.util.Date;


public class Notificare {

	private Integer id;


	private Dispozitiv dispozitiv;

	private String corp;

	private Date dataOra;

	private String titlu;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Dispozitiv getDispozitiv() {
		return dispozitiv;
	}

	public void setDispozitiv(Dispozitiv dispozitiv) {
		this.dispozitiv = dispozitiv;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public String getTitlu() {
		return titlu;
	}

	public void setTitlu(String titlu) {
		this.titlu = titlu;
	}

}
