package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import com.afforess.minecartmaniacore.Item;

public class ControlBlockList implements SettingList {
	private static ArrayList<ControlBlock> controlBlocks = new ArrayList<ControlBlock>();
	private static String[] tags = { "BlockType", "SpeedMultiplier", "Catch", "Launch", "Eject", "Platform", "Station"};
	private static boolean defaults = true;
	
	public ControlBlockList() {
		controlBlocks.add(new ControlBlock(Item.OBSIDIAN, 1.0, true, true, false, false, false));
		controlBlocks.add(new ControlBlock(Item.GOLD_BLOCK, 16.0, false, false, false, false, false));
		controlBlocks.add(new ControlBlock(Item.GOLD_ORE, 4.0, false, false, false, false, false));
		controlBlocks.add(new ControlBlock(Item.WOOL, -1.0, false, false, false, false, false));
		controlBlocks.add(new ControlBlock(Item.SOUL_SAND, 0.075, false, false, false, false, false));
		controlBlocks.add(new ControlBlock(Item.GRAVEL, 0.25, false, false, false, false, false));
		controlBlocks.add(new ControlBlock(Item.IRON_BLOCK, 1.0, false, false, true, false, false));
		controlBlocks.add(new ControlBlock(Item.BRICK, 1.0, false, false, false, false, true));
		controlBlocks.add(new ControlBlock(Item.LIGHT_GREEN_WOOL, 1.0, false, false, false, true, false));
	}
	
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
	
	public static boolean isLauncherBlock(Item item) {
		ControlBlock block = getControlBlock(item);
		if (block != null) {
			return block.isLauncherBlock();
		}
		return false;
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

	@Override
	public String getTagHeading() {
		return "ControlBlocks";
	}

	@Override
	public String getTagName() {
		return "ControlBlock";
	}

	@Override
	public int getNumTags() {
		return tags.length;
	}

	@Override
	public String getTag(int tag) {
		return tag < tags.length ? tags[tag] : null;
	}
	
	@Override
	public boolean isRepeatingTag(int tag) {
		return false;
	}
	
	@Override
	public boolean isItemTag(int tag) {
		return tag == 0;
	}
	
	@Override
	public String getTagComment(int tag) {
		switch(tag) {
		case 0:
			return "Item Name or Item Id for this control block";
		case 1:
			return "Speed Multiplier for this block";
		case 2: 
			return "Marks this as a catcher block";
		case 3:
			return "Marks this as a launcher block";
		case 4:
			return "Marks this as a ejector block";
		case 5:
			return "Marks this as a platform block";
		case 6:
			return "Marks this as a station block";
		default:
			return null;
		}
	}

	@Override
	public int getNumElements() {
		return controlBlocks.size();
	}

	@Override
	public Object getElementForTag(int tag, int element) {
		ControlBlock block = controlBlocks.get(tag);
		switch(element) {
		case 0:
			return block.getType();
		case 1:
			return block.getMultiplier() != 1.0 ? block.getMultiplier() : null;
		case 2: 
			return block.isCatcherBlock() ? true : null;
		case 3:
			return block.isLauncherBlock() ? true : null;
		case 4:
			return block.isEjectorBlock() ? true : null;
		case 5:
			return block.isPlatformBlock() ? true : null;
		case 6:
			return block.isStationBlock() ? true : null;
		default:
			return null;
		}
	}

	@Override
	public void setElementForTag(int tag, int element, Object value) {
		try {
			//Once we begin loading data, ignore the defaults
			if (defaults) {
				defaults = false;
				controlBlocks = new ArrayList<ControlBlock>();
			}
			ControlBlock block = null;
			if (element < controlBlocks.size()) {
				block = controlBlocks.get(element);
			}
			else {
				block = new ControlBlock();
				controlBlocks.add(block);
			}
			if (value == null) {
				value = false;
			}
			switch(tag) {
			case 0:
				block.setType((Item)value);
			case 1:
				block.setMultiplier((Double)value);
			case 2: 
				block.setCatcherBlock((Boolean)value);
			case 3:
				block.setLauncherBlock((Boolean)value);
			case 4:
				block.setEjectorBlock((Boolean)value);
			case 5:
				block.setPlatformBlock((Boolean)value);
			case 6:
				block.setStationBlock((Boolean)value);
			}
		}
		catch (Exception e) {
			
		}
	}
}
