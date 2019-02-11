package com.server.Server.entites;

public class Miscare extends Senzor {

	private int groupId;

	public Miscare(int stare, int groupId) {
		super();
		this.stare = stare;
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "MiscareEvent [stare=" + stare + "]";
	}

}
