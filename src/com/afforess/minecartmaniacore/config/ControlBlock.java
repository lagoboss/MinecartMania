package com.afforess.minecartmaniacore.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.SpecificMaterial;

public class ControlBlock {
    
    private ItemMatcher[] matchers = null;
    private List<SpeedMultiplier> multipliers = new ArrayList<SpeedMultiplier>();
    private boolean catcher = false;
    private RedstoneState catcherState = RedstoneState.Default;
    private double launchSpeed = 0D;
    private RedstoneState launcherState = RedstoneState.Default;
    private boolean ejector = false;
    private RedstoneState ejectorState = RedstoneState.Default;
    private boolean platform = false;
    private RedstoneState platformState = RedstoneState.Default;
    private double platformRange = 4.0;
    private boolean station = false;
    private RedstoneState stationState = RedstoneState.Default;
    private boolean spawnMinecart = false;
    private RedstoneState spawnState = RedstoneState.Default;
    private boolean killMinecart = false;
    private RedstoneState killState = RedstoneState.Default;
    private boolean elevator = false;
    private RedstoneState elevatorState = RedstoneState.Default;
    public boolean updateToPoweredRail = false; //temporary, remove for MC 1.6
    private double ejectY = 0;
    
    public ControlBlock() {
    }
    
    public ItemMatcher[] getMatchers() {
        return matchers;
    }
    
    public List<SpeedMultiplier> getSpeedMultipliers() {
        return multipliers;
    }
    
    protected void setSpeedMultipliers(final List<SpeedMultiplier> list) {
        multipliers = list;
    }
    
    public boolean isCatcherBlock() {
        return catcher;
    }
    
    protected void setCatcherBlock(final boolean val) {
        catcher = val;
    }
    
    public RedstoneState getCatcherState() {
        return catcherState;
    }
    
    protected void setCatcherState(final RedstoneState state) {
        catcherState = state;
    }
    
    public double getLauncherSpeed() {
        return launchSpeed;
    }
    
    protected void setLauncherSpeed(final double d) {
        launchSpeed = d;
    }
    
    protected void setLauncherState(final RedstoneState launcherState) {
        this.launcherState = launcherState;
    }
    
    public RedstoneState getLauncherState() {
        return launcherState;
    }
    
    public boolean isEjectorBlock() {
        return ejector;
    }
    
    protected void setEjectorBlock(final boolean val) {
        ejector = val;
    }
    
    protected void setEjectorState(final RedstoneState ejectorState) {
        this.ejectorState = ejectorState;
    }
    
    public RedstoneState getEjectorState() {
        return ejectorState;
    }
    
    public boolean isPlatformBlock() {
        return platform;
    }
    
    protected void setPlatformBlock(final boolean val) {
        platform = val;
    }
    
    protected void setPlatformState(final RedstoneState platformState) {
        this.platformState = platformState;
    }
    
    public RedstoneState getPlatformState() {
        return platformState;
    }
    
    public double getPlatformRange() {
        return platformRange;
    }
    
    protected void setPlatformRange(final double range) {
        platformRange = range;
    }
    
    public double getEjectY() {
        return ejectY;
    }
    
    protected void setEjectY(final double ejectY) {
        this.ejectY = ejectY;
    }
    
    public boolean isStationBlock() {
        return station;
    }
    
    protected void setStationBlock(final boolean val) {
        station = val;
    }
    
    protected void setStationState(final RedstoneState stationState) {
        this.stationState = stationState;
    }
    
    public RedstoneState getStationState() {
        return stationState;
    }
    
    public boolean isSpawnMinecart() {
        return spawnMinecart;
    }
    
    protected void setSpawnMinecart(final boolean val) {
        spawnMinecart = val;
    }
    
    protected void setSpawnState(final RedstoneState spawnState) {
        this.spawnState = spawnState;
    }
    
    public RedstoneState getSpawnState() {
        return spawnState;
    }
    
    public boolean isKillMinecart() {
        return killMinecart;
    }
    
    protected void setKillMinecart(final boolean val) {
        killMinecart = val;
    }
    
    protected void setKillState(final RedstoneState killState) {
        this.killState = killState;
    }
    
    public RedstoneState getKillState() {
        return killState;
    }
    
    public boolean isElevatorBlock() {
        return elevator;
    }
    
    protected void setElevatorBlock(final boolean val) {
        elevator = val;
    }
    
    protected void setElevatorState(final RedstoneState elevatorState) {
        this.elevatorState = elevatorState;
    }
    
    public RedstoneState getElevatorState() {
        return elevatorState;
    }
    
    @Override
    public String toString() {
        return "[" + matchers[0].toString() + ":" + isCatcherBlock() + ":" + getLauncherSpeed() + ":" + isEjectorBlock() + ":" + isPlatformBlock() + ":" + isStationBlock() + "]";
    }
    
    public void setMatchers(final ItemMatcher[] matchers) {
        this.matchers = matchers;
    }
    
    public boolean match(final SpecificMaterial mat) {
        final ItemStack is = new ItemStack(mat.id, 1, (short) mat.durability);
        for (final ItemMatcher matcher : matchers) {
            if (!matcher.match(is))
                return false;
        }
        return true;
    }
}
