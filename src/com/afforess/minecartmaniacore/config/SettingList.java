package com.afforess.minecartmaniacore.config;

import org.bukkit.Material;

import com.afforess.minecartmaniacore.MinecartManiaCore;

//Holds default settings and values
public class SettingList {
	public final static Setting[] config = {
		new Setting(
				"Minecarts Kill Mobs", 
				Boolean.TRUE, 
				"Minecarts that collide with mobs and animals will kill them and continue uninterrupted.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Pressure Plate Rails", 
				Boolean.TRUE, 
				"Pressure Plates will mimic the effect of rails, and minecarts will pass over them unhindered.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Keep Minecarts Loaded", 
				Boolean.FALSE, 
				"The Server will Load Chunks around minecarts, and will not unload chunks with minecarts in them. May cause excessive RAM usage.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"High Speed Booster Block", 
				new Integer(Material.GOLD_BLOCK.getId()),
				"Minecarts that pass over this will be boosted to the multiplier set below their current speed",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Minecarts return to owner", 
				Boolean.TRUE, 
				"Destroyed Minecarts will be sent back to the player (or chest) that created them, if possible. If not, they will be dropped normally.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Maximum Minecart Speed Percent", 
				new Integer(100), 
				"Adjusts the maximum minecart speed. 100% is equal to the vanilla Minecart speed. Large values will cause minecarts to derail.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"High Speed Booster Block Multiplier", 
				new Integer(8),
				"Multiplier for High Speed Booster Blocks. Do not use absurd values.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Low Speed Booster Block", 
				new Integer(Material.GOLD_ORE.getId()),
				"Minecarts that pass over this will be boosted to the multiplier set below their current speed",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Low Speed Booster Block Multiplier", 
				new Integer(2),
				"Multiplier for Low Speed Booster Blocks. Do not use absurd values.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"High Speed Brake Block", 
				new Integer(Material.SOUL_SAND.getId()),
				"Minecarts that pass over this will be slowed by the divisor set below their current speed",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"High Speed Brake Block Divisor", 
				new Integer(8),
				"Multiplier for High Speed Brake Blocks. Do not use absurd values.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Low Speed Brake Block", 
				new Integer(Material.GRAVEL.getId()),
				"Minecarts that pass over this will be slowed by the divisor set below their current speed",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Low Speed Brake Block Divisor", 
				new Integer(2),
				"Multiplier for Low Speed Brake Blocks. Do not use absurd values.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Reverse Block", 
				new Integer(Material.WOOL.getId()),
				"Minecarts that pass over this will have their momentum and speed reveresed.",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Catcher Block", 
				new Integer(Material.OBSIDIAN.getId()),
				"Minecarts that pass over this without being powered by redstone will be stopped",
				MinecartManiaCore.description.getName()
		),
		new Setting(
				"Ejector Block", 
				new Integer(Material.IRON_BLOCK.getId()),
				"Minecarts that pass over this will eject any passengers in the minecart",
				MinecartManiaCore.description.getName()
		)
	};

}
