package com.afforess.minecartmaniacore.event;

import org.bukkit.event.Listener;

public class MinecartManiaListener implements Listener {
    
    public MinecartManiaListener() {
        
    }
    
    /**
     * Called when a minecart attempts to perform any action. Only occurs after a minecart changes position
     * 
     * @param event
     */
    public void onMinecartActionEvent(final MinecartActionEvent event) {
        
    }
    
    /**
     * Called when the internal clock of a minecart is updated (each second)
     * 
     * @param event
     */
    public void onMinecartTimeEvent(final MinecartTimeEvent event) {
        
    }
    
    /**
     * Called when a minecart that was not moving last tick began moving this tick
     * 
     * @param event
     */
    public void onMinecartMotionStartEvent(final MinecartMotionStartEvent event) {
        
    }
    
    /**
     * Called when a minecart that was moving last tick stopped moving this tick
     * 
     * @param event
     */
    public void onMinecartMotionStopEvent(final MinecartMotionStopEvent event) {
        
    }
    
    /**
     * Called when a minecart reaches an intersection An intersection is considered any point where there is at least 3 minecart tracks intersecting, one forward, one backward, one perpendicular.
     * 
     * @param event
     */
    public void onMinecartIntersectionEvent(final MinecartIntersectionEvent event) {
        
    }
    
    /**
     * Called when the redstone power state adjacent to any chest changes.
     * 
     * @param event
     */
    public void onChestPoweredEvent(final ChestPoweredEvent event) {
        
    }
    
    /**
     * Called when a MinecartManiaMinecart is destroyed
     * 
     * @param event
     */
    public void onMinecartManiaMinecartDestroyedEvent(final MinecartManiaMinecartDestroyedEvent event) {
        
    }
    
    /**
     * Called when a MinecartManiaMinecart is created. This is not nessecary the same as when the generic type Minecart is created.
     * 
     * @param event
     */
    public void onMinecartManiaMinecartCreatedEvent(final MinecartManiaMinecartCreatedEvent event) {
        
    }
    
    /**
     * Called when a minecart is being launched by a launcher block, can be overriden
     * 
     * @param event
     */
    public void onMinecartLaunchedEvent(final MinecartLaunchedEvent event) {
        
    }
    
    /**
     * Called when a chest attempts to spawn a minecart. Can be cancelled, or the location changed.
     * 
     * @param event
     */
    public void onChestSpawnMinecartEvent(final ChestSpawnMinecartEvent event) {
        
    }
    
    /**
     * Called when a player inside of a minecart tap on the minecart from the inside
     * 
     * @param event
     */
    public void onMinecartClickedEvent(final MinecartClickedEvent event) {
        
    }
    
    /**
     * Called when a minecart is caught by a catcher block
     * 
     * @param event
     */
    public void onMinecartCaughtEvent(final MinecartCaughtEvent event) {
        
    }
    
    /**
     * Called when a minecart is moved by an elevator block
     * 
     * @param event
     */
    public void onMinecartElevatorEvent(final MinecartElevatorEvent event) {
        
    }
    
    /**
     * Called when a minecart changes it's direction of motion
     * 
     * @param event
     */
    public void onMinecartDirectionChangeEvent(final MinecartDirectionChangeEvent event) {
        
    }
    
    /**
     * Called when a minecart's speed will be altered by a speed multipler block.
     * 
     * @param event
     */
    public void onMinecartSpeedMultiplierEvent(final MinecartSpeedMultiplierEvent event) {
        
    }
    
    /**
     * Called when a minecart's will be killed by a minecart kill block
     * 
     * @param event
     */
    public void onMinecartKillEvent(final MinecartKillEvent event) {
        
    }
    
    /**
     * Called when a minecart's will be spawned by a minecart spawner block
     * 
     * @param event
     */
    public void onMinecartSpawnEvent(final MinecartSpawnEvent event) {
        
    }
    
    /**
     * Called when a sign has been found for the first time
     * 
     * @param event
     */
    public void onMinecartManiaSignFoundEvent(final MinecartManiaSignFoundEvent event) {
        
    }
    
    /**
     * Called when a minecart meets a specific station condition on a sign
     * 
     * @param event
     */
    public void onMinecartMeetConditionEvent(final MinecartMeetsConditionEvent event) {
        
    }
    
    public void onMinecartPassengerEjectEvent(final MinecartPassengerEjectEvent event) {
        
    }
    
    /*
     * @EventHandler(priority = EventPriority.NORMAL) public void onWormholeExtremeEvent(final StargateMinecartTeleportEvent event) { //Special case if (MinecartManiaCore.isWormholeXTremeEnabled()) { try { final StargateMinecartTeleportEvent e = event; final MinecartManiaMinecart oldMinecart = MinecartManiaWorld.getMinecartManiaMinecart(e.getOldMinecart()); oldMinecart.copy(e.getNewMinecart()); oldMinecart.kill(false); return; } catch (final Exception e) { } } }
     */
    // TODO:  Causes a shitton of errors if Nethrar is not installed (worked before events system overhaul)
    // If you can fix this, please submit a pull.
    //    @EventHandler(priority = EventPriority.NORMAL)
    //    public void onNethrarEvent(final NethrarMinecartTeleportEvent event) {
    //        if (MinecartManiaCore.isNethrarEnabled()) {
    //            try {
    //                if (event instanceof NethrarMinecartTeleportEvent) {
    //                    final NethrarMinecartTeleportEvent e = event;
    //                    final MinecartManiaMinecart oldMinecart = MinecartManiaWorld.getMinecartManiaMinecart(e.getOldCart());
    //                    oldMinecart.copy(e.getNewCart());
    //                    oldMinecart.kill(false);
    //                    return;
    //                }
    //            } catch (final Exception e) {
    //            }
    //        }
    //    }
    
}
