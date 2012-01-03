package com.afforess.minecartmaniacore.minecart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.ChunkCoordIntPair;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class ChunkManager {
    protected ConcurrentHashMap<ChunkCoordIntPair, List<Integer>> loaded = new ConcurrentHashMap<ChunkCoordIntPair, List<Integer>>();
    protected static ConcurrentHashMap<UUID, ChunkManager> worlds = new ConcurrentHashMap<UUID, ChunkManager>();
    private final CraftWorld world;
    private final int range = 4;
    
    public static void Init(final World w) {
        if (!worlds.contains(w.getUID())) {
            worlds.put(w.getUID(), new ChunkManager(w));
        }
    }
    
    public ChunkManager(final World world) {
        this.world = (CraftWorld) world;
    }
    
    public static void updateChunks(final Entity ent) {
        worlds.get(ent.getWorld().getUID()).updateChunks(ent.getEntityId(), ent.getLocation());
    }
    
    public static void unloadChunks(final Entity ent) {
        worlds.get(ent.getWorld().getUID()).unloadChunks(ent.getEntityId());
    }
    
    public void updateChunks(final int entityID, final Location location) {
        final Chunk center = location.getBlock().getChunk();
        for (int dx = -(range); dx <= range; dx++) {
            for (int dz = -(range); dz <= range; dz++) {
                final ChunkCoordIntPair cpos = new ChunkCoordIntPair(center.getX() + dx, center.getZ() + dz);
                if (!loaded.containsKey(cpos)) {
                    final Chunk chunk = world.getChunkAt(center.getX() + dx, center.getZ() + dz);
                    world.loadChunk(chunk);
                    loaded.put(cpos, new ArrayList<Integer>());
                }
                if (!loaded.get(cpos).contains(entityID)) {
                    loaded.get(cpos).add(entityID);
                }
            }
        }
        trimLoadedChunks(-1);
    }
    
    private void trimLoadedChunks(final int unloadByOwner) {
        final Iterator<Entry<ChunkCoordIntPair, List<Integer>>> iterate = loaded.entrySet().iterator();
        
        int unloadedChunks = 0;
        while (iterate.hasNext()) {
            final Entry<ChunkCoordIntPair, List<Integer>> e = iterate.next();
            // Remove ourselves from the list of owners, if applicable.
            if ((unloadByOwner != 0) && e.getValue().contains(unloadByOwner)) {
                e.getValue().remove(unloadByOwner);
            }
            
            boolean unload = false;
            
            if (!unload && (e.getValue().size() > 0)) {
                for (final int owner : e.getValue()) {
                    final net.minecraft.server.Entity ent = world.getHandle().getEntity(owner);
                    if (ent == null) {
                        MinecartManiaLogger.getInstance().severe("[ChunkManager] Can't find owner " + Integer.toString(owner), true, new Object[] {});
                        continue;
                    }
                    final int chunkX = e.getKey().x;
                    final int chunkZ = e.getKey().z;
                    final int ownerX = ent.getBukkitEntity().getLocation().getChunk().getX();
                    final int ownerZ = ent.getBukkitEntity().getLocation().getChunk().getZ();
                    if ((Math.abs(chunkX - ownerX) > range) || (Math.abs(chunkZ - ownerZ) > range)) {
                        unload = true;
                        break;
                    }
                }
            }
            
            // If we don't own it, unload it.
            // If the chunk's not in range of the cart, also unload it.
            if (unload) {
                unloadChunk(e.getKey().x, e.getKey().z);
                unloadedChunks++;
                iterate.remove();
            }
        }
        MinecartManiaLogger.getInstance().info("[ChunkManager] Unloaded " + unloadedChunks + ".");
    }
    
    public void unloadChunks(final int entityID) {
        trimLoadedChunks(entityID);
    }
    
    private boolean spawnChunk(final int x, final int z) {
        //copied from MC Server code
        final int k = (x * 16) + 8;
        final int l = (z * 16) + 8;
        final short short1 = 128;
        if ((k < -short1) || (k > short1) || (l < -short1) || (l > short1))
            return false;
        return true;
    }
    
    private boolean unloadChunk(final int x, final int z) {
        //Spawn must never be unloaded
        if (spawnChunk(x, z))
            return false;
        if (!world.isChunkInUse(x, z)) {
            if (world.unloadChunk(x, z, true, false))
                return true;
        }
        return false;
    }
}
