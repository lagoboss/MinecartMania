package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartNearEntityEvent extends MinecartManiaEvent implements MinecartEvent{
	private static final long serialVersionUID = -565197673064396505L;
	private MinecartManiaMinecart minecart;	
	private Entity entity;
	private boolean action = false;
	private ItemStack drop = null;
	
	public MinecartNearEntityEvent(MinecartManiaMinecart cart, Entity entity) {
		super("MinecartNearEntityEvent");
		minecart = cart;
		this.entity = entity;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}

	public Entity getEntity() {
		return entity;
	}
	
	public ItemStack getDrop() {
		return drop;
	}
	
	public void setDrop(ItemStack drop) {
		this.drop = drop;
	}

	@Override
	public boolean isActionTaken() {
		return action;
	}

	@Override
	public void setActionTaken(boolean Action) {
		this.action = Action;
	}
}
