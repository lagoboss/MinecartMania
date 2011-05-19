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
		time(s, true);
	}
	public void time(String s, boolean toConsole) {
		if (mode == DebugMode.TIMER) {
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
	}
	
	public void debug(String s) {
		debug(s, true);
	}
	
	public void debug(String s, boolean toConsole) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.TIMER) {
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
		queue(s);
	}
	
	public void log(String s) {
		log(s, true);
	}
	
	public void log(String s, boolean toConsole) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.TIMER) {
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
		queue(s);
	}
	
	public void info(String s) {
		log(s, true);
	}
	
	public void severe(String s) {
		severe(s, true);
	}
	
	public void severe(String s, boolean toConsole) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.SEVERE || mode == DebugMode.TIMER) {
			if (toConsole)
				log.severe(prefix + s);
			else
				queue(prefix + s);
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
