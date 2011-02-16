package com.afforess.minecartmaniacore.event;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartNearItemDropEvent extends org.bukkit.event.Event implements Cancellable{
	private MinecartManiaMinecart minecart;	
	private Item item;
	private boolean cancelled = false;
	
	public MinecartNearItemDropEvent(MinecartManiaMinecart cart, Item item) {
		super("MinecartNearItemDropEvent");
		minecart = cart;
		this.item = item;
	}

	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
