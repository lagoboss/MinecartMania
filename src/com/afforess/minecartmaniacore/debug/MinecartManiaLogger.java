package com.afforess.minecartmaniacore.debug;

import java.util.LinkedList;
import java.util.logging.Logger;

public class MinecartManiaLogger {
	protected final Logger log = Logger.getLogger("Minecraft");
	protected static final String prefix = "[Minecart Mania] ";
	protected DebugMode mode;
	protected static MinecartManiaLogger instance = null;
	private LinkedList<String> queuedLog = new LinkedList<String>();
	
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
		queue(s);
	}
	
	public void log(String s) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.TIMER) {
			log.info(prefix + s);
		}
		queue(s);
	}
	
	public void info(String s) {
		log(s);
	}
	
	public void severe(String s) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.SEVERE || mode == DebugMode.TIMER) {
			log.severe(prefix + s);
		}
		queue(s);
	}
	
	public void switchDebugMode(DebugMode mode) {
		this.mode = mode;
		log("Debug mode switched to " + mode.name());
	}
	
	private final void queue(String log) {
		queuedLog.add(log);
		if (queuedLog.size() > 100) {
			LogWriter writer = new LogWriter(queuedLog);
			queuedLog = new LinkedList<String>();
			writer.start();
		}
	}
}
