package com.afforess.bukkit.minecartmaniacore.config;
import org.bukkit.plugin.Plugin;

import com.afforess.bukkit.minecartmaniacore.MinecartManiaCore;

public class Setting {
	
	private String name;
	private String desc;
	private Object value;
	private String plugin;
	
	public Setting(String name, Object value, String desc, String plugin) {
		this.name = name;
		this.desc = desc;
		this.value = value;
		this.plugin = plugin;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getPluginName() {
		return plugin;
	}
	
	public Plugin getPlugin() {
		return MinecartManiaCore.server.getPluginManager().getPlugin(plugin);
	}
	
	public boolean isBoolean() {
		return value instanceof Boolean;
	}
	
	public boolean isInteger() {
		return value instanceof Integer;
	}
	
	public boolean isDouble() {
		return value instanceof Double;
	}
	
	public boolean isString() {
		return value instanceof String;
	}

}
