package com.afforess.minecartmaniacore.event;

import org.bukkit.event.Cancellable;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

public class MinecartKillEvent extends MinecartManiaEvent implements Cancellable{

	private static final long serialVersionUID = -5782963564694931261L;
	private boolean cancelled = false;
	private MinecartManiaMinecart minecart;
	protected MinecartKillEvent(MinecartManiaMinecart minecart) {
		super("MinecartKillEvent");
		this.minecart = minecart;
	}
	
	public MinecartManiaMinecart getMinecart() {
		return this.minecart;
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
