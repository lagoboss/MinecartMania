package com.afforess.minecartmaniacore.event;

import org.akrieger.Nethrar.NethrarMinecartTeleportEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

import de.luricos.bukkit.WormholeXTreme.Wormhole.events.StargateMinecartTeleportEvent;

public class MinecartManiaListener implements Listener {
    
    public MinecartManiaListener() {
        
    }
    
    /**
     * Called when a minecart attempts to perform any action. Only occurs after a minecart changes position
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartActionEvent(final MinecartActionEvent event) {
        
    }
    
    /**
     * Called when the internal clock of a minecart is updated (each second)
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartTimeEvent(final MinecartTimeEvent event) {
        
    }
    
    /**
     * Called when a minecart that was not moving last tick began moving this tick
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartMotionStartEvent(final MinecartMotionStartEvent event) {
        
    }
    
    /**
     * Called when a minecart that was moving last tick stopped moving this tick
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartMotionStopEvent(final MinecartMotionStopEvent event) {
        
    }
    
    /**
     * Called when a minecart reaches an intersection An intersection is considered any point where there is at least 3 minecart tracks intersecting, one forward, one backward, one perpendicular.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartIntersectionEvent(final MinecartIntersectionEvent event) {
        
    }
    
    /**
     * Called when the redstone power state adjacent to any chest changes.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestPoweredEvent(final ChestPoweredEvent event) {
        
    }
    
    /**
     * Called when a MinecartManiaMinecart is destroyed
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartManiaMinecartDestroyedEvent(final MinecartManiaMinecartDestroyedEvent event) {
        
    }
    
    /**
     * Called when a MinecartManiaMinecart is created. This is not nessecary the same as when the generic type Minecart is created.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartManiaMinecartCreatedEvent(final MinecartManiaMinecartCreatedEvent event) {
        
    }
    
    /**
     * Called when a minecart is being launched by a launcher block, can be overriden
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartLaunchedEvent(final MinecartLaunchedEvent event) {
        
    }
    
    /**
     * Called when a chest attempts to spawn a minecart. Can be cancelled, or the location changed.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestSpawnMinecartEvent(final ChestSpawnMinecartEvent event) {
        
    }
    
    /**
     * Called when a player inside of a minecart tap on the minecart from the inside
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartClickedEvent(final MinecartClickedEvent event) {
        
    }
    
    /**
     * Called when a minecart is caught by a catcher block
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartCaughtEvent(final MinecartCaughtEvent event) {
        
    }
    
    /**
     * Called when a minecart is moved by an elevator block
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartElevatorEvent(final MinecartElevatorEvent event) {
        
    }
    
    /**
     * Called when a minecart changes it's direction of motion
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartDirectionChangeEvent(final MinecartDirectionChangeEvent event) {
        
    }
    
    /**
     * Called when a minecart's speed will be altered by a speed multipler block.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartSpeedMultiplierEvent(final MinecartSpeedMultiplierEvent event) {
        
    }
    
    /**
     * Called when a minecart's will be killed by a minecart kill block
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartKillEvent(final MinecartKillEvent event) {
        
    }
    
    /**
     * Called when a minecart's will be spawned by a minecart spawner block
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartSpawnEvent(final MinecartSpawnEvent event) {
        
    }
    
    /**
     * Called when a sign has been found for the first time
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartManiaSignFoundEvent(final MinecartManiaSignFoundEvent event) {
        
    }
    
    /**
     * Called when a minecart meets a specific station condition on a sign
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartMeetConditionEvent(final MinecartMeetsConditionEvent event) {
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartPassengerEjectEvent(final MinecartPassengerEjectEvent event) {
        
    }
    
    /**
     * Avoid using.
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomEvent(final Event event) {
        //Special case
        if (MinecartManiaCore.isWormholeXTremeEnabled()) {
            try {
                if (event instanceof StargateMinecartTeleportEvent) {
                    final StargateMinecartTeleportEvent e = (StargateMinecartTeleportEvent) event;
                    final MinecartManiaMinecart oldMinecart = MinecartManiaWorld.getMinecartManiaMinecart(e.getOldMinecart());
                    oldMinecart.copy(e.getNewMinecart());
                    oldMinecart.kill(false);
                    return;
                }
            } catch (final Exception e) {
            }
        }
        if (MinecartManiaCore.isNethrarEnabled()) {
            try {
                if (event instanceof NethrarMinecartTeleportEvent) {
                    final NethrarMinecartTeleportEvent e = (NethrarMinecartTeleportEvent) event;
                    final MinecartManiaMinecart oldMinecart = MinecartManiaWorld.getMinecartManiaMinecart(e.getOldCart());
                    oldMinecart.copy(e.getNewCart());
                    oldMinecart.kill(false);
                    return;
                }
            } catch (final Exception e) {
            }
        }
        
        if (event instanceof MinecartActionEvent) {
            onMinecartActionEvent((MinecartActionEvent) event);
        } else if (event instanceof MinecartTimeEvent) {
            onMinecartTimeEvent((MinecartTimeEvent) event);
        } else if (event instanceof MinecartIntersectionEvent) {
            onMinecartIntersectionEvent((MinecartIntersectionEvent) event);
        } else if (event instanceof MinecartMotionStartEvent) {
            onMinecartMotionStartEvent((MinecartMotionStartEvent) event);
        } else if (event instanceof MinecartMotionStopEvent) {
            onMinecartMotionStopEvent((MinecartMotionStopEvent) event);
        } else if (event instanceof ChestPoweredEvent) {
            onChestPoweredEvent((ChestPoweredEvent) event);
        } else if (event instanceof MinecartManiaMinecartDestroyedEvent) {
            onMinecartManiaMinecartDestroyedEvent((MinecartManiaMinecartDestroyedEvent) event);
        } else if (event instanceof MinecartLaunchedEvent) {
            onMinecartLaunchedEvent((MinecartLaunchedEvent) event);
        } else if (event instanceof ChestSpawnMinecartEvent) {
            onChestSpawnMinecartEvent((ChestSpawnMinecartEvent) event);
        } else if (event instanceof MinecartManiaMinecartCreatedEvent) {
            onMinecartManiaMinecartCreatedEvent((MinecartManiaMinecartCreatedEvent) event);
        } else if (event instanceof MinecartClickedEvent) {
            onMinecartClickedEvent((MinecartClickedEvent) event);
        } else if (event instanceof MinecartCaughtEvent) {
            onMinecartCaughtEvent((MinecartCaughtEvent) event);
        } else if (event instanceof MinecartElevatorEvent) {
            onMinecartElevatorEvent((MinecartElevatorEvent) event);
        } else if (event instanceof MinecartDirectionChangeEvent) {
            onMinecartDirectionChangeEvent((MinecartDirectionChangeEvent) event);
        } else if (event instanceof MinecartManiaSignFoundEvent) {
            onMinecartManiaSignFoundEvent((MinecartManiaSignFoundEvent) event);
        } else if (event instanceof MinecartMeetsConditionEvent) {
            onMinecartMeetConditionEvent((MinecartMeetsConditionEvent) event);
        } else if (event instanceof MinecartPassengerEjectEvent) {
            onMinecartPassengerEjectEvent((MinecartPassengerEjectEvent) event);
        }
    }
    
}
