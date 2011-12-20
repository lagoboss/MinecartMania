package com.afforess.minecartmaniacore.api;

import java.util.ArrayList;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.afforess.minecartmaniacore.config.MinecartManiaConfiguration;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class MinecartManiaCoreWorldListener extends WorldListener {
    public static final int CHUNK_RANGE = 4;
    
    @Override
    public void onChunkUnload(final ChunkUnloadEvent event) {
        if (!event.isCancelled()) {
            if (MinecartManiaConfiguration.isKeepMinecartsLoaded()) {
                final ArrayList<MinecartManiaMinecart> minecarts = MinecartManiaWorld.getMinecartManiaMinecartList();
                for (final MinecartManiaMinecart minecart : minecarts) {
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
