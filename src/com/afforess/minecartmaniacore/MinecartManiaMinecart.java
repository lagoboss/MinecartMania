package com.afforess.minecartmaniacore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.EntityMinecart;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import com.afforess.minecartmaniacore.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;


public class MinecartManiaMinecart {
	public final Minecart minecart;
	private static final double maxMomentum = 1E308;
	private Vector previousLocation;
	private Vector previousMotion;
	private Calendar cal;
	private DirectionUtils.CompassDirection previousFacingDir = DirectionUtils.CompassDirection.NO_DIRECTION;
	private boolean wasMovingLastTick;
	private String owner = "none";
	
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	
	public MinecartManiaMinecart(Minecart cart) {
		minecart = cart; 
		findOwner();
		initialize();
	}

	public MinecartManiaMinecart(Minecart cart, String owner) {
		minecart = cart; 
		this.owner = owner;
		initialize();
	}
	
	private void initialize() {
		minecart.setSlowWhenEmpty(false);
		cal = Calendar.getInstance();
		setWasMovingLastTick(isMoving());
		previousMotion = minecart.getVelocity().clone();
		previousLocation = minecart.getLocation().toVector().clone();
		minecart.setMaxSpeed(MinecartManiaWorld.getMaximumMinecartSpeedPercent() * 0.4D / 100);
	}

	/**
	 ** Attempts to find the player that spawned this minecart.
	 **
	 */
	private void findOwner() {
		double closest = Double.MAX_VALUE;
		Player closestPlayer = null;
		for (LivingEntity le : minecart.getWorld().getLivingEntities()) {
			if (le instanceof Player) {
				double distance = le.getLocation().toVector().distance(minecart.getLocation().toVector());
				if (distance < closest) {
					closestPlayer = (Player)le;
					closest = distance;
				}
			}
		}
		if (closestPlayer != null) {
			owner = closestPlayer.getName();
			
		}
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
		return MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY()-1, getZ()).getTypeId();
	}
	
	public boolean isPoweredBeneath() {
		if (MinecartManiaWorld.isBlockIndirectlyPowered(minecart.getWorld(), getX(), getY()-1, getZ()) || MinecartManiaWorld.isBlockIndirectlyPowered(minecart.getWorld(), getX(), getY(), getZ())) {
			return true;
		}
		//Temporary Fix for Pressure Plates, since they are NYI
		/*if (MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getTypeId() == Material.WOOD_PLATE.getId()) {
			return true;
		}
		if (MinecartManiaWorld.getBlockAt(getX(), getY(), getZ()).getTypeId() == Material.STONE_PLATE.getId()) {
			return this.minecart.getPassenger() != null;
		}*/
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
    		setMotionX(getMotionX() / MinecartManiaWorld.getLowSpeedBrakeBlockDivisor());
    		setMotionZ(getMotionZ() / MinecartManiaWorld.getLowSpeedBrakeBlockDivisor());
    		return true;
    	}
		return false;
	}

	public boolean doHighSpeedBrake() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getHighSpeedBrakeBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() / MinecartManiaWorld.getHighSpeedBrakeBlockDivisor());
    		setMotionZ(getMotionZ() / MinecartManiaWorld.getHighSpeedBrakeBlockDivisor());
    		return true;
    	}
		return false;
	}

	public boolean doLowSpeedBooster() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getLowSpeedBoosterBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() * MinecartManiaWorld.getLowSpeedBoosterBlockMultiplier());
    		setMotionZ(getMotionZ() * MinecartManiaWorld.getLowSpeedBoosterBlockMultiplier());
    		return true;
    	}
		return false;
	}
	
	public boolean doHighSpeedBooster() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getHighSpeedBoosterBlockId() && !isPoweredBeneath())
    	{
    		setMotionX(getMotionX() * MinecartManiaWorld.getHighSpeedBoosterBlockMultiplier());
    		setMotionZ(getMotionZ() * MinecartManiaWorld.getHighSpeedBoosterBlockMultiplier());
    		return true;
    	}
		return false;
	}
	
	public void doLauncherBlock() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getCatcherBlockId()){
			if (!isMoving()) {
				launchCart();
			}
		}
	}
	
	private void doWoodPressurePlate() {
		if (MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()).getType().equals(Material.WOOD_PLATE)) {
			if (MinecartManiaWorld.isPressurePlateRails()) {
				try {
					//does the task next server tick
					MinecartManiaWorld.doAsyncTask(MinecartManiaMinecart.class.getDeclaredMethod("doLauncherBlock"), this);
				} catch (Exception e) {	}
			}
		}
	}

	public boolean doCatcherBlock() {
		if (getBlockIdBeneath() == MinecartManiaWorld.getCatcherBlockId()){
			if (!isPoweredBeneath()) {
				stopCart();
				//Handle wooden pressure plates. Wood plates should launch the minecart next tick
				//doWoodPressurePlate();
				return true;
			}
		}
		return false;
	}
	
	private void launchCart() {

		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(this, 2);
loop:   for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				if (sign.getLine(i).toLowerCase().indexOf("north") > -1) {
					if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX()-1, getY(), getZ(), 2, DirectionUtils.CompassDirection.NORTH)) {
						sign.setLine(i, "[North]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.NORTH, 0.6D);
						break loop;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("east") > -1) {
					if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ()-1, 2, DirectionUtils.CompassDirection.EAST)) {
						sign.setLine(i, "[East]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.EAST, 0.6D);
						break loop;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("south") > -1) {
					if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX()+1, getY(), getZ(), 2, DirectionUtils.CompassDirection.SOUTH)) {
						sign.setLine(i, "[South]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.SOUTH, 0.6D);
						break loop;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("west") > -1) {
					if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ()+1, 2, DirectionUtils.CompassDirection.WEST)) {
						sign.setLine(i, "[West]");
						sign.update();
						setMotion(DirectionUtils.CompassDirection.WEST, 0.6D);
						break loop;
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("facing dir") > -1) {
					if (minecart.getPassenger() != null) {
						DirectionUtils.CompassDirection facingDir = DirectionUtils.getDirectionFromMinecartRotation((minecart.getPassenger().getLocation().getYaw() - 90.0F) % 360.0F);
						if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ()+1, 2, facingDir)) {
							sign.setLine(i, "[Facing Dir]");
							sign.update();
							setMotion(facingDir, 0.6D);
							break loop;
						}
					}
				}
				if (sign.getLine(i).toLowerCase().indexOf("previous dir") > -1) {
					if (!this.getPreviousFacingDir().equals(DirectionUtils.CompassDirection.NO_DIRECTION)) {
						if (MinecartUtils.validMinecartTrackAnyDirection(minecart.getWorld(), getX(), getY(), getZ()+1, 2)) {
							sign.setLine(i, "[Previous Dir]");
							sign.update();
							setMotion(this.getPreviousFacingDir(), 0.6D);
							break loop;
						}
					}
				}
			}
		}
		if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX()-1, getY(), getZ(), 2, DirectionUtils.CompassDirection.NORTH)) {
			setMotion(DirectionUtils.CompassDirection.NORTH, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ()-1, 2, DirectionUtils.CompassDirection.EAST)) {
			setMotion(DirectionUtils.CompassDirection.EAST, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX()+1, getY(), getZ(), 2, DirectionUtils.CompassDirection.SOUTH)) {
			setMotion(DirectionUtils.CompassDirection.SOUTH, 0.6D);
		}
		else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ()+1, 2, DirectionUtils.CompassDirection.WEST)) {
			setMotion(DirectionUtils.CompassDirection.WEST, 0.6D);
		}
		
		//Create event, then stop the cart and wait for the results
		MinecartLaunchedEvent mle = new MinecartLaunchedEvent(this, minecart.getVelocity().clone());
		stopCart();
		MinecartManiaCore.server.getPluginManager().callEvent(mle);
		if (mle.isCancelled()) {
			return;
		}
		else {
			minecart.setVelocity(mle.getLaunchSpeed());
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
		return MinecartUtils.isMinecartTrack(MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()));
	}
	
	/**
	 ** Determines whether or not the track the minecart is currently on is the center piece of a large track intersection. Returns true if it is an intersection.
	 **/
	public boolean isAtIntersection() {
		if (this.isOnRails()) {
			return MinecartUtils.isAtIntersection(minecart.getWorld(), getX(), getY(), getZ());
		}
		return false;
	}
	
	public Block getBlockTypeAhead() {
		return DirectionUtils.getBlockTypeAhead(minecart.getWorld(), getDirectionOfMotion(), getX(), getY(), getZ());
	}
	
	public Block getBlockTypeBehind() {
		return DirectionUtils.getBlockTypeAhead(minecart.getWorld(), DirectionUtils.getOppositeDirection(getDirectionOfMotion()), getX(), getY(), getZ());
	}

	public void doPressurePlateRails() {
		if (MinecartManiaWorld.isPressurePlateRails()) {
			if (MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()).getType().equals(Material.STONE_PLATE)
			|| MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()).getType().equals(Material.WOOD_PLATE)) {
				minecart.setDerailedVelocityMod(new Vector(1, 1, 1));
	    	}
			else if (getBlockTypeBehind() != null && (getBlockTypeBehind().getType().equals(Material.STONE_PLATE) || getBlockTypeBehind().getType().equals(Material.WOOD_PLATE))) {
				minecart.setDerailedVelocityMod(new Vector(0.5D, 0.5D, 0.5D));
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
	
	public ArrayList<Block> getParallelBlocks() {
		ArrayList<Block> blocks = new ArrayList<Block>(4);
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX()-1, getY(), getZ()));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX()+1, getY(), getZ()));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()-1));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), getX(), getY(), getZ()+1));
		return blocks;
	}
	
	public ArrayList<Block> getPreviousLocationParallelBlocks() {
		ArrayList<Block> blocks = new ArrayList<Block>(4);
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), previousLocation.getBlockX()-1, previousLocation.getBlockY(), previousLocation.getBlockZ()));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), previousLocation.getBlockX()+1, previousLocation.getBlockY(), previousLocation.getBlockZ()));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), previousLocation.getBlockX(), previousLocation.getBlockY(), previousLocation.getBlockZ()-1));
		blocks.add(MinecartManiaWorld.getBlockAt(minecart.getWorld(), previousLocation.getBlockX(), previousLocation.getBlockY(), previousLocation.getBlockZ()+1));
		return blocks;
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
	
	public Material getType() {
		if (isPoweredMinecart()) {
			return Material.POWERED_MINECART;
		}
		if (isStorageMinecart()) {
			return Material.STORAGE_MINECART;
		}
		
		return Material.MINECART;
	}
	
	public Inventory getInventory() {
		if (isStorageMinecart()) {
			return ((StorageMinecart)minecart).getInventory();
		}
		return null;
	}
	
	/**
	 * attempts to find and return the owner of this object, a player or a minecart mania chest.
	 * It will fail if the owner is offline, wasn't found, or the chest was destroyed.
	 * @return Player or Minecart Mania Chest that spawned this minecart.
	 */
	public Object getOwner() {
		if (owner.equals("none")) {
			return null;
		}
		if (owner.contains("[") && owner.contains("]")) {
			try {
				int x, y, z;
				String[] split = owner.split(":");
				x = Integer.valueOf(StringUtils.getNumber(split[0]));
				y = Integer.valueOf(StringUtils.getNumber(split[1]));
				z = Integer.valueOf(StringUtils.getNumber(split[2]));
				if (MinecartManiaWorld.getBlockAt(minecart.getWorld(), x, y, z).getState() instanceof Chest) {
					return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(minecart.getWorld(), x, y, z).getState());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return MinecartManiaCore.server.getPlayer(owner);
	}
	
	public void kill() {
		kill(true);
	}
	
	public void kill(boolean returnToOwner) {

		try {
			if (returnToOwner) {
				Object owner = getOwner();
				if (owner instanceof Player && MinecartManiaWorld.isReturnMinecartToOwner()) {
					((Player)owner).getInventory().addItem(new ItemStack(getType(), 1));
				}
				else if (owner instanceof MinecartManiaChest && MinecartManiaWorld.isReturnMinecartToOwner()) {
					((MinecartManiaChest)owner).addItem(getType().getId());
				}
				else {
					minecart.getWorld().dropItemNaturally(minecart.getLocation(), new ItemStack(getType(), 1));
				}
			}
			
			//Fire destroyed event
			MinecartManiaMinecartDestroyedEvent mmmee = new MinecartManiaMinecartDestroyedEvent(this);
			MinecartManiaCore.server.getPluginManager().callEvent(mmmee);
			
			MinecartManiaWorld.delMinecartManiaMinecart(minecart.getEntityId());
			CraftMinecart cart = (CraftMinecart)minecart;
			EntityMinecart em = (EntityMinecart) cart.getHandle();
			em.q();
			
			
		}
		catch (Exception e) {
			//Failed to kill minecart
		}

	}
}
