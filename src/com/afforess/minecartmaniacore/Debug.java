package com.afforess.minecartmaniacore;

public abstract class Debug {
	
	private static boolean log = false;
	private static final String prefix = "[DEBUG] ";
	public static void log(String s){ 
		if (log)
			MinecartManiaCore.log.info(prefix+s);
	}
	
	public static void warn(String s){ 
		if (log)
			MinecartManiaCore.log.warning(prefix+s);
	}
	
	public static void severe(String s){ 
		if (log)
			MinecartManiaCore.log.severe(prefix+s);
	}

}
