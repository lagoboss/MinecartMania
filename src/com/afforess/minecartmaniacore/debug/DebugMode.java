package com.afforess.minecartmaniacore.debug;

public enum DebugMode {
    TIMER,
    DEBUG,
    NORMAL,
    SEVERE,
    NONE;
    
    public static DebugMode debugModeFromString(final String s) {
        for (final DebugMode m : DebugMode.values()) {
            if (m.name().equalsIgnoreCase(s))
                return m;
        }
        return null;
    }
}
