package com.afforess.minecartmaniacore.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.config.RedstoneState;
import com.afforess.minecartmaniacore.event.ChestPoweredEvent;
import com.afforess.minecartmaniacore.inventory.MinecartManiaChest;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.MinecartTypeSign;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.utils.MinecartUtils;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

public class MinecartManiaCoreBlockListener extends BlockListener {
    private final HashMap<Location, Long> lastSpawn = new HashMap<Location, Long>();
    
    @Override
    public void onBlockRedstoneChange(final BlockRedstoneEvent event) {
        if ((event.getOldCurrent() > 0) && (event.getNewCurrent() > 0))
            return;
        final boolean power = event.getNewCurrent() > 0;
        final Block block = event.getBlock();
        
        final int range = 1;
        for (int dx = -(range); dx <= range; dx++) {
            for (int dy = -(range); dy <= range; dy++) {
                for (int dz = -(range); dz <= range; dz++) {
                    final Block b = MinecartManiaWorld.getBlockAt(block.getWorld(), block.getX() + dx, block.getY() + dy, block.getZ() + dz);
                    if (b.getState() instanceof Chest) {
                        final Chest chest = (Chest) b.getState();
                        final MinecartManiaChest mmc = MinecartManiaWorld.getMinecartManiaChest(chest);
                        if (mmc != null) {
                            final boolean previouslyPowered = mmc.isRedstonePower();
                            if (!previouslyPowered && power) {
                                mmc.setRedstonePower(power);
                                final ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
                                MinecartManiaCore.callEvent(cpe);
                            } else if (previouslyPowered && !power) {
                                mmc.setRedstonePower(power);
                                final ChestPoweredEvent cpe = new ChestPoweredEvent(mmc, power);
                                MinecartManiaCore.callEvent(cpe);
                            }
                        }
                    }
                    final SpecificMaterial material = new SpecificMaterial(b.getTypeId(), b.getData());
                    if (ControlBlockList.isSpawnMinecartBlock(material)) {
                        if ((ControlBlockList.getControlBlock(material).getSpawnState() != RedstoneState.Enables) || power) {
                            if ((ControlBlockList.getControlBlock(material).getSpawnState() != RedstoneState.Disables) || !power) {
                                if (MinecartUtils.isTrack(b.getRelative(0, 1, 0).getTypeId())) {
                                    final Long lastSpawn = this.lastSpawn.get(b.getLocation());
                                    if ((lastSpawn == null) || (Math.abs(System.currentTimeMillis() - lastSpawn) > 1000)) {
                                        final Location spawn = b.getLocation().clone();
                                        spawn.setY(spawn.getY() + 1);
                                        final MinecartManiaMinecart minecart = MinecartManiaWorld.spawnMinecart(spawn, getMinecartType(b.getLocation()), null);
                                        this.lastSpawn.put(b.getLocation(), System.currentTimeMillis());
                                        if (ControlBlockList.getLaunchSpeed(SpecificMaterial.convertBlock(b)) != 0.0) {
                                            minecart.launchCart(ControlBlockList.getLaunchSpeed(SpecificMaterial.convertBlock(b)));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static Material getMinecartType(final Location loc) {
        final ArrayList<Sign> signList = SignUtils.getAdjacentMinecartManiaSignList(loc, 2);
        for (final Sign sign : signList) {
            if (sign instanceof MinecartTypeSign) {
                final MinecartTypeSign type = (MinecartTypeSign) sign;
                if (type.canDispenseMinecartType(Material.MINECART))
                    return Material.MINECART;
                if (type.canDispenseMinecartType(Material.POWERED_MINECART))
                    return Material.POWERED_MINECART;
                if (type.canDispenseMinecartType(Material.STORAGE_MINECART))
                    return Material.STORAGE_MINECART;
            }
        }
        
        //Returns standard minecart by default
        return Material.MINECART;
    }
}
