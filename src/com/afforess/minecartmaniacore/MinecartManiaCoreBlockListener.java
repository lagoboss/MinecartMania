package com.afforess.minecartmaniacore;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.afforess.minecartmaniacore.event.ChestPoweredEvent;

public class MinecartManiaCoreBlockListener extends BlockListener{
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {    
    	if (event.isCancelled()) {
    		return;
    	}
    	if (event.getOldCurrent() > 0 && event.getNewCurrent() > 0) {
    		return;
    	}
    	boolean power = event.getNewCurrent() > 0;
    	
    	Block block = event.getToBlock();
    	int range = 1;
    	for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					if (MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState() instanceof Chest) {
						Chest chest = (Chest)MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState();
						MinecartManiaChest mmc = MinecartManiaWorld.getMinecartManiaChest(chest);
						boolean previouslyPowered = mmc.isRedstonePower();
						if (!previouslyPowered && power) {
							ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
							MinecartManiaCore.server.getPluginManager().callEvent(cpe);
							mmc.setRedstonePower(power);
						}
						else if (previouslyPowered && !power) {
							ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
							MinecartManiaCore.server.getPluginManager().callEvent(cpe);
							mmc.setRedstonePower(power);
						}
					}
				}
			}
    	}
    }
}
