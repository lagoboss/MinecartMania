package com.afforess.minecartmaniacore.minecart;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "MinecartManiaMinecartDataTable")
public class MinecartManiaMinecartDataTable {
    private transient static HashMap<String, MinecartManiaMinecartDataTable> cache = new HashMap<String, MinecartManiaMinecartDataTable>();
    @NotNull
    protected double previousX;
    @NotNull
    protected double previousY;
    @NotNull
    protected double previousZ;
    @NotNull
    protected double previousMotionX;
    @NotNull
    protected double previousMotionY;
    @NotNull
    protected double previousMotionZ;
    @NotNull
    protected CompassDirection previousFacingDir;
    @NotNull
    protected boolean wasMovingLastTick;
    @NotNull
    protected String owner;
    @NotNull
    protected int myrange;
    @NotNull
    protected int rangeY;
    @NotNull
    protected boolean dead;
    @NotNull
    protected double X;
    @NotNull
    protected double Y;
    @NotNull
    protected double Z;
    @NotNull
    protected double motionX;
    @NotNull
    protected double motionY;
    @NotNull
    protected double motionZ;
    @NotNull
    protected int typeId;
    @NotNull
    protected String world;
    @NotNull
    protected String player;
    @Id
    protected int oldId;
    protected transient ConcurrentHashMap<String, Object> data = null;
    
    public MinecartManiaMinecartDataTable() {
        
    }
    
    public MinecartManiaMinecartDataTable(final MinecartManiaMinecart minecart, final String player) {
        previousX = minecart.previousLocation.getX();
        previousY = minecart.previousLocation.getY();
        previousZ = minecart.previousLocation.getZ();
        previousMotionX = minecart.previousMotion.getX();
        previousMotionY = minecart.previousMotion.getY();
        previousMotionZ = minecart.previousMotion.getZ();
        previousFacingDir = minecart.previousFacingDir;
        wasMovingLastTick = minecart.wasMovingLastTick;
        owner = minecart.owner.getOwner();
        myrange = minecart.range;
        rangeY = minecart.rangeY;
        dead = minecart.dead;
        oldId = minecart.minecart.getEntityId();
        data = minecart.data;
        X = minecart.getLocation().getX();
        Y = minecart.getLocation().getY();
        Z = minecart.getLocation().getZ();
        motionX = minecart.getMotionX();
        motionY = minecart.getMotionY();
        motionZ = minecart.getMotionZ();
        this.player = player;
        typeId = minecart.getType().getId();
        world = minecart.getWorld().getName();
    }
    
    public static MinecartManiaMinecartDataTable getDataTable(final String player) {
        if (cache.containsKey(player))
            return cache.get(player);
        try {
            MinecartManiaMinecartDataTable data = null;
            final List<MinecartManiaMinecartDataTable> list = MinecartManiaCore.getInstance().getDatabase().find(MinecartManiaMinecartDataTable.class).where().ieq("player", player).findList();
            if (list.size() > 0) {
                data = list.get(0);
                //handle issues with the db gracefully
                if (list.size() > 1) {
                    for (int i = 1; i < list.size(); i++) {
                        MinecartManiaCore.getInstance().getDatabase().delete(list.get(i));
                    }
                }
            }
            cache.put(player, data);
            return data;
        } catch (final Exception e) {
            MinecartManiaLogger.getInstance().severe("Failed to load the minecart from memory when " + player + " reconnected");
            MinecartManiaLogger.getInstance().log(e.getMessage(), false);
            return null;
        }
    }
    
    public static void delete(final MinecartManiaMinecartDataTable data) {
        MinecartManiaCore.getInstance().getDatabase().delete(data);
        cache.remove(data.getPlayer());
    }
    
    public static void save(final MinecartManiaMinecartDataTable data) {
        MinecartManiaCore.getInstance().getDatabase().save(data);
        cache.put(data.getPlayer(), data);
    }
    
    public MinecartManiaMinecart toMinecartManiaMinecart() {
        final MinecartManiaMinecart minecart = MinecartManiaWorld.spawnMinecart(getLocation(), Material.getMaterial(typeId), owner);
        minecart.previousFacingDir = previousFacingDir;
        minecart.previousLocation = getPreviousLocation();
        minecart.previousMotion = getPreviousMotion();
        minecart.minecart.setVelocity(getMotion());
        minecart.range = myrange;
        minecart.rangeY = rangeY;
        minecart.wasMovingLastTick = wasMovingLastTick;
        minecart.dead = dead;
        if (data != null) {
            minecart.data = data;
        }
        return minecart;
    }
    
    public double getPreviousX() {
        return previousX;
    }
    
    public void setPreviousX(final double previousX) {
        this.previousX = previousX;
    }
    
    public double getPreviousY() {
        return previousY;
    }
    
    public void setPreviousY(final double previousY) {
        this.previousY = previousY;
    }
    
    public double getPreviousZ() {
        return previousZ;
    }
    
    public void setPreviousZ(final double previousZ) {
        this.previousZ = previousZ;
    }
    
    public double getPreviousMotionX() {
        return previousMotionX;
    }
    
    public void setPreviousMotionX(final double previousMotionX) {
        this.previousMotionX = previousMotionX;
    }
    
    public double getPreviousMotionY() {
        return previousMotionY;
    }
    
    public void setPreviousMotionY(final double previousMotionY) {
        this.previousMotionY = previousMotionY;
    }
    
    public double getPreviousMotionZ() {
        return previousMotionZ;
    }
    
    public void setPreviousMotionZ(final double previousMotionZ) {
        this.previousMotionZ = previousMotionZ;
    }
    
    public double getX() {
        return X;
    }
    
    public void setX(final double x) {
        X = x;
    }
    
    public double getY() {
        return Y;
    }
    
    public void setY(final double y) {
        Y = y;
    }
    
    public double getZ() {
        return Z;
    }
    
    public void setZ(final double z) {
        Z = z;
    }
    
    public double getMotionX() {
        return motionX;
    }
    
    public void setMotionX(final double motionX) {
        this.motionX = motionX;
    }
    
    public double getMotionY() {
        return motionY;
    }
    
    public void setMotionY(final double motionY) {
        this.motionY = motionY;
    }
    
    public double getMotionZ() {
        return motionZ;
    }
    
    public void setMotionZ(final double motionZ) {
        this.motionZ = motionZ;
    }
    
    public String getWorld() {
        return world;
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
    
    public String getPlayer() {
        return player;
    }
    
    public void setPlayer(final String player) {
        this.player = player;
    }
    
    public Vector getPreviousLocation() {
        return new Vector(previousX, previousY, previousZ);
    }
    
    public Vector getPreviousMotion() {
        return new Vector(previousMotionX, previousMotionY, previousMotionZ);
    }
    
    public Vector getMotion() {
        return new Vector(motionX, motionY, motionZ);
    }
    
    public Location getLocation() {
        return new Location(Bukkit.getServer().getWorld(world), X, Y, Z);
    }
    
    public CompassDirection getPreviousFacingDir() {
        return previousFacingDir;
    }
    
    public void setPreviousFacingDir(final CompassDirection previousFacingDir) {
        this.previousFacingDir = previousFacingDir;
    }
    
    public boolean isWasMovingLastTick() {
        return wasMovingLastTick;
    }
    
    public void setWasMovingLastTick(final boolean wasMovingLastTick) {
        this.wasMovingLastTick = wasMovingLastTick;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public int getRangeY() {
        return rangeY;
    }
    
    public void setRangeY(final int rangeY) {
        this.rangeY = rangeY;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void setDead(final boolean dead) {
        this.dead = dead;
    }
    
    public int getOldId() {
        return oldId;
    }
    
    public void setOldId(final int oldId) {
        this.oldId = oldId;
    }
    
    public int getTypeId() {
        return typeId;
    }
    
    public void setTypeId(final int type) {
        typeId = type;
    }
    
    public void setMyrange(final int myrange) {
        this.myrange = myrange;
    }
    
    public int getMyrange() {
        return myrange;
    }
    
}
