package com.afforess.minecartmaniacore;

import org.bukkit.Material;
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
    	
    	if (power) {
	    	//Launch Minecarts
	    	MinecartManiaMinecart cart = null;
	    	//Rail check for performance reasons
	    	if (block.getRelative(-1, 0, 0).getTypeId() == Material.RAILS.getId()) {
	    		cart = MinecartManiaWorld.getMinecartManiaMinecartAt(block.getX()-1, block.getY(), block.getZ());
	    	}
	    	if (block.getRelative(1, 0, 0).getTypeId() == Material.RAILS.getId()) {
	    		cart = MinecartManiaWorld.getMinecartManiaMinecartAt(block.getX()+1, block.getY(), block.getZ());
	    	}
	    	if (block.getRelative(0, 0, -1).getTypeId() == Material.RAILS.getId()) {
	    		cart = MinecartManiaWorld.getMinecartManiaMinecartAt(block.getX(), block.getY(), block.getZ()-1);
	    	}
	    	if (block.getRelative(0, 0, 1).getTypeId() == Material.RAILS.getId()) {
	    		cart = MinecartManiaWorld.getMinecartManiaMinecartAt(block.getX(), block.getY(), block.getZ()+1);
	    	}
	    	if (cart != null) {
				cart.doLauncherBlock();
			}
    	}


    	
    	int range = 1;
    	for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					if (MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState() instanceof Chest) {
						Chest chest = (Chest)MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz).getState();
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
