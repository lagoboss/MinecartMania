package com.afforess.minecartmaniacore;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkManager {
	protected HashSet<Chunk> loaded = new HashSet<Chunk>();
	
	public void updateChunks(Location location) {
		Chunk center = location.getBlock().getChunk();
		World world = center.getWorld();
		int range = 3;
		for (int dx = -(range); dx <= range; dx++){
			for (int dz = -(range); dz <= range; dz++){
				Chunk chunk = world.getChunkAt(center.getX()+dx, center.getZ()+dz);
				world.loadChunk(chunk);
				if (!loaded.contains(chunk)) {
					loaded.add(chunk);
				}
			}
		}
		Iterator<Chunk> i = loaded.iterator();
		while(i.hasNext()) {
			Chunk old = i.next();
			if (old.getX() > center.getX()+range || old.getX() < center.getX()-range) {
				world.unloadChunkRequest(old.getX(), old.getZ());
				i.remove();
			}
			else if (old.getZ() > center.getZ()+range || old.getZ() < center.getZ()-range) {
				world.unloadChunkRequest(old.getX(), old.getZ());
				i.remove();
			}
		}
	}
	
	public void unloadChunks(Location location) {
		World world = location.getWorld();
		Iterator<Chunk> i = loaded.iterator();
		while(i.hasNext()) {
			Chunk old = i.next();
			world.unloadChunkRequest(old.getX(), old.getZ());
			i.remove();
		}
	}
}
