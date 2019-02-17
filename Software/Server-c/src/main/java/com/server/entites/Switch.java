package com.server.entites;

import javax.persistence.Entity;

@Entity
public class Switch extends Consumer {

	public Switch() {
		
		setType("switch");
	}
}
