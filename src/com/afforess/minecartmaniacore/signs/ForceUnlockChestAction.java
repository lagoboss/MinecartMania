package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class ForceUnlockChestAction implements SignAction {
    protected boolean valid = false;
    
    public ForceUnlockChestAction(final Sign sign) {
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        return valid;
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
        boolean success = false;
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains("unlock chest")) {
                success = true;
                break;
            }
        }
        if (success) {
            sign.addBrackets();
        }
        valid = success;
        return success;
    }
    
    public String getName() {
        return "forceunlockchestsign";
    }
    
    public String getFriendlyName() {
        return "Force Unlock Chest Sign";
    }
}
