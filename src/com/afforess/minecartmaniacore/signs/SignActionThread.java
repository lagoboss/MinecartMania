package com.afforess.minecartmaniacore.signs;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SignActionThread extends Thread {
    protected MinecartManiaMinecart minecart;
    protected SignAction action;
    
    protected SignActionThread(final MinecartManiaMinecart minecart, final SignAction action) {
        this.minecart = minecart;
        this.action = action;
    }
    
    @Override
    public void start() {
        action.execute(minecart);
    }
    
}
