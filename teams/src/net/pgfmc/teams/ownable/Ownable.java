package net.pgfmc.teams.ownable;

import org.bukkit.GameMode;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;

/*
Written by CrimsonDart

-----------------------------------

abstract class extended by other container classes

subclasses:

BlockContainer
Beacons
EntityContainer

-----------------------------------
 */

/**
 * Abstract Ownable Class to be extended by things that can be owned; usually containers, but also claims.
 * Stores the {@code PlayerData} and {@code Lock}.
 * 
 * 
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public abstract class Ownable {
	
	protected PlayerData placer;
	
	protected Lock lock;
	
	/**
	 * Defines access states.
	 * 
	 * Each constant defines a different relationship between 
	 * the owner, the 
	 * 
	 * @author james
	 *
	 */
	public enum Security {
		OWNER,
		FAVORITE,
		FRIEND,
		UNLOCKED,
		DISALLOWED,
		EXCEPTION
	}
	
	public enum Lock {
		UNLOCKED,
		FRIENDS_ONLY,
		FAVORITES_ONLY,
		LOCKED,
		CREATIVE
	}
	
	/**
	 * Constructs a new ownable.
	 * 
	 * 
	 * @param player
	 * @param lock
	 */
	public Ownable(PlayerData player, Lock lock) { // class constructor
		
		this.placer = player;
		this.lock = lock;
	}
	
	// --------------------------------------------------- getters and setters
	
	/**
	 * Gets the Player that owns this Ownable.
	 * @return The player's PlayerData.
	 */
	public PlayerData getPlayer() {
		return placer;
	}
	
	public void setOwner(PlayerData pd) {
		placer = pd;
	}
	
	/**
	 * Gets the ownable's Lock.
	 * @return
	 */
	public Lock getLock() {
		return lock;
	}
	
	/**
	 * Sets the lock of this ownable.
	 * @param sug The lock that this ownable's lock is set to.
	 */
	public void setLock(Lock sug) {
		lock = sug;
	}
	
	public Security getAccess(PlayerData player) {
		
		Relation r = Friends.getRelation(placer, player);
		
		switch(lock) {
		case LOCKED: // ------------------------ only the owner can access.
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
			}
			return Security.DISALLOWED;
			
		case FAVORITES_ONLY:
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			}
			return Security.DISALLOWED;
			
		case FRIENDS_ONLY: // --------------------- only Friends can access.
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			} else if (r == Relation.FRIEND) {
				
				return Security.FRIEND;
			}
			return Security.DISALLOWED;
		
		case UNLOCKED: // --------------------- anybody can access.
			if (placer.equals(player)) {
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			}else if (r == Relation.FRIEND) {
				return Security.FRIEND;
				
			}
			return Security.UNLOCKED;
		
		case CREATIVE:
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				return Security.OWNER;
			}
			return Security.DISALLOWED;
			
		default:
			return Security.EXCEPTION;
		}
	};
}
