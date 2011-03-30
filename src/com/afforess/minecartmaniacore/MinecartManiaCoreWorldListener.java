package com.afforess.minecartmaniacore;

import java.util.ArrayList;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class MinecartManiaCoreWorldListener extends WorldListener{
	public static final int CHUNK_RANGE = 3;
    public void onChunkUnload(ChunkUnloadEvent event) {
    	if (!event.isCancelled()) {
    		if (MinecartManiaWorld.isKeepMinecartsLoaded()) {
    			ArrayList<MinecartManiaMinecart> minecarts = MinecartManiaWorld.getMinecartManiaMinecartList();
    			for (MinecartManiaMinecart minecart : minecarts) {
    				if (Math.abs(event.getChunk().getX() - minecart.minecart.getLocation().getBlock().getChunk().getX()) > CHUNK_RANGE) {
    					continue;
    				}
    				if (Math.abs(event.getChunk().getZ() - minecart.minecart.getLocation().getBlock().getChunk().getZ()) > CHUNK_RANGE) {
    					continue;
    				}
    				event.setCancelled(true);
    				return;
    			}
    		}
    	}
    }
}
