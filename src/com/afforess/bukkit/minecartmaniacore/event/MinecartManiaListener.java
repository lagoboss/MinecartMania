package com.afforess.bukkit.minecartmaniacore.event;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;


public class MinecartManiaListener implements CustomEventListener, Listener{

	public MinecartManiaListener() {
		
	}
	

	/**
     * Called when a minecart attempts to perform any action. Only occurs after a minecart changes position
     * 
     * @param event
     */
	public void onMinecartActionEvent(MinecartActionEvent event) {
		
	}
	
	/**
     * Called when the internal clock of a minecart is updated (each second)
     * 
     * @param event
     */
	public void onMinecartTimeEvent(MinecartTimeEvent event) {
		
	}
	
	/**
     * Called when a minecart that was not moving last tick began moving this tick
     * 
     * @param event
     */
	public void onMinecartMotionStartEvent(MinecartMotionStartEvent event) {
		
	}
	
	/**
     * Called when a minecart that was moving last tick stopped moving this tick
     * 
     * @param event
     */
	public void onMinecartMotionStopEvent(MinecartMotionStopEvent event) {
		
	}
	
	/**
     * Called when a minecart reaches an intersection
     * An intersection is considered any point where there is at least 3 minecart tracks intersecting, one forward, one backward, one perpendicular.
     * 
     * @param event
     */
	public void onMinecartIntersectionEvent(MinecartIntersectionEvent event) {
		
	}
	
	/**
     * Called when the redstone power state adjacent to any chest changes.
     * 
     * @param event
     */
	public void onChestPoweredEvent(ChestPoweredEvent event) {
		
	}
	
	/**
     * Avoid using.
     * 
     * @param event
     */
	public void onCustomEvent(Event event) {
		if (event instanceof MinecartActionEvent) {
			onMinecartActionEvent((MinecartActionEvent)event);
		}
		else if (event instanceof MinecartTimeEvent) {
			onMinecartTimeEvent((MinecartTimeEvent)event);
		}
		else if (event instanceof MinecartIntersectionEvent) {
			onMinecartIntersectionEvent((MinecartIntersectionEvent)event);
		}
		else if (event instanceof MinecartMotionStartEvent) {
			onMinecartMotionStartEvent((MinecartMotionStartEvent)event);
		}
		else if (event instanceof MinecartMotionStopEvent) {
			onMinecartMotionStopEvent((MinecartMotionStopEvent)event);
		}
		else if (event instanceof ChestPoweredEvent) {
			onChestPoweredEvent((ChestPoweredEvent)event);
		}
	}

}
