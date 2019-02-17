package com.server.entites;

public class MovementSensor extends Sensor {

	private int groupId;

	public MovementSensor(int state, int groupId) {
		super();
		this.state = state;
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



}
