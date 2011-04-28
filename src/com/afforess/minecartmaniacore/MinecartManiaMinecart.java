package com.afforess.minecartmaniacore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.config.ControlBlock;
import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartElevatorEvent;
import com.afforess.minecartmaniacore.event.MinecartLaunchedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartCreatedEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaMinecartDestroyedEvent;
import com.afforess.minecartmaniacore.event.MinecartSpeedMultiplierEvent;
import com.afforess.minecartmaniacore.event.MinecartTimeEvent;
import com.afforess.minecartmaniacore.signs.LaunchMinecartAction;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.utils.BlockUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.EntityUtils;
import com.afforess.minecartmaniacore.utils.MinecartUtils;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;


public class MinecartManiaMinecart {
	public final Minecart minecart;
	private Vector previousLocation;
	private Vector previousMotion;
	private Calendar cal;
	private DirectionUtils.CompassDirection previousFacingDir = DirectionUtils.CompassDirection.NO_DIRECTION;
	private boolean wasMovingLastTick;
	private String owner = "none";
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	private int range = 4;
	private int rangeY = 4;
	private boolean dead = false;
	public static final double MAXIMUM_MOMENTUM = 1E150D;
	public boolean createdLastTick = true;
	
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
		setRange(MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("Range")));
		setRangeY(MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("RangeY")));
		cal = Calendar.getInstance();
		setWasMovingLastTick(isMoving());
		previousMotion = minecart.getVelocity().clone();
		previousLocation = minecart.getLocation().toVector().clone();
		previousLocation.setY(previousLocation.getX() -1); //fool game into thinking we've already moved
		minecart.setMaxSpeed(MinecartManiaWorld.getDefaultMinecartSpeedPercent() * 0.4D / 100);
		MinecartManiaCore.server.getPluginManager().callEvent(new MinecartManiaMinecartCreatedEvent(this));
	}

	/**
	 ** Attempts to find the player that spawned this minecart.
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
	
	public boolean isDead() {
		return dead;
	}
	
	public final Location getLocation() {
		return minecart.getLocation();
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
	
	public void changeMotionX(double change) {
		setMotionX(getMotionX() + change);
	}
	
	public void setMotionX(double motionX){
		setMotion(motionX, getMotionY(), getMotionZ());
	}
	
	public double getMotionY() {
		return minecart.getVelocity().getY();
	}
	
	public void changeMotionY(double change) {
		setMotionY(getMotionY() + change);
	}
	
	public void setMotionY(double motionY){
		setMotion(getMotionX(), motionY, getMotionZ());
	}
	
	public double getMotionZ() {
		return minecart.getVelocity().getZ();
	}
	
	public void changeMotionZ(double change) {
		setMotionZ(getMotionZ() + change);
	}
	
	public void setMotionZ(double motionZ){
		setMotion(getMotionX(), getMotionY(), motionZ);
	}
	
	/**
	 * Multiplies the minecarts current motion by the given multiplier in a safe way that will avoid
	 * causing overflow, which will cause the minecart to grind to a halt.
	 * @param multiplier
	 */
	public void multiplyMotion(double multiplier) {
		if (MAXIMUM_MOMENTUM / Math.abs(multiplier) > Math.abs(getMotionX())) {
			setMotionX(getMotionX() * multiplier);
		}
		if (MAXIMUM_MOMENTUM / Math.abs(multiplier) > Math.abs(getMotionZ())) {
			setMotionZ(getMotionZ() * multiplier);
		}
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
	
	public final int getX(){
		return minecart.getLocation().getBlockX();
	}
	
	public final int getY(){
		return minecart.getLocation().getBlockY();
	}
	
	public final int getZ(){
		return minecart.getLocation().getBlockZ();
	}

	/**
	 ** Returns the value from the loaded data
	 ** @param the string key the data value is associated with
	 **/
	 public final Object getDataValue(String key) {
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
	 public final void setDataValue(String key, Object value) {
		 if (value == null) {
			 data.remove(key);
		 }else {
			 data.put(key, value);
		 }
	 }
	
	 
	public int getBlockIdBeneath() {
		return MinecartManiaWorld.getBlockIdAt(minecart.getWorld(), getX(), getY()-1, getZ());
	}
	
	public Item getItemBeneath() {
		return Item.getItem(getBlockBeneath());
	}
	
	public Block getBlockBeneath() {
		if (ControlBlockList.getControlBlock(Item.getItem(getLocation().getBlock())) != null) {
			return getLocation().getBlock();
		}
		else {
			Location temp = getLocation();
			temp.setY(temp.getY() - 1);
			return temp.getBlock();
		}
	}
	
	public boolean isPoweredBeneath() {
		if (MinecartManiaWorld.isBlockPowered(minecart.getWorld(), getX(), getY()-2, getZ()) || 
			MinecartManiaWorld.isBlockIndirectlyPowered(minecart.getWorld(), getX(), getY()-1, getZ()) || 
			MinecartManiaWorld.isBlockIndirectlyPowered(minecart.getWorld(), getX(), getY(), getZ())) {
			return true;
		}
		return false;
	}
	
	public void reverse() {
		setMotionX(getMotionX() * -1);
		setMotionY(getMotionY() * -1);
		setMotionZ(getMotionZ() * -1);
	}
	
	public void undoPoweredRails() {
		//this server had decided to override the default boost value, so we need to undo notch's changes
		if (getLocation().getBlock().getTypeId() == Item.POWERED_RAIL.getId()) {
			if (ControlBlockList.getSpeedMultiplier(this) != 1.0D && isMoving()) {
				int data = getLocation().getBlock().getData();
				boolean powered = (data & 8) != 0;
				final double boost = 0.0078125D; //magic number from MC code
				if (powered) {
					if (data == 2) {
						changeMotionX(-boost);
					}
					else if (data == 3) {
						changeMotionX(boost);
					}
					else if (data == 4) {
						changeMotionZ(boost);
					}
					else if (data == 5) {
						changeMotionZ(-boost);
					}
				}
				else {
					multiplyMotion(2.0D);
				}
			}
		}
			
	}
	
	public boolean doSpeedMultiplierBlock() {
		double multiplier = ControlBlockList.getSpeedMultiplier(this);
		if (multiplier != 1.0D) {
			MinecartSpeedMultiplierEvent msme = new MinecartSpeedMultiplierEvent(this, multiplier);
			MinecartManiaCore.server.getPluginManager().callEvent(msme);
			multiplyMotion(msme.getSpeedMultiplier());
	    	return msme.isCancelled();
    	}
		//check for powered rails
		multiplier = ControlBlockList.getSpeedMultiplier(this);
		if (multiplier != 1.0D) {
			MinecartSpeedMultiplierEvent msme = new MinecartSpeedMultiplierEvent(this, multiplier);
			MinecartManiaCore.server.getPluginManager().callEvent(msme);
			multiplyMotion(msme.getSpeedMultiplier());
	    	return msme.isCancelled();
		}
		return false;
	}
	
	public boolean doPlatformBlock() {
		if (ControlBlockList.isValidPlatformBlock(getBlockBeneath()) && isStandardMinecart()) {
			if (minecart.getPassenger() == null) {
				List<LivingEntity> list = minecart.getWorld().getLivingEntities();
				double range = ControlBlockList.getControlBlock(getItemBeneath()).getPlatformRange();
				range *= range;
				for (LivingEntity le : list) {
					if (le.getLocation().toVector().distanceSquared(minecart.getLocation().toVector()) < range) {
						//Let the world know about this
						VehicleEnterEvent vee = new VehicleEnterEvent(minecart, le);
						MinecartManiaCore.server.getPluginManager().callEvent(vee);
						if (!vee.isCancelled()) {
							minecart.setPassenger(le);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void doLauncherBlock() {
		if (ControlBlockList.getLaunchSpeed(getItemBeneath()) != 0.0D){
			if (isPoweredBeneath()) {
				if (!isMoving()) {
					launchCart(ControlBlockList.getLaunchSpeed(getItemBeneath()));
				}
			}
		}
	}

	public boolean doCatcherBlock() {
		if (ControlBlockList.isCatcherBlock(getItemBeneath())){
			if (!isPoweredBeneath()) {
				MinecartCaughtEvent mce = new MinecartCaughtEvent(this);
				MinecartManiaCore.server.getPluginManager().callEvent(mce);
				if (!mce.isActionTaken()) {
					stopCart();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean doKillBlock() {
		if (ControlBlockList.isValidKillMinecartBlock(getBlockBeneath())) {
			kill(getOwner() instanceof MinecartManiaChest);
			return true;
		}
		return false;
	}
	
	public void launchCart() {
		launchCart(0.6D);
	}
	
	public void launchCart(double speed) {
		ArrayList<Sign> signList = SignUtils.getAdjacentMinecartManiaSignList(getLocation(), 2);
		for (Sign sign : signList) {
			if (sign.executeAction(this, LaunchMinecartAction.class)) {
				break;
			}
		}
		if (!isMoving()) {
			if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ(), 2, DirectionUtils.CompassDirection.NORTH)) {
				setMotion(DirectionUtils.CompassDirection.NORTH, speed);
			}
			else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ(), 2, DirectionUtils.CompassDirection.EAST)) {
				setMotion(DirectionUtils.CompassDirection.EAST, speed);
			}
			else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ(), 2, DirectionUtils.CompassDirection.SOUTH)) {
				setMotion(DirectionUtils.CompassDirection.SOUTH, speed);
			}
			else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), getX(), getY(), getZ(), 2, DirectionUtils.CompassDirection.WEST)) {
				setMotion(DirectionUtils.CompassDirection.WEST, speed);
			}
		}
		
		//Create event, then stop the cart and wait for the results
		MinecartLaunchedEvent mle = new MinecartLaunchedEvent(this, minecart.getVelocity().clone());
		stopCart();
		MinecartManiaCore.server.getPluginManager().callEvent(mle);
		if (mle.isActionTaken()) {
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

	public CompassDirection getDirectionOfMotion()
	{
		if (getMotionX() < 0.0D) return CompassDirection.NORTH;
		if (getMotionZ() < 0.0D) return CompassDirection.EAST;
		if (getMotionX() > 0.0D) return CompassDirection.SOUTH;
		if (getMotionZ() > 0.0D) return CompassDirection.WEST;
		return CompassDirection.NO_DIRECTION;
	}
	
	public CompassDirection getPreviousDirectionOfMotion() {
		return previousFacingDir;
	}
	
	public void setPreviousDirectionOfMotion(CompassDirection direction) {
		previousFacingDir = direction;
	}
	
	@Deprecated
	public void setPreviousFacingDir(DirectionUtils.CompassDirection dir) {
		previousFacingDir = dir;
	}

	@Deprecated
	public DirectionUtils.CompassDirection getPreviousFacingDir() {
		return previousFacingDir;
	}
	
	/**
	 * Attempts a "best guess" at the direction of the minecart. 
	 * If the minecart is moving, it will return the correct direction, but if it's stopped, it will use the value stored in memory.
	 * @return CompassDirection that the minecart is moving towards
	 */
	public CompassDirection getDirection() {
		if (isMoving()) {
			return getDirectionOfMotion();
		}
		return getPreviousDirectionOfMotion();
	}

	public boolean doEjectorBlock() {
		if (ControlBlockList.isValidEjectorBlock(getBlockBeneath())) {
			if (minecart.getPassenger() != null) {
				return minecart.eject();
			}
		}
		return false;
	}
	
	public void doRealisticFriction() {
		if (minecart.getPassenger() == null && isOnRails()) {
			multiplyMotion(1.0385416D);
    	}
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
		return MinecartUtils.isTrack(minecart.getLocation());
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
	
	public HashSet<Block> getAdjacentBlocks(int range) {
		return BlockUtils.getAdjacentBlocks(minecart.getLocation(), range);
	}
	
	public HashSet<Block> getPreviousLocationAdjacentBlocks(int range) {
		return BlockUtils.getAdjacentBlocks(getPreviousLocation().toLocation(minecart.getWorld()), range);
	}
	
	public HashSet<Block> getBlocksBeneath(int range) {
		return BlockUtils.getBlocksBeneath(minecart.getLocation(), range);
	}
	
	public HashSet<Block> getPreviousLocationBlocksBeneath(int range) {
		return BlockUtils.getBlocksBeneath(getPreviousLocation().toLocation(minecart.getWorld()), range);
	}
	
	public boolean isMovingAway(Location l) {
		//North of us
		if (l.getBlockX() - getX() < 0) {
			if (getDirectionOfMotion().equals(DirectionUtils.CompassDirection.SOUTH)) {
				return true;
			}
		}
		//South of us
		if (l.getBlockX() - getX() > 0) {
			if (getDirectionOfMotion().equals(DirectionUtils.CompassDirection.NORTH)) {
				return true;
			}
		}
		//East of us
		if (l.getBlockZ() - getZ() < 0) {
			if (getDirectionOfMotion().equals(DirectionUtils.CompassDirection.WEST)) {
				return true;
			}
		}
		//West of us
		if (l.getBlockZ() + getZ() > 0) {
			if (getDirectionOfMotion().equals(DirectionUtils.CompassDirection.WEST)) {
				return true;
			}
		}
		
		return false;
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
	
	public Item getType() {
		if (isPoweredMinecart()) {
			return Item.POWERED_MINECART;
		}
		if (isStorageMinecart()) {
			return Item.STORAGE_MINECART;
		}
		
		return Item.MINECART;
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
	
	public boolean isOwner(Entity e) {
		Object owner = getOwner();
		if (owner == null) {
			return false;
		}
		if (owner instanceof Player) {
			return ((Player)owner).getEntityId() == e.getEntityId();
		}
		if (owner instanceof MinecartManiaChest) {
			return ((MinecartManiaChest) owner).getLocation().equals(e.getLocation());
		}
		return false;
	}
	
	public void kill() {
		kill(true);
	}
	
	public void kill(boolean returnToOwner) {
		if (!dead && !minecart.isDead()) {
			if (returnToOwner) {
				//give the items back inside too
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				if (isStorageMinecart()) {
					for (ItemStack i : ((MinecartManiaStorageCart)this).getContents()) {
						if (i != null && i.getTypeId() != 0) {
							items.add(i);
						}
					}
				}
				items.add(new ItemStack(getType().toMaterial(), 1));
				
				Object owner = getOwner();
				MinecartManiaInventory inventory = null;
				if (owner instanceof Player && MinecartManiaWorld.isReturnMinecartToOwner()) {
					inventory = MinecartManiaWorld.getMinecartManiaPlayer((Player)owner);
				}
				else if (owner instanceof MinecartManiaChest && MinecartManiaWorld.isReturnMinecartToOwner()) {
					inventory = ((MinecartManiaChest)owner);
				}
				
				if (inventory != null) {
					for (int i = 0; i < items.size(); i++) {
						if (!inventory.addItem(items.get(i))) {
							minecart.getWorld().dropItemNaturally(minecart.getLocation(), items.get(i));
						}
					}
				}
				else {
					for (ItemStack i : items) {
						minecart.getWorld().dropItemNaturally(minecart.getLocation(), i);
					}
				}	
			}
			
			//Fire destroyed event
			MinecartManiaMinecartDestroyedEvent mmmee = new MinecartManiaMinecartDestroyedEvent(this);
			MinecartManiaCore.server.getPluginManager().callEvent(mmmee);
			
			minecart.remove();
			dead = true;
		}
	}
	
	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}
	
	public void setRangeY(int range) {
		this.rangeY = range;
	}
	
	public int getRangeY() {
		return rangeY;
	}

	public void updateChunks() {
		if (MinecartManiaWorld.isKeepMinecartsLoaded() && !isDead()) {
			Chunk current = minecart.getLocation().getBlock().getChunk();
			Chunk old = previousLocation.toLocation(minecart.getWorld()).getBlock().getChunk();
			int range = 3;
			HashSet<Chunk> toLoad = new HashSet<Chunk>();
			for (int dx = -(range); dx <= range; dx++){
				for (int dz = -(range); dz <= range; dz++){
					Chunk chunk = current.getWorld().getChunkAt(current.getX() + dx, current.getZ() + dz);
					toLoad.add(chunk);
					current.getWorld().loadChunk(chunk);
				}
			}
			//we've just moved chunks, and we must manually unload chunks
			if (current.getX() != old.getX() || current.getZ() != old.getZ()) {
				for (int dx = -(range); dx <= range; dx++){
					for (int dz = -(range); dz <= range; dz++){
						Chunk chunk = current.getWorld().getChunkAt(old.getX() + dx, old.getZ() + dz);
						if (!toLoad.contains(chunk)) {
							old.getWorld().unloadChunkRequest(old.getX() + dx, old.getZ() + dz);
						}
					}
				}
			}
		}
	}
	
	public boolean isApproaching(Vector v) {
		if (!isMoving()) {
			return false;
		}
		CompassDirection direction = getDirectionOfMotion();
		if (direction == CompassDirection.NORTH) {
			if (minecart.getLocation().getX() - v.getX() < 3.0D && minecart.getLocation().getX() - v.getX() > 0.0D) {
				return Math.abs(minecart.getLocation().getZ() - v.getZ()) < 1.5D;
			}
		}
		if (direction == CompassDirection.SOUTH) {
			if (minecart.getLocation().getX() - v.getX() > -3.0D && minecart.getLocation().getX() - v.getX() < 0.0D) {
				return Math.abs(minecart.getLocation().getZ() - v.getZ()) < 1.5D;
			}
		}
		if (direction == CompassDirection.EAST) {
			if (minecart.getLocation().getZ() - v.getZ() < 3.0D && minecart.getLocation().getZ() - v.getZ() > 0.0D) {
				return Math.abs(minecart.getLocation().getX() - v.getX()) < 1.5D;
			}
		}
		if (direction == CompassDirection.WEST) {
			if (minecart.getLocation().getZ() - v.getZ() > -3.0D && minecart.getLocation().getZ() - v.getZ() < 0.0D) {
				return Math.abs(minecart.getLocation().getX() - v.getX()) < 1.5D;
			}
		}
		
		return false;
	}
	
	public MinecartManiaMinecart copy(Minecart newMinecart) {
		MinecartManiaMinecart newCopy = MinecartManiaWorld.getMinecartManiaMinecart(newMinecart);
		newCopy.cal = this.cal;
		newCopy.data = this.data;
		newCopy.range = this.range;
		newCopy.owner = this.owner;
		newCopy.previousFacingDir = this.previousFacingDir;
		newCopy.previousLocation = this.previousLocation;
		newCopy.previousMotion = this.previousMotion;
		newCopy.wasMovingLastTick = this.wasMovingLastTick;
		
		return newCopy;
	}

	public boolean doElevatorBlock() {
		if (ControlBlockList.isValidElevatorBlock(getBlockBeneath())) {
			//Get where we are
			Block elevatorBlock = getBlockBeneath();
			int y = elevatorBlock.getY();
			//Find the closest elevator block starting 2 blocks (so we do not find ourselves) away and expand from there.
			for (int yOffset = 2; yOffset < 128; yOffset++) {
				if (y + yOffset < 128) {
					//See if we have a valid destination
					if (MinecartUtils.isTrack(elevatorBlock.getRelative(0, yOffset, 0))
							&& ControlBlockList.isElevatorBlock(Item.getItem(elevatorBlock.getRelative(0, yOffset-1, 0)))) {
						//do the teleport and return
						MinecartElevatorEvent event = new MinecartElevatorEvent(this, elevatorBlock.getRelative(0, yOffset, 0).getLocation());
						MinecartManiaCore.server.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							return minecart.teleport(event.getTeleportLocation());
						}
					}					
				} 
				if (y - yOffset > 0) {
					//See if we have a valid destination
					if (MinecartUtils.isTrack(elevatorBlock.getRelative(0, -yOffset, 0))
							&& ControlBlockList.isElevatorBlock(Item.getItem(elevatorBlock.getRelative(0, -yOffset-1, 0)))) {
						//do the teleport and return
						MinecartElevatorEvent event = new MinecartElevatorEvent(this, elevatorBlock.getRelative(0, -yOffset, 0).getLocation());
						MinecartManiaCore.server.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							return minecart.teleport(event.getTeleportLocation());
						}
					}										
				}
			}
		}
		return false;
	}
	
	public void updateToPoweredRails() {
		HashSet<Block> blocks = getAdjacentBlocks(2);
		for (Block block : blocks) {
			ControlBlock cb = ControlBlockList.getControlBlock(Item.getItem(block));
			if (cb != null && cb.updateToPoweredRail && block.getTypeId() != Item.POWERED_RAIL.getId()) {
				Block rail = block.getRelative(0, 1, 0);
				if (MinecartUtils.isTrack(rail) && !MinecartUtils.isCurvedTrack(rail)) {
					rail.setTypeId(Item.POWERED_RAIL.getId());
					
					//analyze for replacement block
					HashMap<Item, Integer> replacement = new HashMap<Item, Integer>();
					for (Block loop : BlockUtils.getAdjacentBlocks(block.getLocation(), 1)) {
						Item temp = Item.getItem(loop);
						if (temp != null) {
							if (replacement.containsKey(temp)) {
								replacement.put(temp, replacement.get(temp) + 1);
							}
							else {
								replacement.put(temp, 1);
							}
						}
					}
					
					//find the most common item from adjacent blocks
					Item best = null;
					int count = 0;
					Iterator<Entry<Item, Integer>> i = replacement.entrySet().iterator();
					while(i.hasNext()) {
						Entry<Item, Integer> entry = i.next();
						if (best == null || entry.getValue() > count) {
							if (EntityUtils.isSolidMaterial(entry.getKey().toMaterial())) {
								best = entry.getKey();
								count = entry.getValue();
							}
						}
					}
					if (best != null) {
						block.setTypeId(best.getId());
					}
				}
			}
		}
	}
}
