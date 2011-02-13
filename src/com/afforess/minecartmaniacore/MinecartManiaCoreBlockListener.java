package com.afforess.minecartmaniacore;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.afforess.minecartmaniacore.event.ChestPoweredEvent;

public class MinecartManiaCoreBlockListener extends BlockListener{
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {    
    	if (event.getOldCurrent() > 0 && event.getNewCurrent() > 0) {
    		return;
    	}
    	boolean power = event.getNewCurrent() > 0;
    	Block block = event.getBlock();
    	
    	
    	int rangex = 1;
    	int rangey = 2;
    	int rangez = 1;
    	if (power) {
	    	//Launch Minecarts
	    	MinecartManiaMinecart cart = null;
	    	for (int dx = -(rangex); dx <= rangex; dx++){
				for (int dy = -(rangey); dy <= rangey; dy++){
					for (int dz = -(rangez); dz <= rangez; dz++){
						//Rail check for performance reasons
						if (MinecartUtils.isMinecartTrack(block.getRelative(dx, dy, dz))) {
							cart = MinecartManiaWorld.getMinecartManiaMinecartAt(block.getX()+dx, block.getY()+dy, block.getZ()+dz);
							if (cart != null) {
								cart.doLauncherBlock();
							}
						}
					}
				}
			}
    	}


    	
    	int range = 1;
    	for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					Block b = MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz);
					if (b.getState() instanceof Chest) {
						Chest chest = (Chest)b.getState();
						MinecartManiaChest mmc = MinecartManiaWorld.getMinecartManiaChest(chest);
						boolean previouslyPowered = mmc.isRedstonePower();
						if (!previouslyPowered && power) {
							mmc.setRedstonePower(power);
							ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
							MinecartManiaCore.server.getPluginManager().callEvent(cpe);
						}
						else if (previouslyPowered && !power) {
							mmc.setRedstonePower(power);
							ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
							MinecartManiaCore.server.getPluginManager().callEvent(cpe);
							
						}
					}
				}
			}
    	}
    }
}
