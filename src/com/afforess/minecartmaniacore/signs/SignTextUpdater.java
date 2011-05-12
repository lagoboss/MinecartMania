package com.afforess.minecartmaniacore.signs;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.MinecartManiaCore;

public class SignTextUpdater implements Runnable{
	private Block sign;
	
	public SignTextUpdater(Block block) {
		sign = block;
	}

	@Override
	public void run() {
		Sign sign = SignManager.getSignAt(this.sign);
		if (sign != null && sign instanceof MinecartManiaSign) {
			((MinecartManiaSign)sign).updated();
			Packet130UpdateSign update = new Packet130UpdateSign(sign.getX(), sign.getY(), sign.getZ(), sign.getLines());
			for (Player player : MinecartManiaCore.server.getOnlinePlayers()) {
				((CraftPlayer)player).getHandle().netServerHandler.sendPacket(update);
			}
		}
		else {
			this.sign.getState().update(true);
		}
	}

}
