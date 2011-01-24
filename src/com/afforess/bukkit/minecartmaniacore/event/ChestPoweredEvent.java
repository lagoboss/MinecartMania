package com.afforess.bukkit.minecartmaniacore.event;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaChest;
public class ChestPoweredEvent  extends org.bukkit.event.Event{
	
	private MinecartManiaChest chest;
	private boolean powered;
	private boolean wasPowered;
	private boolean action = false;
	
	public ChestPoweredEvent(MinecartManiaChest chest, boolean powered) {
		super("ChestPoweredEvent");
		this.chest = chest;
		this.powered = powered;
		this.wasPowered = chest.isRedstonePower();
	}
	
	public MinecartManiaChest getChest(){
		return chest;
	}
	
	public boolean isPowered() {
		return powered;
	}
	
	public boolean wasPowered() {
		return wasPowered;
	}

	public boolean isActionTaken() {
		return action;
	}
	
	public void setActionTaken(boolean b) {
		action = b;
	}
}
