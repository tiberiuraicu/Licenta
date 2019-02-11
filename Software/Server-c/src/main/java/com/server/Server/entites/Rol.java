package com.server.Server.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Rol {
	
	public Rol(boolean isAdmin, boolean isUser) {
		super();
		this.isAdmin = isAdmin;
		this.isUser = isUser;
	}
	public Rol() {
	
	}


	@Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private Integer id;	
	 
	 @OneToOne(mappedBy = "rol")
	 private User user;
	 
	 private boolean isAdmin;
	 
	 private boolean isUser;

   
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	 @Override
	public String toString() {
		return "Rol [ isAdmin=" + isAdmin + ", isUser=" + isUser + "]";
	}

	 
	 
}
