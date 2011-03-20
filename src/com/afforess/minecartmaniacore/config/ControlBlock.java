package com.afforess.minecartmaniacore.config;

import com.afforess.minecartmaniacore.Item;

public class ControlBlock {
	
	private Item type;
	private double multiplier;
	private boolean catcher;
	private boolean launcher;
	private boolean ejector; 
	private boolean platform;
	private boolean station;
	
	public ControlBlock() {
		this.type = null;
		this.multiplier = 1.0;
		this.catcher = false;
		this.launcher = false;
		this.ejector = false;
		this.platform = false;
		this.station = false;
	}
	
	public ControlBlock(Item type, double multiplier, boolean catcher, boolean launcher, boolean ejector, boolean platform, boolean station) {
		this.type = type;
		this.multiplier = multiplier;
		this.catcher = catcher;
		this.launcher = launcher;
		this.ejector = ejector;
		this.platform = platform;
		this.station = station;
	}
	
	public Item getType() {
		return type;
	}
	
	protected void setType(Item type) {
		this.type = type;
	}
	
	public double getMultiplier() {
		return multiplier;
	}
	
	protected void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
	
	public boolean isCatcherBlock() {
		return catcher;
	}
	
	protected void setCatcherBlock(boolean val) {
		catcher = val;
	}
	
	public boolean isLauncherBlock() {
		return launcher;
	}
	
	protected void setLauncherBlock(boolean val) {
		launcher = val;
	}
	
	public boolean isEjectorBlock() {
		return ejector;
	}
	
	protected void setEjectorBlock(boolean val) {
		ejector = val;
	}
	
	public boolean isPlatformBlock() {
		return platform;
	}
	
	protected void setPlatformBlock(boolean val) {
		platform = val;
	}
	
	public boolean isStationBlock() {
		return station;
	}
	
	protected void setStationBlock(boolean val) {
		station = val;
	}
	
	public String toString() {
		return "[" + getType() + ":" + getMultiplier() + ":" + isCatcherBlock() + ":" + isLauncherBlock() + ":" + isEjectorBlock() + ":" + isPlatformBlock() + ":" + isStationBlock() + "]";
	}
}
