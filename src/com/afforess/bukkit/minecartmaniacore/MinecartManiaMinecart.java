package com.afforess.bukkit.minecartmaniacore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.EntityMinecart;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.util.Vector;
import com.afforess.bukkit.minecartmaniacore.DirectionUtils.CompassDirection;
import com.afforess.bukkit.minecartmaniacore.event.MinecartTimeEvent;


public class MinecartManiaMinecart {
	public final Minecart minecart;
	private static final double maxMomentum = 1E308;
	private Vector previousLocation;
	private Vector previousMotion;
	private Calendar cal;
	private DirectionUtils.CompassDirection previousFacingDir = DirectionUtils.CompassDirection.NO_DIRECTION;
	private boolean wasMovingLastTick;
	
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	
	public MinecartManiaMinecart(Minecart cart) {
		minecart = cart;
		previousMotion = cart.getVelocity().clone();
		previousLocation = cart.getLocation().toVector().clone();
		cal = Calendar.getInstance();
		setWasMovingLastTick(isMoving());
	}
	
	public Vector getPreviousLocation() {
		return previousLocation.clone();
	}
	
	public void updateLocation() {
		previousLocation = minecart.getLocation().toVector().clone();
	}
	
	public Vector getPreviousMotion() {
		return previousMotion.clone();
	}
	
	public void updateMotion() {
		previousMotion = minecart.getVelocity().clone();
	}
	
	public boolean hasChangedPosition() {
		if (getPreviousLocation().getBlockX() != minecart.getLocation().getBlockX()) {
			return true;
		}
		if (getPreviousLocation().getBlockY() != minecart.getLocation().getBlockY()) {
			return true;
		}
		if (getPreviousLocation().getBlockZ() != minecart.getLocation().getBlockZ()) {
			return true;
		}
		
		return false;
	}
	
	public double getMotionX() {
		return minecart.getVelocity().getX();
	}
	
	public double getMotionY() {
		return minecart.getVelocity().getY();
	}
	
	public double getMotionZ() {
		return minecart.getVelocity().getZ();
	}
	
	public void setMotionX(double motionX){
		motionX = MathUtils.range(motionX, maxMomentum, -maxMomentum);
		setMotion(motionX, getMotionY(), getMotionZ());
	}
	
	public void setMotionY(double motionY){
		motionY = MathUtils.range(motionY, maxMomentum, -maxMomentum);
		setMotion(getMotionX(), motionY, getMotionZ());
	}
	
	public void setMotionZ(double motionZ){
		motionZ = MathUtils.range(motionZ, maxMomentum, -maxMomentum);
		setMotion(getMotionX(), getMotionY(), motionZ);
	}
	
	private void setMotion(double motionX, double motionY, double motionZ) {
		Vector newVelocity = new Vector();
		newVelocity.setX(motionX);
		newVelocity.setY(motionY);
		newVelocity.setZ(motionZ);
		minecart.setVelocity(newVelocity);
	}
	
	public void stopCart() {
		setMotion(0D, 0D, 0D);
	}
	
	public boolean isMoving() {
		return getMotionX() != 0D || getMotionY() != 0D || getMotionZ() != 0D;
	}
	
	public int getX(){
		return minecart.getLocation().getBlockX();
	}
	
	public int getY(){
		return minecart.getLocation().getBlockY();
	}
	
	public int getZ(){
		return minecart.getLocation().getBlockZ();
	}
	
	public void setPreviousFacingDir(DirectionUtils.CompassDirection dir) {
		previousFacingDir = dir;
	}

	public DirectionUtils.CompassDirection getPreviousFacingDir() {
		return previousFacingDir;
	}
	
	/**
	 ** Returns the value from the loaded data
	 ** @param the string key the data value is associated with
	 **/
	 public Object getDataValue(String key) {
		 if (data.containsKey(key)) {
			 return data.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new data value if it does not already exists, or resets an existing value
	 ** @param the string key the data value is associated with
	 ** @param the value to store
	 **/	 
	 public void setDataValue(String key, Object value) {
		 if (value == null) {
			 data.remove(key);
		 }else {
			 data.put(key, value);
		 }
	 }
	
	public int getBlockIdBeneath() {
		return MinecartManiaWorld.getBlockAt(getX(), getY()-1, getZ()).getTypeId();
	}
	
	public boolean isPoweredBeneath() {
		if (MinecartManiaWorld.isBlockIndirectlyPowered(getX(), getY()-1, getZ()) || MinecartManiaWorld.isBlockIndirectlyPowered(getX(), getY(), getZ())) {
			return true;
		}
		//Temporary Fix for Pressure Plates, since they are NYI
		if (MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getTypeId() == Material.WOOD_PLATE.getId()) {
			return true;
		}
		if (MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getTypeId() == Material.STONE_PLATE.getId()) {
			return this.minecart.getPassenger() != null;
		}
		return false;
	}
	
	public void reverse() {
		setMotionX(getMotionX() * -1);
		setMotionY(getMotionY() * -1);
		setMotionZ(getMotionZ() * -1);
	}
	
	public boolean doReverse() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getReverseBlockId() && !isPoweredBeneath())
    	{
			reverse();
    		return true;
    	}
		return false;
	}

	public boolean doLowSpeedBrake() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getLowSpeedBrakeBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() / 2D);
    		setMotionZ(getMotionZ() / 2D);
    		return true;
    	}
		return false;
	}

	public boolean doHighSpeedBrake() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getHighSpeedBrakeBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() / 8D);
    		setMotionZ(getMotionZ() / 8D);
    		return true;
    	}
		return false;
	}

	public boolean doLowSpeedBooster() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getLowSpeedBoosterBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() * 2D);
    		setMotionZ(getMotionZ() * 2D);
    		return true;
    	}
		return false;
	}
	
	public boolean doHighSpeedBooster() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getHighSpeedBoosterBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() * 8D);
    		setMotionZ(getMotionZ() * 8D);
    		return true;
    	}
		return false;
	}

	public boolean doCatcherBlock() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getCatcherBlockId())
 		{
			if (isPoweredBeneath()) {
				if (!isMoving()) {
					launchCart();
					return true;
				}
			}
			else {
				stopCart();
				return true;
			}
		}
		return false;
	}
	
	private void launchCart() {

		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(this, 1);
		for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				if (sign.getLine(i).toLowerCase().indexOf("north") > -1) {
					if (MinecartUtils.validMinecartTrack(getX()-1, getY(), getZ(), 2, DirectionUtils.CompassDirection.NORTH)) {
						sign.setLine(i, "[North]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.NORTH, 0.6D);
						return;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("east") > -1) {
					if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()-1, 2, DirectionUtils.CompassDirection.EAST)) {
						sign.setLine(i, "[East]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.EAST, 0.6D);
						return;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("south") > -1) {
					if (MinecartUtils.validMinecartTrack(getX()+1, getY(), getZ(), 2, DirectionUtils.CompassDirection.SOUTH)) {
						sign.setLine(i, "[South]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.SOUTH, 0.6D);
						return;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("west") > -1) {
					if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()+1, 2, DirectionUtils.CompassDirection.WEST)) {
						sign.setLine(i, "[West]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.WEST, 0.6D);
						return;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("facing dir") > -1) {
					if (minecart.getPassenger() != null) {
						DirectionUtils.CompassDirection facingDir = DirectionUtils.getDirectionFromMinecartRotation((minecart.getPassenger().getLocation().getYaw() - 90.0F) % 360.0F);
						if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()+1, 2, facingDir)) {
							sign.setLine(i, "[Facing Dir]");
							sign.update();
							setMotion(facingDir, 0.6D);
							return;
						}
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("previous dir") > -1) {
					if (!this.getPreviousFacingDir().equals(DirectionUtils.CompassDirection.NO_DIRECTION)) {
						if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()+1, 2, this.getPreviousFacingDir())) {
							sign.setLine(i, "[Previous Dir]");
							sign.update();
							setMotion(this.getPreviousFacingDir(), 0.6D);
							return;
						}
					}
				}
			}
		}
		if (MinecartUtils.validMinecartTrack(getX()-1, getY(), getZ(), 2, DirectionUtils.CompassDirection.NORTH)) {
			setMotion(DirectionUtils.CompassDirection.NORTH, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()-1, 2, DirectionUtils.CompassDirection.EAST)) {
			setMotion(DirectionUtils.CompassDirection.EAST, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(getX()+1, getY(), getZ(), 2, DirectionUtils.CompassDirection.SOUTH)) {
			setMotion(DirectionUtils.CompassDirection.SOUTH, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(getX(), getY(), getZ()+1, 2, DirectionUtils.CompassDirection.WEST)) {
			setMotion(DirectionUtils.CompassDirection.WEST, 0.6D);
		}
	}

	public void setMotion(CompassDirection direction, double speed) {
		if (direction.equals(DirectionUtils.CompassDirection.NORTH))
			setMotionX(-speed);	
		else if (direction.equals(DirectionUtils.CompassDirection.SOUTH))
			setMotionX(speed);
		else if (direction.equals(DirectionUtils.CompassDirection.EAST))
			setMotionZ(-speed);	
		else if (direction.equals(DirectionUtils.CompassDirection.WEST))
			setMotionZ(speed);	
		else
			throw new IllegalArgumentException();
	}

	public DirectionUtils.CompassDirection getDirectionOfMotion()
	{
		if (getMotionX() < 0.0D) return DirectionUtils.CompassDirection.NORTH;
		if (getMotionZ() < 0.0D) return DirectionUtils.CompassDirection.EAST;
		if (getMotionX() > 0.0D) return DirectionUtils.CompassDirection.SOUTH;
		if (getMotionZ() > 0.0D) return DirectionUtils.CompassDirection.WEST;
		return DirectionUtils.CompassDirection.NO_DIRECTION;
	}
	
	public void doRealisticFriction() {
		if (minecart.getPassenger() == null && MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getType().equals(Material.RAILS)) 	{
			setMotion(getMotionX() * 1.03774, getMotionY(), getMotionZ()* 1.03774);
    	}
	}

	public boolean doEjectorBlock() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getEjectorBlockId() && !isPoweredBeneath()) {
			if (minecart.getPassenger() != null) {
				return minecart.eject();
			}
		}
		return false;
	}
	
	public boolean hasPlayerPassenger() {
		return getPlayerPassenger() != null;
	}
	
	public Player getPlayerPassenger() {
		if (minecart.getPassenger() == null) {
			return null;
		}
		if (minecart.getPassenger() instanceof Player) {
			return (Player)minecart.getPassenger();
		}
		return null;
	}
	
	public boolean isOnRails() {
		return MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()));
	}
	
	/**
	 ** Determines whether or not the track the minecart is currently on is the center piece of a large track intersection. Returns true if it is an intersection.
	 **/
	public boolean isAtIntersection() {
		if (this.isOnRails()) {
			return MinecartUtils.isAtIntersection(getX(), getY(), getZ());
		}
		return false;
	}
	
	public Block getBlockTypeAhead() {
		return DirectionUtils.getBlockTypeAhead(getDirectionOfMotion(), getX(), getY(), getZ());
	}
	
	public Block getBlockTypeBehind() {
		return DirectionUtils.getBlockTypeAhead(DirectionUtils.getOppositeDirection(getDirectionOfMotion()), getX(), getY(), getZ());
	}

	public void doPressurePlateRails() {
		if (MinecartManiaWorld.isPressurePlateRails()) {
			if (MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getType().equals(Material.STONE_PLATE)
			|| MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getType().equals(Material.WOOD_PLATE)) {
				setMotionX(getMotionX() * 3);
				setMotionZ(getMotionZ() * 3);
	    	}
			else if (getBlockTypeAhead() != null && (getBlockTypeAhead().getType().equals(Material.STONE_PLATE) || getBlockTypeAhead().getType().equals(Material.WOOD_PLATE))) {
				setDataValue("pre-ppr velocity", this.minecart.getVelocity());
			}
			else if (getBlockTypeBehind() != null && (getBlockTypeBehind().getType().equals(Material.STONE_PLATE) || getBlockTypeBehind().getType().equals(Material.WOOD_PLATE))) {
				Vector velocity = (Vector) getDataValue("pre-ppr velocity");
				if (velocity != null) {
					if (getBlockTypeBehind().getFace(BlockFace.DOWN).getTypeId() != MinecartManiaWorld.getCatcherBlockId()) {
						this.minecart.setVelocity(velocity);
					}
					setDataValue("pre-ppr velocity", null);
				}
			}
		}
	}

	public void updateCalendar() {
		Calendar current = Calendar.getInstance();
		if (cal.get(Calendar.SECOND) != current.get(Calendar.SECOND)) {
			MinecartTimeEvent e = new MinecartTimeEvent(this, cal, current);
			MinecartManiaCore.server.getPluginManager().callEvent(e);
			cal = current;
		}
	}
	
	public MinecartManiaMinecart getAdjacentMinecartFromDirection(DirectionUtils.CompassDirection direction) {
		if (direction == DirectionUtils.CompassDirection.NORTH) return MinecartManiaWorld.getMinecartManiaMinecartAt(getX()-1, getY(), getZ());
		if (direction == DirectionUtils.CompassDirection.EAST) return MinecartManiaWorld.getMinecartManiaMinecartAt(getX(), getY(), getZ()-1);
		if (direction == DirectionUtils.CompassDirection.SOUTH) return MinecartManiaWorld.getMinecartManiaMinecartAt(getX()+1, getY(), getZ());
		if (direction == DirectionUtils.CompassDirection.WEST) return MinecartManiaWorld.getMinecartManiaMinecartAt(getX(), getY(), getZ()+1);
		return null;
	}
	
	public MinecartManiaMinecart getMinecartAhead() {
		return getAdjacentMinecartFromDirection(getDirectionOfMotion());
	}
	
	public MinecartManiaMinecart getMinecartBehind() {
		return getAdjacentMinecartFromDirection(DirectionUtils.getOppositeDirection(getDirectionOfMotion()));
	}

	public void setWasMovingLastTick(boolean wasMovingLastTick) {
		this.wasMovingLastTick = wasMovingLastTick;
	}

	public boolean wasMovingLastTick() {
		return wasMovingLastTick;
	}
	
	public boolean isPoweredMinecart() {
		return minecart instanceof PoweredMinecart;
	}
	
	public boolean isStorageMinecart() {
		return minecart instanceof StorageMinecart;
	}
	
	public boolean isStandardMinecart() {
		return !isPoweredMinecart() && !isStorageMinecart();
	}
	
	public void kill() {

		try {
			CraftMinecart cart = (CraftMinecart)minecart;
			EntityMinecart em = (EntityMinecart) cart.getHandle();
			em.q();
			
		}
		catch (Exception e) {
			//Failed to kill minecart
		}

	}
}
