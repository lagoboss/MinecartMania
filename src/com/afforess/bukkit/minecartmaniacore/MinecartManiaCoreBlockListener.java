package com.afforess.bukkit.minecartmaniacore;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.afforess.bukkit.minecartmaniacore.event.ChestPoweredEvent;

public class MinecartManiaCoreBlockListener extends BlockListener{
    public void onBlockRedstoneChange(BlockFromToEvent bfte) {    
    	//Funny handling
    	BlockRedstoneEvent event = ((BlockRedstoneEvent)bfte);
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
					if (MinecartManiaWorld.getBlockAt(block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState() instanceof Chest) {
						Chest chest = (Chest)MinecartManiaWorld.getBlockAt(block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState();
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
