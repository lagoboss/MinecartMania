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
	
	public void time(String s, Object ...args) {
		timeCore(s, true);
	}
	
	public void timeCore(String s, boolean toConsole, Object ...args) {
		if (mode == DebugMode.TIMER) {
			s = String.format(s, args);
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
	}
	
	public void debug(String s, Object ...args) {
		debugCore(s, true);
	}
	
	public void debugCore(String s, boolean toConsole, Object ...args) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.TIMER) {
			s = String.format(s, args);
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
		queue(s);
	}
	
	public void log(String s, Object ...args) {
		logCore(s, true, args);
	}
	
	public void logCore(String s, boolean toConsole, Object ...args) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.TIMER) {
			if (toConsole)
				log.info(prefix + s);
			else
				queue(prefix + s);
		}
		queue(s);
	}
	
	public void info(String s, Object ...args) {
		logCore(s, true, args);
	}
	
	public void severe(String s, Object ...args) {
		severeCore(s, true, args);
	}
	
	public void severeCore(String s, boolean toConsole, Object ...args) {
		if (mode == DebugMode.DEBUG || mode == DebugMode.NORMAL || mode == DebugMode.SEVERE || mode == DebugMode.TIMER) {
			s = String.format(s, args);
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
