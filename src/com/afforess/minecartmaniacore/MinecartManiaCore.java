package com.afforess.minecartmaniacore;
import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.config.CoreSettingParser;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;

public class MinecartManiaCore extends JavaPlugin {
	
	public final MinecartManiaCoreListener listener = new MinecartManiaCoreListener(this);
	public final MinecartManiaCoreBlockListener blockListener = new MinecartManiaCoreBlockListener();
	public final MinecartManiaCoreWorldListener worldListener = new MinecartManiaCoreWorldListener();
	public static Logger log;
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile description;
	public static File data;
	public static File MinecartManiaCore;
	public static String dataDirectory = "plugins" + File.separator + "MinecartMania";
	
	

	public void onEnable(){
		server = this.getServer();
		description = this.getDescription();
		instance = this;
		log = Logger.getLogger("Minecraft");
		data = getDataFolder();
		MinecartManiaCore = this.getFile();
		String path = data.getPath();
		path = path.replace("plugins"+File.separatorChar+"MinecartManiaCore", "");
		if (!path.isEmpty()) {
			dataDirectory = path + dataDirectory;
		}

		writeItemsFile();

		MinecartManiaConfigurationParser.read("MinecartManiaConfiguration.xml", dataDirectory, new CoreSettingParser());

		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_UPDATE, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_COLLISION_ENTITY, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DAMAGE, listener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, listener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public void onDisable(){
		
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
