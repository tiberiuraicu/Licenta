package com.server.entites;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Role {
	
	public Role(boolean isAdmin, boolean isUser) {
		super();
		this.isAdmin = isAdmin;
		this.isUser = isUser;
	}
	public Role() {}


	@Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private Integer id;	
	 
	 @OneToOne(mappedBy = "role",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
