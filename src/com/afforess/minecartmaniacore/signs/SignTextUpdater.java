package com.afforess.minecartmaniacore.signs;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.MinecartManiaCore;

public class SignTextUpdater implements Runnable{
	private Location sign;
	
	public SignTextUpdater(Location location) {
		sign = location;
	}

	@Override
	public void run() {
		Sign sign = SignManager.getSignAt(this.sign);
		if (sign != null && sign instanceof MinecartManiaSign) {
			((MinecartManiaSign)sign).updated();
			Packet130UpdateSign update = new Packet130UpdateSign(sign.getLocation().getBlockX(), sign.getLocation().getBlockY(), sign.getLocation().getBlockZ(), sign.getLines());
			for (Player player : MinecartManiaCore.server.getOnlinePlayers()) {
				((CraftPlayer)player).getHandle().netServerHandler.sendPacket(update);
			}
		}
		else {
			this.sign.getBlock().getState().update(true);
		}
	}

}
