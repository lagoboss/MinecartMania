package com.afforess.minecartmaniacore;

import java.util.ArrayList;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class MinecartManiaCoreWorldListener extends WorldListener{
    public void onChunkUnloaded(ChunkUnloadEvent event) {
    	if (!event.isCancelled()) {
    		if (MinecartManiaWorld.isKeepMinecartsLoaded()) {
    			ArrayList<MinecartManiaMinecart> minecarts = MinecartManiaWorld.getMinecartManiaMinecartList();
    			for (MinecartManiaMinecart minecart : minecarts) {
    				if (Math.abs(event.getChunk().getX() - minecart.minecart.getLocation().getBlock().getChunk().getX()) > 1) {
    					continue;
    				}
    				if (Math.abs(event.getChunk().getZ() - minecart.minecart.getLocation().getBlock().getChunk().getZ()) > 1) {
    					continue;
    				}
    				event.setCancelled(true);
    				return;
    			}
    		}
    	}
    }
}
