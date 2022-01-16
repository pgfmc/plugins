package net.pgfmc.teams.ownable.block;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.Vector4;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable;
import net.pgfmc.teams.ownable.block.table.ClaimSection;
import net.pgfmc.teams.ownable.block.table.ClaimsTable;
import net.pgfmc.teams.ownable.block.table.ContainerSection;
import net.pgfmc.teams.ownable.block.table.ContainerTable;

/**
 * Block Ownable Class. 
 * 
 * When a new Ownable Block (OB) is created, it is automatically added to the Ownable table and The Sets in 
 * {@code BlockManager} If the block placed is a {@code Lodestone}, then the ownable functions as a claim.
 * if the block is anything else, then it is a normal block Ownable. 
 * 
 * Claims have a square radius of 36 blocks.
 * 
 * Access to any ownable / range inside a claim is determined by the extended {@code Ownable.isAllowed(PlayerData pd)}.
 * @see Ownable
 * @see BlockManager
 * @see ClaimSection 
 * @see ContainerSection
 * @see ClaimsTable
 * @see ContainerTable
 * 
 * @author CrimsonDart
 * @since 1.1.0	
 * @version 4.0.3
 */
public class OwnableBlock extends Ownable {
	
	private Vector4 vector;
	private boolean isClaim;
	
	public OwnableBlock(PlayerData player, Vector4 vec, Lock lock) {
		super(player, (lock == null) ? player.getData("lockMode") : lock);
		
		Block block = vec.getBlock();
		vector = vec;
		
		if (block.getType() == Material.LODESTONE) {
			
			isClaim = true;
			BlockManager.claims.add(this);
			ClaimsTable.put(this);
			return;
		}
		isClaim = false;
		BlockManager.containers.add(this);	
		ContainerTable.put(this);
	}
	
	/**
	 * @since 1.0.2
	 * @version 4.0.2
	 */
	@Override
	public void setLock(Lock lock) {
		
		Vector4 v4 = getOtherSide(vector.getBlock());
		if (v4 != null) {
			getOwnable(v4.getBlock()).lock = lock;
		}

		this.lock = lock;
	}
	
	/**
	 * Attempts to cycle the lock on this ownable.
	 * @param pd The player cycling the lock
	 * @return cancellation state.
	 */
	public void cycleLock(PlayerData pd) {
		
		if (getLock() == Lock.CREATIVE) { // for Creative Locks.
			pd.sendMessage("§cAccess Denied.");
			pd.playSound(Sound.BLOCK_ANVIL_DESTROY);
			return;
		}
		
		switch(getAccess(pd)) {
		
		case OWNER: {
			
			// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
			switch(getLock()) {
			case LOCKED:
				
				pd.sendMessage("§6Favorites only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FAVORITES_ONLY);
				return;
				
			case FAVORITES_ONLY:
				
				pd.sendMessage("§6Friends only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FRIENDS_ONLY);
				return;
				
				
			case FRIENDS_ONLY:
				
				pd.sendMessage("§6Unlocked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				pd.sendMessage("§6Fully Locked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.LOCKED);
				return;
				
			case CREATIVE: return;
			}
		}
		
		case FAVORITE: {
			
			switch(getLock()) {
			case LOCKED:
				
				pd.sendMessage("§cAccess Denied.");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 0, 0);
				return;
				
			case FAVORITES_ONLY:
				
				pd.sendMessage("§6Friends only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FRIENDS_ONLY);
				return;
				
			case FRIENDS_ONLY:
				
				pd.sendMessage("§6Unlocked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				
				pd.sendMessage("§6Favorites Only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FAVORITES_ONLY);
				return;
				
			default:
				return;
			}
		}
		
		case FRIEND: {
			
			pd.sendMessage("§cAccess denied.");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			return;
		}
		case UNLOCKED: {
			
			pd.sendMessage("§cAccess denied.");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			return;
		}
		
		case DISALLOWED: {
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			if (isClaim) {
				pd.sendMessage("§cThis Lodestone is locked!");
			} else {
				pd.sendMessage("§cThis container is locked!");
			}
		}
		case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!"); return;
		}
	}
	
	static Vector4 getOtherSide(Block block) {
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) { // chest check
			
			InventoryHolder invh = ((Chest) block.getState()).getInventory().getHolder();
			if (invh instanceof DoubleChest) {
				DoubleChest db = (DoubleChest) invh;
				Vector4 v1 = new Vector4(block);
				
				double x = db.getX();
				if (v1.x() == x) { // search the z axis
					
					double z = db.getZ();
					z = z > v1.z() ? z + .5d : z - .5d;
					
					return new Vector4(v1.x(), v1.y(), (int) z, v1.w());
				} else {
					
					x = x > v1.x() ? x + .5d : x - .5d;
					
					return new Vector4((int) x, v1.y(), v1.z(), v1.w());
				}
			}
		}
		return null;
	}
	
	/**
	 * Removes this ownable.
	 */
	public void remove() {
		if (isClaim) {
			System.out.println("1");
			ClaimsTable.remove(this);
			return;
		} else {
			System.out.println("2");
			ContainerTable.remove(this);
			return;
		}
	}
	
	/**
	 * Returns the location, in the form of a vector.
	 * @return The vector location.
	 */
	public Vector4 getLocation() {
		return vector;
	}
	
	/**
	 * Gets an ownable from the input block.
	 * @param block The block to get the Ownable for.
	 * @return The block's ownable.
	 */
	public static OwnableBlock getOwnable(Block block) { // gets a container from block
		
		if (!BlockManager.isOwnable(block.getType())) return null;
		if (block.getType() == Material.LODESTONE) 
			return ClaimsTable.getOwnable(new Vector4(block));
		else 
			return ContainerTable.getOwnable(new Vector4(block));
	}
	
	public static OwnableBlock getOwnable(Vector4 v, boolean claim) {
		if (claim) {
			return ClaimsTable.getOwnable(v);
		} else {
			return ContainerTable.getOwnable(v);
		}
	}
	
	/**
	 * Returns if this ownable is a claim or not.
	 * @return true if this ownable is a claim, false if else.
	 */
	public boolean isClaim() {
		return (isClaim);
	}
}
