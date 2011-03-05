package com.afforess.minecartmaniacore;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.config.SettingList;

public class MinecartManiaCore extends JavaPlugin {
	
	public final MinecartManiaCoreListener listener = new MinecartManiaCoreListener(this);
	public final MinecartManiaCoreBlockListener blockListener = new MinecartManiaCoreBlockListener();
	public final MinecartManiaCoreWorldListener worldListener = new MinecartManiaCoreWorldListener();
	public static Logger log;
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile description;
	public static File data;
	public static String dataDirectory = "plugins" + File.separator + "MinecartMania";
	
	

	public void onEnable(){
		server = this.getServer();
		description = this.getDescription();
		instance = this;
		log = Logger.getLogger("Minecraft");
		data = getDataFolder();
		String path = data.getPath();
		path = path.replace("plugins"+File.separatorChar+"MinecartManiaCore", "");
		if (!path.isEmpty()) {
			dataDirectory = path + dataDirectory;
		}
		Configuration.loadConfiguration(description, SettingList.config);

		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_UPDATE, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_COLLISION_ENTITY, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DAMAGE, listener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, listener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.CHUNK_UNLOADED, worldListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (commandLabel.contains("reloadconfig")) {
			Configuration.loadConfiguration(description, SettingList.config);
		}
		return true;
	}
}
