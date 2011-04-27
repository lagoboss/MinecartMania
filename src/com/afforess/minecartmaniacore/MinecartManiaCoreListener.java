package com.afforess.minecartmaniacore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import net.minecraft.server.EntityLiving;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartClickedEvent;
import com.afforess.minecartmaniacore.event.MinecartDirectionChangeEvent;
import com.afforess.minecartmaniacore.event.MinecartIntersectionEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStartEvent;
import com.afforess.minecartmaniacore.event.MinecartMotionStopEvent;
import com.afforess.minecartmaniacore.signs.LaunchPlayerAction;
import com.afforess.minecartmaniacore.signs.MinecartManiaSign;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.EntityUtils;
import com.afforess.minecartmaniacore.utils.MinecartUtils;
import com.afforess.minecartmaniacore.utils.SignUtils;

@SuppressWarnings("unused")
public class MinecartManiaCoreListener extends VehicleListener{
	private MinecartManiaCore core;
	
	public MinecartManiaCoreListener(MinecartManiaCore instance) {
		core = instance;
	}
	
	@Override
	public void onVehicleUpdate(VehicleUpdateEvent event) {
		if (event.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart)event.getVehicle();
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart(cart);
			
			if (minecart.isDead()) {
				return;
			}
			
			CraftServer server = (CraftServer)MinecartManiaCore.server;
			//server.getHandle().server.tracker.
			minecart.updateCalendar(); 
			if (minecart.isMoving()) {
				if (minecart.getDirectionOfMotion() != minecart.getPreviousDirectionOfMotion()) {
					MinecartManiaCore.server.getPluginManager().callEvent(new MinecartDirectionChangeEvent(minecart, minecart.getPreviousDirectionOfMotion(), minecart.getDirectionOfMotion()));
					minecart.setPreviousDirectionOfMotion(minecart.getDirectionOfMotion());
				}
			}
			
			//Fire new events
			if (minecart.wasMovingLastTick() && !minecart.isMoving()) {
				MinecartMotionStopEvent mmse = new MinecartMotionStopEvent(minecart);
				MinecartManiaCore.server.getPluginManager().callEvent(mmse);
				mmse.logProcessTime();
			}
			else if (!minecart.wasMovingLastTick() && minecart.isMoving()) {
				MinecartMotionStartEvent mmse = new MinecartMotionStartEvent(minecart);
				MinecartManiaCore.server.getPluginManager().callEvent(mmse);
				mmse.logProcessTime();
			}
			minecart.setWasMovingLastTick(minecart.isMoving());
			minecart.doRealisticFriction();
			minecart.doLauncherBlock();
			minecart.undoPoweredRails();
			if (minecart.isMoving()) {
				minecart.updateChunks();
			}
			
			//total hack workaround because of the inability to create runnables/threads w/o IllegalAccessError
			if (minecart.getDataValue("launch") != null) {
				minecart.launchCart();
				minecart.setDataValue("launch", null);
			}
			
			if (minecart.hasChangedPosition() || minecart.createdLastTick) {
				minecart.updateToPoweredRails();
				if (minecart.isAtIntersection()) {
					MinecartIntersectionEvent mie = new MinecartIntersectionEvent(minecart);
					MinecartManiaCore.server.getPluginManager().callEvent(mie);
					mie.logProcessTime();
				}
				
					MinecartActionEvent mae = new MinecartActionEvent(minecart);
				if (!minecart.createdLastTick) {
					MinecartManiaCore.server.getPluginManager().callEvent(mae);
					mae.logProcessTime();
				}
				
				minecart.doSpeedMultiplierBlock();
				minecart.doPlatformBlock();
				minecart.doCatcherBlock();
				minecart.doElevatorBlock();

				boolean action = mae.isActionTaken();
				if (!action) {
					action = minecart.doEjectorBlock();
				}
				MinecartUtils.updateNearbyItems(minecart);
				
				minecart.updateMotion();
				minecart.updateLocation();
				
				//should do last
				minecart.doKillBlock();
				minecart.createdLastTick = false;
			}
		}
	}
	
	@Override
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (event.getVehicle() instanceof Minecart && !event.isCancelled()) {
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
			minecart.kill(false);
		}
	}
	
	@Override
	public void onVehicleDamage(VehicleDamageEvent event) {
		if (event.getVehicle() instanceof Minecart) {
			MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
			if (!event.isCancelled()) {
				MinecartManiaLogger.getInstance().debug("Damage: " + event.getDamage() + " Existing: " + minecart.minecart.getDamage());
				if ((event.getDamage() * 10) + minecart.minecart.getDamage() > 40) {
					minecart.kill();
					event.setCancelled(true);
					event.setDamage(0);
				}
				if (minecart.minecart.getPassenger() != null) {
					if (minecart.isOnRails()) {
						if(event.getAttacker() != null && event.getAttacker().getEntityId() == minecart.minecart.getPassenger().getEntityId()) {
							MinecartClickedEvent mce = new MinecartClickedEvent(minecart);
							MinecartManiaCore.server.getPluginManager().callEvent(mce);
							if (mce.isActionTaken()) {
								event.setDamage(0);
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
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
			if (collisioner instanceof LivingEntity) {
				LivingEntity victim = (LivingEntity)(collisioner);
				if (!(victim instanceof Player) && !(victim instanceof Wolf)) {
					if (MinecartManiaWorld.isMinecartsKillMobs()) {
						if (minecart.isMoving()) {
							victim.remove();
							event.setCancelled(true);
							event.setCollisionCancelled(true);
							event.setPickupCancelled(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.isCancelled() || !(event.getVehicle() instanceof Minecart)) {
			return;
		}
		
		final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)event.getVehicle());
		if (minecart.minecart.getPassenger() != null) {
			return;
		}
		if (ControlBlockList.isCatcherBlock(minecart.getItemBeneath())) {
			if (!minecart.isMoving()) {
				ArrayList<Sign> signs = SignUtils.getAdjacentSignList(minecart, 2);
				for (Sign s : signs) {
					com.afforess.minecartmaniacore.signs.Sign sign = SignManager.getSignAt(s.getBlock().getLocation());
					if (sign.executeAction(minecart, LaunchPlayerAction.class)) {
						break;
					}
				}
			}
		}
	}


}
