package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class ControlBlockList {
	protected static ArrayList<ControlBlock> controlBlocks = new ArrayList<ControlBlock>();
	
	public static List<ControlBlock> getControlBlockList() {
		return controlBlocks;
	}
	
	public static boolean isControlBlock(Item item) {
		return getControlBlock(item) != null;
	}
	
	public static ControlBlock getControlBlock(Item item) {
		for (ControlBlock cb : controlBlocks) {
			if (cb.getType().equals(item)) {
				return cb;
			}
		}
		return null;
	}
	
	public static boolean hasSpeedMultiplier(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.getSpeedMultipliers().size() > 0;
		}
		return false;
	}
	
	public static double getSpeedMultiplier(MinecartManiaMinecart minecart) {
		ControlBlock block = getControlBlock(minecart.getItemBeneath());
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
				if (minecart.isPoweredMinecart()) type = 1;
				else if (minecart.isStorageMinecart()) type = 2;
				if (!speed.types[type]) {
					continue;
				}
				return speed.multiplier;
			}
			
		}
		return 1.0D;
	}
	
	public static boolean isCatcherBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isCatcherBlock();
		}
		return false;
	}
	
	public static boolean isValidCatcherBlock(Block block) {
		return isCatcherBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getCatcherState());
	}
	
	public static double getLaunchSpeed(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.getLauncherSpeed();
		}
		return 0.0D;
	}
	
	public static boolean isValidLauncherBlock(Block block) {
		return getLaunchSpeed(blockToItem(block)) != 0.0D && isCorrectState(block, getControlBlock(blockToItem(block)).getLauncherState());
	}
	
	public static boolean isEjectorBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isEjectorBlock();
		}
		return false;
	}
	
	public static boolean isValidEjectorBlock(Block block) {
		return isEjectorBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getEjectorState());
	}
	
	public static boolean isPlatformBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isPlatformBlock();
		}
		return false;
	}
	
	public static boolean isValidPlatformBlock(Block block) {
		return isPlatformBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getPlatformState());
	}
	
	public static boolean isStationBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isStationBlock();
		}
		return false;
	}
	
	public static boolean isValidStationBlock(Block block) {
		return isStationBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getStationState());
	}
	
	public static boolean isKillMinecartBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isKillMinecart();
		}
		return false;
	}
	
	public static boolean isValidKillMinecartBlock(Block block) {
		return isKillMinecartBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getKillState());
	}
	
	public static boolean isSpawnMinecartBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isSpawnMinecart();
		}
		return false;
	}
	
	public static boolean isValidSpawnMinecartBlock(Block block) {
		return isSpawnMinecartBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getSpawnState());
	}
	
	public static boolean isElevatorBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isElevatorBlock();
		}
		return false;
	}

	public static boolean isValidElevatorBlock(Block block) {
		return isElevatorBlock(blockToItem(block)) && isCorrectState(block, getControlBlock(blockToItem(block)).getElevatorState());
	}
	
	private static Item blockToItem(Block block) {
		return Item.getItem(block.getTypeId(), block.getData());
	}
	
	private static boolean isCorrectState(Block block, RedstoneState state) {
		switch(state) {
			case Default: return true;
			case Enables: return block.isBlockIndirectlyPowered() || block.getRelative(0, -1, 0).isBlockIndirectlyPowered();
			case Disables: return !block.isBlockIndirectlyPowered() && !block.getRelative(0, -1, 0).isBlockIndirectlyPowered();
		}
		return false;
	}
}
