package com.raspberry.entites;


public class Instruction {

	private String type;
	
	private String deviceName;
	
	private String onOffValue;
	
	private String powerSource;

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOnOffValue() {
		return onOffValue;
	}

	public void setOnOffValue(String onOffValue) {
		this.onOffValue = onOffValue;
	}

	public String getPowerSource() {
		return powerSource;
	}

	public void setPowerSource(String powerSource) {
		this.powerSource = powerSource;
	}
	
}
