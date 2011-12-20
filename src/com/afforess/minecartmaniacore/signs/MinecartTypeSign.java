package com.afforess.minecartmaniacore.signs;

import org.bukkit.Material;

import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;

public class MinecartTypeSign extends MinecartManiaSign {
    protected boolean standard = false;
    protected boolean powered = false;
    protected boolean storage = false;
    protected boolean calculated = false;
    
    public MinecartTypeSign(final Sign sign) {
        super(sign.getBlock());
        sign.copy(this);
    }
    
    public boolean canDispenseMinecartType(final Material item) {
        if (!calculated) {
            for (final String line : lines) {
                if (line.toLowerCase().contains("empty") || line.toLowerCase().contains("standard")) {
                    standard = true;
                } else if (line.toLowerCase().contains("powered")) {
                    powered = true;
                } else if (line.toLowerCase().contains("storage")) {
                    storage = true;
                }
            }
            calculated = true;
        }
        if (item == Material.MINECART)
            return standard;
        if (item == Material.POWERED_MINECART)
            return powered;
        if (item == Material.STORAGE_MINECART)
            return storage;
        return false;
    }
    
    @Override
    public void update(final org.bukkit.block.Sign sign) {
        calculated = false;
        super.update(sign);
    }
    
    @Override
    public void copy(final Sign sign) {
        if (sign instanceof MinecartTypeSign) {
            ((MinecartTypeSign) sign).calculated = calculated;
            ((MinecartTypeSign) sign).standard = standard;
            ((MinecartTypeSign) sign).powered = powered;
            ((MinecartTypeSign) sign).storage = storage;
        }
        super.copy(sign);
    }
    
    public static boolean isMinecartTypeSign(final Sign sign) {
        MinecartManiaLogger.getInstance().debug("Testing Sign For Minecart Type Sign, Line 0: " + sign.getLine(0));
        if (sign.getLine(0).contains("[Dispenser]")) {
            sign.setLine(0, "minecart type");
            sign.addBrackets();
            return true;
        }
        if (sign.getLine(0).toLowerCase().contains("minecart type")) {
            sign.setLine(0, "[Minecart Type]");
            MinecartManiaLogger.getInstance().debug("Found valid Minecart Type Sign");
            return true;
        }
        return false;
    }
    
}
