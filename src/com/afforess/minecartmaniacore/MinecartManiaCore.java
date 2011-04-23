package com.afforess.minecartmaniacore;
import java.io.File;
import java.io.PrintWriter;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.config.CoreSettingParser;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class MinecartManiaCore extends JavaPlugin {
	
	public final MinecartManiaCoreListener listener = new MinecartManiaCoreListener(this);
	public final MinecartManiaCoreBlockListener blockListener = new MinecartManiaCoreBlockListener();
	public final MinecartManiaCoreWorldListener worldListener = new MinecartManiaCoreWorldListener();
	public final MinecartManiaActionListener actionListener = new MinecartManiaActionListener();
	public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile description;
	public static File data;
	public static File MinecartManiaCore;
	public static String dataDirectory = "plugins" + File.separator + "MinecartMania";
	public static boolean WormholeXTreme = false;
	public static boolean Nethrar = false;
	public static boolean Lockette = false;
	public static boolean LWC = false;
	public static boolean ChestLock = false;
	
	public void onEnable(){
		server = this.getServer();
		description = this.getDescription();
		instance = this;
		data = getDataFolder();
		MinecartManiaCore = this.getFile();
		
		//manage external plugins
		WormholeXTreme = server.getPluginManager().getPlugin("WormholeXTreme") != null;
		Nethrar = server.getPluginManager().getPlugin("Nethrar") != null;
		Lockette = server.getPluginManager().getPlugin("Lockette") != null;
		LWC = server.getPluginManager().getPlugin("LWC") != null;
		ChestLock = server.getPluginManager().getPlugin("ChestLock") != null;

		writeItemsFile();

		MinecartManiaConfigurationParser.read("MinecartManiaConfiguration.xml", dataDirectory, new CoreSettingParser());
		MinecartManiaConfigurationParser.read("MinecartManiaLocale.xml", dataDirectory, new LocaleParser());

		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_UPDATE, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_COLLISION_ENTITY, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DAMAGE, listener, Priority.Lowest, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DESTROY, listener, Priority.Lowest, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, listener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, actionListener, Priority.Normal, this);

		log.info( description.getName() + " version " + description.getVersion() + " is enabled!" );
	}
	
	public void onDisable(){
		server.getScheduler().cancelTasks(instance);
		log.info( description.getName() + " version " + description.getVersion() + " is disabled!" );
	}
	
	private void writeItemsFile() {
		try {
			File items = new File(dataDirectory + File.separator + "items.txt");
			PrintWriter pw = new PrintWriter(items);
			pw.append("This file is a list of all the data values, and matching item names for Minecart Mania. \nThis list is never used, and changes made to this file will be ignored");
			pw.append("\n");
			pw.append("\n");
			pw.append("Items:");
			pw.append("\n");
			for (Item item : Item.values()) {
				String name = "Item Name: " + item.toString();
				pw.append(name);
				String id = "";
				for (int i = name.length()-1; i < 40; i++) {
					id += " ";
				}
				pw.append(id);
				id = "Item Id: " + String.valueOf(item.getId());
				pw.append(id);
				String data = "";
				for (int i = id.length()-1; i < 15; i++) {
					data += " ";
				}
				data += "Item Data: " + String.valueOf(item.getData());
				pw.append(data);
				pw.append("\n");
			}
			pw.close();
		}
		catch (Exception e) {}
	}
}
