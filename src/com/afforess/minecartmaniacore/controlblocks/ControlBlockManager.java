package com.afforess.minecartmaniacore.controlblocks;

import java.util.ArrayList;
import java.util.List;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.config.ControlBlock;

public class ControlBlockManager {
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
}
