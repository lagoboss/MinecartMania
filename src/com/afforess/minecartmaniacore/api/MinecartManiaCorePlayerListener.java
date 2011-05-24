package com.afforess.minecartmaniacore.api;

import org.bukkit.entity.Minecart;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.afforess.minecartmaniacore.config.MinecartManiaConfiguration;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecartDataTable;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class MinecartManiaCorePlayerListener extends PlayerListener{
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (MinecartManiaConfiguration.isDisappearOnDisconnect()) {
			MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(event.getPlayer());
			if (event.getPlayer().getVehicle() instanceof Minecart) {
				final MinecartManiaMinecart minecart = MinecartManiaWorld.getMinecartManiaMinecart((Minecart)player.getPlayer().getVehicle());
				try {
					MinecartManiaMinecartDataTable data = new MinecartManiaMinecartDataTable(minecart, player.getName());
					MinecartManiaMinecartDataTable.save(data);
					minecart.kill(false);
				}
				catch (Exception e) {
					MinecartManiaLogger.getInstance().log("Failed to remove the minecart when " + player.getName() + " disconnected");
					MinecartManiaLogger.getInstance().log(e.getMessage(), false);
				}
			}
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (MinecartManiaConfiguration.isDisappearOnDisconnect()) {
			MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(event.getPlayer());
			MinecartManiaMinecartDataTable data = MinecartManiaMinecartDataTable.getDataTable(player.getName());
			if (data != null) {
				MinecartManiaMinecart minecart = data.toMinecartManiaMinecart();
				minecart.minecart.setPassenger(player.getPlayer());
				MinecartManiaMinecartDataTable.delete(data);
			}
		}
	}

}
