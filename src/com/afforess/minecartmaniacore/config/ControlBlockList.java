package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

public class ControlBlockList {
    protected static ArrayList<ControlBlock> controlBlocks = new ArrayList<ControlBlock>();
    
    public static List<ControlBlock> getControlBlockList() {
        return controlBlocks;
    }
    
    public static boolean isControlBlock(final SpecificMaterial mat) {
        return getControlBlock(mat) != null;
    }
    
    public static ControlBlock getControlBlock(final SpecificMaterial item) {
        if (item == null)
            return null;
        for (final ControlBlock cb : controlBlocks) {
            if (cb == null)
                return null;
            if (cb.match(item))
                return cb;
        }
        return null;
    }
    
    public static boolean hasSpeedMultiplier(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.getSpeedMultipliers().size() > 0;
        return false;
    }
    
    public static double getSpeedMultiplier(final MinecartManiaMinecart minecart) {
        final ControlBlock block = getControlBlock(minecart.getSpecificMaterialBeneath());
        if (block != null) {
            final List<SpeedMultiplier> multipliers = block.getSpeedMultipliers();
            for (final SpeedMultiplier speed : multipliers) {
                if (!isCorrectState(minecart.getBlockBeneath(), speed.redstone)) {
                    continue;
                }
                if ((speed.passenger == PassengerState.Disables) && (minecart.minecart.getPassenger() != null)) {
                    continue;
                }
                if ((speed.passenger == PassengerState.Enables) && (minecart.minecart.getPassenger() == null)) {
                    continue;
                }
                if ((speed.direction != CompassDirection.NO_DIRECTION) && (speed.direction != minecart.getDirection())) {
                    continue;
                }
                int type = 0;
                if (minecart.isPoweredMinecart()) {
                    type = 1;
                } else if (minecart.isStorageMinecart()) {
                    type = 2;
                }
                if (!speed.types[type]) {
                    continue;
                }
                return speed.multiplier;
            }
            
        }
        return 1.0D;
    }
    
    public static boolean isCatcherBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isCatcherBlock();
        return false;
    }
    
    public static boolean isValidCatcherBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isCatcherBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getCatcherState());
    }
    
    public static double getLaunchSpeed(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.getLauncherSpeed();
        return 0.0D;
    }
    
    public static boolean isValidLauncherBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return (getLaunchSpeed(item) != 0.0D) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getLauncherState());
    }
    
    public static boolean isEjectorBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isEjectorBlock();
        return false;
    }
    
    public static boolean isValidEjectorBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isEjectorBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getEjectorState());
    }
    
    public static boolean isPlatformBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isPlatformBlock();
        return false;
    }
    
    public static boolean isValidPlatformBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isPlatformBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getPlatformState());
    }
    
    public static boolean isStationBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isStationBlock();
        return false;
    }
    
    public static boolean isValidStationBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isStationBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getStationState());
    }
    
    public static boolean isKillMinecartBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isKillMinecart();
        return false;
    }
    
    public static boolean isValidKillMinecartBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isKillMinecartBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getKillState());
    }
    
    public static boolean isSpawnMinecartBlock(final SpecificMaterial material) {
        final ControlBlock block = getControlBlock(material);
        if (block != null)
            return block.isSpawnMinecart();
        return false;
    }
    
    public static boolean isElevatorBlock(final SpecificMaterial item) {
        final ControlBlock block = getControlBlock(item);
        if (block != null)
            return block.isElevatorBlock();
        return false;
    }
    
    public static boolean isValidElevatorBlock(final MinecartManiaMinecart minecart) {
        final SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isElevatorBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getElevatorState());
    }
    
    private static boolean isCorrectState(final boolean power, final RedstoneState state) {
        switch (state) {
            case Default:
                return true;
            case Enables:
                return power;
            case Disables:
                return !power;
        }
        return false;
    }
    
    private static boolean isCorrectState(final Block block, final RedstoneState state) {
        boolean power = block.isBlockIndirectlyPowered() || block.getRelative(0, -1, 0).isBlockIndirectlyPowered();
        if (block.getTypeId() == Material.POWERED_RAIL.getId()) {
            power = (block.getData() & 0x8) != 0;
        }
        switch (state) {
            case Default:
                return true;
            case Enables:
                return power;
            case Disables:
                return !power;
        }
        return false;
    }
}
