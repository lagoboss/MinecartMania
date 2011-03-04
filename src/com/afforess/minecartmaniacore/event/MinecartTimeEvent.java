package com.afforess.minecartmaniacore.event;

import java.util.Calendar;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;

	public class MinecartTimeEvent extends org.bukkit.event.Event {
	/**
		 * 
		 */
		private static final long serialVersionUID = 5065969344934650992L;
	private MinecartManiaMinecart minecart;	
	private Calendar oldCalendar;
	private Calendar currentCalendar;
	public MinecartTimeEvent(MinecartManiaMinecart cart, Calendar oldCal, Calendar newCal) {
		super("MinecartTimeEvent");
		minecart = cart;
		oldCalendar = oldCal;
		currentCalendar = newCal;
	}
	
	
	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public Calendar getOldCalendar() {
		return oldCalendar;
	}
	
	public Calendar getCurrentCalendar() {
		return currentCalendar;
	}
}
