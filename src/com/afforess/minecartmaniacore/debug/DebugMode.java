package com.afforess.minecartmaniacore.debug;

public enum DebugMode {
	DEBUG,
	NORMAL,
	SEVERE,
	NONE;
	
	public static DebugMode debugModeFromString(String s) {
		for (DebugMode m: DebugMode.values()) {
			if (m.name().equalsIgnoreCase(s)) {
				return m;
			}
		}
		return null;
	}
}
