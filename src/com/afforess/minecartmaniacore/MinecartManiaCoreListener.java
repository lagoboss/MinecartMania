package com.afforess.minecartmaniacore;

import java.util.Calendar;
import java.util.List;

import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import net.minecraft.server.EntityLiving;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartIntersectionEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStartEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.utils.MinecartUtils;

@SuppressWarnings("unused")
public class MinecartManiaCoreListener extends VehicleListener{
	private MinecartManiaCore core;
	
	public MinecartManiaCoreListener(MinecartManiaCore instance) {
		core = instance;
	}
	
	 public void onVehicleUpdate(VehicleEvent event) {
		if (event.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart)event.getVehicle();
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart(cart);
			
			minecart.updateCalendar(); 
			if (minecart.isMoving()) {
				minecart.setPreviousFacingDir(minecart.getDirectionOfMotion());
			}
			
			//Fire new events
			if (minecart.wasMovingLastTick() && !minecart.isMoving()) {
				MinecartMotionStopEvent mmse = new MinecartMotionStopEvent(minecart);
				MinecartManiaCore.server.getPluginManager().callEvent(mmse);
			}
			else if (!minecart.wasMovingLastTick() && minecart.isMoving()) {
				MinecartMotionStartEvent mmse = new MinecartMotionStartEvent(minecart);
				MinecartManiaCore.server.getPluginManager().callEvent(mmse);
			}
			minecart.setWasMovingLastTick(minecart.isMoving());
			minecart.doRealisticFriction();
			minecart.doLauncherBlock();
			minecart.doPressurePlateRails();
			minecart.updateChunks();
			
			if (minecart.hasChangedPosition()) {
				
				if (minecart.isAtIntersection()) {
					MinecartIntersectionEvent mie = new MinecartIntersectionEvent(minecart);
					MinecartManiaCore.server.getPluginManager().callEvent(mie);
				}
				
				MinecartActionEvent mae = new MinecartActionEvent(minecart);
				MinecartManiaCore.server.getPluginManager().callEvent(mae);
				
				boolean action = mae.isActionTaken();
		    	if (!action) {
		    		action = minecart.doHighSpeedBooster();
		    	}
		    	if (!action) {
		    		action = minecart.doLowSpeedBooster();
		    	}
		    	if (!action) {
		    		action = minecart.doHighSpeedBrake();
		    	}
		    	if (!action) {
		    		action = minecart.doLowSpeedBrake();
		    	}
		    	if (!action) {
		    		action = minecart.doReverse();
		    	}
		    	if (!action) {
		    		action = minecart.doCatcherBlock();
		    	}
		    	if (!action) {
		    		action = minecart.doEjectorBlock();
		    	}
		    	MinecartUtils.updateNearbyItems(minecart);
		    	
				minecart.updateMotion();
				minecart.updateLocation();
			}
		}
    }
	
	public void onVehicleDamage(VehicleDamageEvent event) {
		if (event.getVehicle() instanceof Minecart) {
    		MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
    		if (!event.isCancelled()) {
	    		if ((event.getDamage() * 10) + minecart.minecart.getDamage() > 40) {
	    			event.setDamage(0);
	    			event.setCancelled(true);
	    			minecart.kill();
	    		}
    		}
		}
    }
	
	public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
    	if (event.getVehicle() instanceof Minecart) {
    		Minecart cart = (Minecart)event.getVehicle();
    		MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart(cart);
			Entity collisioner = event.getEntity();
			
			if (minecart.doCatcherBlock()) {
				event.setCancelled(true);
				event.setCollisionCancelled(true);
				event.setPickupCancelled(true);
				return;
			}
			
			//TODO possible?
			/*if (collisioner instanceof Minecart) {
				event.setCancelled(true);
				event.setCollisionCancelled(true);
				minecart.minecart.setVelocity(minecart.getPreviousMotion());
				MinecartManiaMinecart collisionerMinecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)collisioner);
				collisionerMinecart.minecart.setVelocity(collisionerMinecart.getPreviousMotion());
			}*/

			if (collisioner instanceof LivingEntity) {
				LivingEntity victim = (LivingEntity)(collisioner);
				if (!(victim instanceof Player)) {
					if (MinecartManiaWorld.isMinecartsKillMobs()) {
						if (minecart.isMoving()) {
							
							try {
								CraftLivingEntity e = (CraftLivingEntity)victim;
								EntityLiving el = e.getHandle();
								el.C();
							}
							catch (Exception e) {
								victim.setHealth(0);
							}
							
							event.setCancelled(true);
							event.setCollisionCancelled(true);
							event.setPickupCancelled(true);
						}
					}
				}
			}
    	}
    }

}
