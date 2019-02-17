package com.server.entites;

import javax.persistence.Entity;

@Entity
public class Outlet extends Consumer  {
	
	public Outlet() { setType("outlet"); }
}
