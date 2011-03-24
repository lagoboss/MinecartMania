package com.afforess.minecartmaniacore.config;

import com.afforess.minecartmaniacore.Item;

public class ControlBlock {
	
	private Item type = null;
	private double multiplier = 1.0;
	private boolean catcher = false;
	private double launchSpeed = 0D;
	private boolean ejector = false;
	private boolean platform = false;
	private boolean station = false;
	private boolean redstoneDisable = false;
	private boolean requiresRedstone = false;
	private boolean spawnMinecart = false;
	private boolean killMinecart = false;
	
	public ControlBlock() {
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
	
	public double getLauncherSpeed() {
		return launchSpeed;
	}
	
	protected void setLauncherSpeed(double d) {
		launchSpeed = d;
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
	
	public boolean isRedstoneDisables() {
		return redstoneDisable;
	}
	
	protected void setRedstoneDisables(boolean val) {
		redstoneDisable = val;
	}
	
	public boolean isReqRedstone() {
		return requiresRedstone;
	}
	
	protected void setReqRedstone(boolean val) {
		requiresRedstone = val;
	}
	
	public boolean isSpawnMinecart() {
		return spawnMinecart;
	}
	
	protected void setSpawnMinecart(boolean val) {
		spawnMinecart = val;
	}
	
	public boolean isKillMinecart() {
		return killMinecart;
	}
	
	protected void setKillMinecart(boolean val) {
		killMinecart = val;
	}
	
	public String toString() {
		return "[" + getType() + ":" + getMultiplier() + ":" + isCatcherBlock() + ":" + getLauncherSpeed() + ":" + isEjectorBlock() + ":" + isPlatformBlock() + ":" + isStationBlock() + "]";
	}
}
