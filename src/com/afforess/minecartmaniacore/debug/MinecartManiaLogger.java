package com.afforess.minecartmaniacore.debug;

import java.util.logging.Logger;

public class MinecartManiaLogger {
	protected final Logger log = Logger.getLogger("Minecraft");
	protected static final String prefix = "[Minecart Mania] ";
	protected DebugMode mode;
	protected static MinecartManiaLogger instance = null;
	
	protected MinecartManiaLogger(DebugMode mode) {
		this.mode = mode;
	}
	
	public static MinecartManiaLogger getInstance() {
		if (instance == null) {
			instance = new MinecartManiaLogger(DebugMode.NORMAL);
		}
		return instance;
	}
	
	public void time(String s) {
		if (mode == DebugMode.TIMER) {
			log.info(prefix + s);
		}
	}
	
	public void debug(String s) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.TIMER) {
			log.info(prefix + s);
		}
	}
	
	public void log(String s) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.TIMER) {
			log.info(prefix + s);
		}
	}
	
	public void info(String s) {
		log(s);
	}
	
	public void severe(String s) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.SEVERE || mode == DebugMode.TIMER) {
			log.severe(prefix + s);
		}
	}
	
	public void switchDebugMode(DebugMode mode) {
		this.mode = mode;
		log("Debug mode switched to " + mode.name());
	}
}
