package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

public class ControlBlockList {
    protected static ArrayList<ControlBlock> controlBlocks = new ArrayList<ControlBlock>();
    
    public static List<ControlBlock> getControlBlockList() {
        return controlBlocks;
    }
    
    public static boolean isControlBlock(SpecificMaterial mat) {
        return getControlBlock(mat) != null;
    }
    
    public static ControlBlock getControlBlock(SpecificMaterial item) {
        if (item == null)
            return null;
        for (ControlBlock cb : controlBlocks) {
            if(cb==null) return null;
            for(ItemMatcher match : cb.getMatchers())
            if (match.match(new ItemStack(item.id,1,item.durability))) {
                return cb;
            }
        }
        return null;
    }
    
    public static boolean hasSpeedMultiplier(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.getSpeedMultipliers().size() > 0;
        }
        return false;
    }
    
    public static double getSpeedMultiplier(MinecartManiaMinecart minecart) {
        ControlBlock block = getControlBlock(minecart.getSpecificMaterialBeneath());
        if (block != null) {
            List<SpeedMultiplier> multipliers = block.getSpeedMultipliers();
            for (SpeedMultiplier speed : multipliers) {
                if (!isCorrectState(minecart.getBlockBeneath(), speed.redstone)) {
                    continue;
                }
                if (speed.passenger == PassengerState.Disables && minecart.minecart.getPassenger() != null) {
                    continue;
                }
                if (speed.passenger == PassengerState.Enables && minecart.minecart.getPassenger() == null) {
                    continue;
                }
                if (speed.direction != CompassDirection.NO_DIRECTION && speed.direction != minecart.getDirection()) {
                    continue;
                }
                int type = 0;
                if (minecart.isPoweredMinecart())
                    type = 1;
                else if (minecart.isStorageMinecart())
                    type = 2;
                if (!speed.types[type]) {
                    continue;
                }
                return speed.multiplier;
            }
            
        }
        return 1.0D;
    }
    
    public static boolean isCatcherBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isCatcherBlock();
        }
        return false;
    }
    
    public static boolean isValidCatcherBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isCatcherBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getCatcherState());
    }
    
    public static double getLaunchSpeed(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.getLauncherSpeed();
        }
        return 0.0D;
    }
    
    public static boolean isValidLauncherBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return getLaunchSpeed(item) != 0.0D && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getLauncherState());
    }
    
    public static boolean isEjectorBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isEjectorBlock();
        }
        return false;
    }
    
    public static boolean isValidEjectorBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isEjectorBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getEjectorState());
    }
    
    public static boolean isPlatformBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isPlatformBlock();
        }
        return false;
    }
    
    public static boolean isValidPlatformBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isPlatformBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getPlatformState());
    }
    
    public static boolean isStationBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isStationBlock();
        }
        return false;
    }
    
    public static boolean isValidStationBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isStationBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getStationState());
    }
    
    public static boolean isKillMinecartBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isKillMinecart();
        }
        return false;
    }
    
    public static boolean isValidKillMinecartBlock(
            MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isKillMinecartBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getKillState());
    }
    
    public static boolean isSpawnMinecartBlock(SpecificMaterial material) {
        ControlBlock block = getControlBlock(material);
        if (block != null) {
            return block.isSpawnMinecart();
        }
        return false;
    }
    
    public static boolean isElevatorBlock(SpecificMaterial item) {
        ControlBlock block = getControlBlock(item);
        if (block != null) {
            return block.isElevatorBlock();
        }
        return false;
    }
    
    public static boolean isValidElevatorBlock(MinecartManiaMinecart minecart) {
        SpecificMaterial item = minecart.getSpecificMaterialBeneath();
        return isElevatorBlock(item) && isCorrectState(minecart.isPoweredBeneath(), getControlBlock(item).getElevatorState());
    }
    
    private static boolean isCorrectState(boolean power, RedstoneState state) {
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
    
    private static boolean isCorrectState(Block block, RedstoneState state) {
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
