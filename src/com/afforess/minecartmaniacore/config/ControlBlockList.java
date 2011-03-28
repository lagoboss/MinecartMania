package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

import com.afforess.minecartmaniacore.Item;

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
	
	public static double getSpeedMultiplier(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.getMultiplier();
		}
		return 1.0D;
	}
	
	public static boolean isValidSpeedMultiplierBlock(Block block) {
		return getSpeedMultiplier(blockToItem(block)) != 1.0 && isCorrectState(block, getControlBlock(blockToItem(block)).getMultiplierState());
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
