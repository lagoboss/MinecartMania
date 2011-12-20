package com.afforess.minecartmaniacore.signs;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SignTextUpdater implements Runnable {
    private final Block sign;
    
    public SignTextUpdater(final Block block) {
        sign = block;
    }
    
    public void run() {
        final Sign sign = SignManager.getSignAt(this.sign);
        if ((sign != null) && (sign instanceof MinecartManiaSign)) {
            ((MinecartManiaSign) sign).updated();
            final Packet130UpdateSign update = new Packet130UpdateSign(sign.getX(), sign.getY(), sign.getZ(), sign.getLines());
            for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(update);
            }
        } else {
            this.sign.getState().update(true);
        }
    }
    
}
