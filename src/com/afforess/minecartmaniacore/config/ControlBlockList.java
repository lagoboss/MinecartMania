package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

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
	
	public static boolean isCatcherBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isCatcherBlock();
		}
		return false;
	}
	
	public static double getLaunchSpeed(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.getLauncherSpeed();
		}
		return 0.0D;
	}
	
	public static boolean isEjectorBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isEjectorBlock();
		}
		return false;
	}
	
	public static boolean isPlatformBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isPlatformBlock();
		}
		return false;
	}
	
	public static boolean isStationBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isStationBlock();
		}
		return false;
	}
	
	public static boolean isKillMinecartBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isKillMinecart();
		}
		return false;
	}
	
	public static boolean isSpawnMinecartBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isSpawnMinecart();
		}
		return false;
	}
	
	public static boolean isRedstoneDisables(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isRedstoneDisables();
		}
		return false;
	}
	
	public static boolean isReqRedstone(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isReqRedstone();
		}
		return false;
	}
}
