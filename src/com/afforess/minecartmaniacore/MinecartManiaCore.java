package com.afforess.minecartmaniacore;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.PersistenceException;

import org.akrieger.Nethrar.NethrarMinecartTeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.config.CoreSettingParser;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecartDataTable;
import com.afforess.minecartmaniacore.minecart.MinecartOwner;
import com.afforess.minecartmaniacore.world.Item;

import de.luricos.bukkit.WormholeXTreme.Wormhole.events.StargateMinecartTeleportEvent;

public class MinecartManiaCore extends JavaPlugin {
    
    public static final MinecartManiaCoreListener listener = new MinecartManiaCoreListener();
    //    public static final MinecartManiaCoreBlockListener blockListener = new MinecartManiaCoreBlockListener();
    //    public static final MinecartManiaCoreWorldListener worldListener = new MinecartManiaCoreWorldListener();
    //    public static final MinecartManiaActionListener actionListener = new MinecartManiaActionListener();
    //    public static final MinecartManiaCorePlayerListener playerListener = new MinecartManiaCorePlayerListener();
    public static MinecartManiaLogger log = MinecartManiaLogger.getInstance();
    private static Plugin instance;
    private static PluginDescriptionFile description;
    private static File data;
    private static File minecartManiaCore;
    private static String dataDirectory = "plugins" + File.separator + "MinecartMania";
    private static boolean wormholeXTreme = false;
    private static boolean nethrar = false;
    private static boolean lockette = false;
    private static boolean lwc = false;
    
    private static final int DATABASE_VERSION = 3;
    
    @Override
    public void onLoad() {
        setNaggable(false);
    }
    
    public void onEnable() {
        description = getDescription();
        instance = this;
        data = getDataFolder();
        minecartManiaCore = getFile();
        
        //manage external plugins
        wormholeXTreme = getServer().getPluginManager().getPlugin("WormholeXTreme") != null;
        nethrar = getServer().getPluginManager().getPlugin("Nethrar") != null;
        lockette = getServer().getPluginManager().getPlugin("Lockette") != null;
        lwc = getServer().getPluginManager().getPlugin("LWC") != null;
        
        writeItemsFile();
        
        MinecartManiaConfigurationParser.read("MinecartManiaConfiguration.xml", dataDirectory, new CoreSettingParser());
        MinecartManiaConfigurationParser.read("MinecartManiaLocale.xml", dataDirectory, new LocaleParser());
        
        getServer().getPluginManager().registerEvents(listener, this);
        
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_UPDATE, listener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_COLLISION_ENTITY, listener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DAMAGE, listener, Priority.Lowest, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_DESTROY, listener, Priority.Lowest, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_ENTER, listener, Priority.Monitor, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Priority.Normal, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Monitor, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);
        //        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        
        //database setup
        final File ebeans = new File(new File(getDataFolder().getParent()).getParent(), "ebean.properties");
        if (!ebeans.exists()) {
            try {
                ebeans.createNewFile();
                final PrintWriter pw = new PrintWriter(ebeans);
                pw.append("# General logging level: (none, explicit, all)");
                pw.append('\n');
                pw.append("ebean.logging=none");
                pw.close();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        setupDatabase();
        
        log.info(description.getName() + " version " + description.getVersion() + " is enabled!");
    }
    
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        log.info(description.getName() + " version " + description.getVersion() + " is disabled!");
    }
    
    private void writeItemsFile() {
        try {
            final File items = new File(dataDirectory + File.separator + "items.txt");
            final PrintWriter pw = new PrintWriter(items);
            pw.append("This file is a list of all the data values, and matching item names for Minecart Mania. \nThis list is never used, and changes made to this file will be ignored");
            pw.append("\n");
            pw.append("\n");
            pw.append("Items:");
            pw.append("\n");
            for (final Item item : Item.values()) {
                final String name = "Item Name: " + item.toString();
                pw.append(name);
                String id = "";
                for (int i = name.length() - 1; i < 40; i++) {
                    id += " ";
                }
                pw.append(id);
                id = "Item Id: " + String.valueOf(item.getId());
                pw.append(id);
                String data = "";
                for (int i = id.length() - 1; i < 15; i++) {
                    data += " ";
                }
                data += "Item Data: " + String.valueOf(item.getData());
                pw.append(data);
                pw.append("\n");
            }
            pw.close();
        } catch (final Exception e) {
        }
    }
    
    private int getDatabaseVersion() {
        try {
            getDatabase().find(MinecartOwner.class).findRowCount();
        } catch (final PersistenceException ex) {
            return 0;
        }
        try {
            getDatabase().find(MinecartManiaMinecartDataTable.class).findRowCount();
        } catch (final PersistenceException ex) {
            return 1;
        }
        try {
            getDatabase().find(MinecartManiaMinecartDataTable.class).findList();
        } catch (final PersistenceException ex) {
            return 2;
        }
        return DATABASE_VERSION;
    }
    
    protected void setupInitialDatabase() {
        try {
            getDatabase().find(MinecartOwner.class).findRowCount();
            getDatabase().find(MinecartManiaMinecartDataTable.class).findRowCount();
        } catch (final PersistenceException ex) {
            log.info("Installing database");
            installDDL();
        }
    }
    
    protected void setupDatabase() {
        final int version = getDatabaseVersion();
        switch (version) {
            case 0:
                setupInitialDatabase();
                break;
            case 1:
                upgradeDatabase(1);
                break;
            case 2:
                upgradeDatabase(2);
                break;
            case 3: /* up to date database */
                break;
        }
    }
    
    private void upgradeDatabase(final int current) {
        log.info(String.format("Upgrading database from version %d to version %d", current, DATABASE_VERSION));
        if ((current == 1) || (current == 2)) {
            removeDDL();
            setupInitialDatabase();
        }
        /*
         * Add additional versions here
         */
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(MinecartOwner.class);
        list.add(MinecartManiaMinecartDataTable.class);
        return list;
    }
    
    public static MinecartManiaCore getInstance() {
        return (MinecartManiaCore) instance;
    }
    
    public static PluginDescriptionFile getPluginDescription() {
        return description;
    }
    
    public static File getPluginDataFolder() {
        return data;
    }
    
    public static File getMinecartManiaCoreJar() {
        return minecartManiaCore;
    }
    
    public static String getDataDirectoryRelativePath() {
        return dataDirectory;
    }
    
    public static boolean isWormholeXTremeEnabled() {
        return wormholeXTreme;
    }
    
    public static boolean isNethrarEnabled() {
        return nethrar;
    }
    
    public static boolean isLocketteEnabled() {
        return lockette;
    }
    
    public static boolean isLWCEnabled() {
        return lwc;
    }
    
    public static void callEvent(final Event event) {
        if (event instanceof NethrarMinecartTeleportEvent) {
            listener.onNethrarEvent((NethrarMinecartTeleportEvent) event);
        } else if (event instanceof StargateMinecartTeleportEvent) {
            listener.onWormholeExtremeEvent((StargateMinecartTeleportEvent) event);
        }
        //now everyone else goes
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
    
    public static Entity findEntity(final UUID owner) {
        for (final World w : getInstance().getServer().getWorlds()) {
            for (final Entity e : w.getEntities()) {
                if (e.getUniqueId().equals(owner))
                    return e;
            }
        }
        return null;
    }
}
