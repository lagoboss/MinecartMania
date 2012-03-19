package com.afforess.minecartmaniacore.signs;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SignTextUpdater implements Runnable{
	private Block sign;
	
	public SignTextUpdater(Block block) {
		sign = block;
	}

	public void run() {
		Sign sign = SignManager.getSignAt(this.sign);
		if (sign != null && sign instanceof MinecartManiaSign) {
			((MinecartManiaSign)sign).updated();
			Packet130UpdateSign update = new Packet130UpdateSign(sign.getX(), sign.getY(), sign.getZ(), sign.getLines());
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				((CraftPlayer)player).getHandle().netServerHandler.sendPacket(update);
			}
		}
		else {
			this.sign.getState().update(true);
		}
	}

}
