package com.afforess.minecartmaniacore.config;

import com.afforess.minecartmaniacore.Item;

public class ControlBlock {
	
	private Item type = null;
	private double multiplier = 1.0;
	private RedstoneState multiplierState = RedstoneState.Default;
	private boolean catcher = false;
	private RedstoneState catcherState = RedstoneState.Default;
	private double launchSpeed = 0D;
	private RedstoneState launcherState = RedstoneState.Default;
	private boolean ejector = false;
	private RedstoneState ejectorState = RedstoneState.Default;
	private boolean platform = false;
	private RedstoneState platformState = RedstoneState.Default;
	private boolean station = false;
	private RedstoneState stationState = RedstoneState.Default;
	private boolean spawnMinecart = false;
	private RedstoneState spawnState = RedstoneState.Default;
	private boolean killMinecart = false;
	private RedstoneState killState = RedstoneState.Default;
	
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
	
	protected void setMultiplierState(RedstoneState multiplierState) {
		this.multiplierState = multiplierState;
	}

	public RedstoneState getMultiplierState() {
		return multiplierState;
	}
	
	public boolean isCatcherBlock() {
		return catcher;
	}
	
	protected void setCatcherBlock(boolean val) {
		catcher = val;
	}
	
	public RedstoneState getCatcherState() {
		return catcherState;
	}
	
	protected void setCatcherState(RedstoneState state) {
		catcherState = state;
	}
	
	public double getLauncherSpeed() {
		return launchSpeed;
	}
	
	protected void setLauncherSpeed(double d) {
		launchSpeed = d;
	}
	
	protected void setLauncherState(RedstoneState launcherState) {
		this.launcherState = launcherState;
	}

	public RedstoneState getLauncherState() {
		return launcherState;
	}
	
	public boolean isEjectorBlock() {
		return ejector;
	}
	
	protected void setEjectorBlock(boolean val) {
		ejector = val;
	}
	
	protected void setEjectorState(RedstoneState ejectorState) {
		this.ejectorState = ejectorState;
	}

	public RedstoneState getEjectorState() {
		return ejectorState;
	}

	
	public boolean isPlatformBlock() {
		return platform;
	}
	
	protected void setPlatformBlock(boolean val) {
		platform = val;
	}
	
	protected void setPlatformState(RedstoneState platformState) {
		this.platformState = platformState;
	}

	public RedstoneState getPlatformState() {
		return platformState;
	}
	
	public boolean isStationBlock() {
		return station;
	}
	
	protected void setStationBlock(boolean val) {
		station = val;
	}
	
	protected void setStationState(RedstoneState stationState) {
		this.stationState = stationState;
	}

	public RedstoneState getStationState() {
		return stationState;
	}

	
	public boolean isSpawnMinecart() {
		return spawnMinecart;
	}
	
	protected void setSpawnMinecart(boolean val) {
		spawnMinecart = val;
	}
	
	protected void setSpawnState(RedstoneState spawnState) {
		this.spawnState = spawnState;
	}

	public RedstoneState getSpawnState() {
		return spawnState;
	}
	
	public boolean isKillMinecart() {
		return killMinecart;
	}
	
	protected void setKillMinecart(boolean val) {
		killMinecart = val;
	}
	
	protected void setKillState(RedstoneState killState) {
		this.killState = killState;
	}

	public RedstoneState getKillState() {
		return killState;
	}
	
	public String toString() {
		return "[" + getType() + ":" + getMultiplier() + ":" + isCatcherBlock() + ":" + getLauncherSpeed() + ":" + isEjectorBlock() + ":" + isPlatformBlock() + ":" + isStationBlock() + "]";
	}
}
