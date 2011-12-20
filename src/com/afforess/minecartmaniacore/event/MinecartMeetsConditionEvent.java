package com.afforess.minecartmaniacore.event;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;

public class MinecartMeetsConditionEvent extends MinecartManiaEvent {
    private static final long serialVersionUID = -8654123734496448310L;
    private final MinecartManiaMinecart minecart;
    private final Sign sign;
    private boolean condition = false;
    
    public MinecartMeetsConditionEvent(final MinecartManiaMinecart minecart, final Sign sign) {
        super("MinecartMeetsConditionEvent");
        this.minecart = minecart;
        this.sign = sign;
    }
    
    public MinecartManiaMinecart getMinecart() {
        return minecart;
    }
    
    public Sign getSign() {
        return sign;
    }
    
    public boolean isMeetCondition() {
        return condition;
    }
    
    public void setMeetCondition(final boolean condition) {
        this.condition = condition;
    }
    
}
