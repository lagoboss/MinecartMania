package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartNearEntityEvent extends org.bukkit.event.Event implements Cancellable{
	private MinecartManiaMinecart minecart;	
	private Entity entity;
	private boolean cancelled = false;
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

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	public ItemStack getDrop() {
		return drop;
	}
	
	public void setDrop(ItemStack drop) {
		this.drop = drop;
	}
}
