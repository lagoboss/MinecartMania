package com.afforess.minecartmaniacore;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

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
    	
    	
    	//Button unpressed will not trigger RedstoneChange event, which is needed to tell if the chest is still powered
		//This tests whether the chest is still powered after 8 ticks, which is when a button unpresses itself
		if (block.getTypeId() == Material.STONE_BUTTON.getId() && power) {
			final BlockRedstoneEvent bre = new BlockRedstoneEvent(block, 16, 0);
			PluginManager pm = MinecartManiaCore.server.getPluginManager();
			@SuppressWarnings("rawtypes")
			Class[] params = {Event.class};
			Object[] args = {bre};
			
			try {
				MinecartManiaTaskScheduler.doAsyncTask(PluginManager.class.getDeclaredMethod("callEvent", params), pm, 8, args);
			} catch (Exception e) { }
		}
    }
}
