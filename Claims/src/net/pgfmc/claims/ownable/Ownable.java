package net.pgfmc.claims.ownable;

import net.pgfmc.core.playerdataAPI.PlayerData;

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
	
	// protected String name;
	
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
		ADMIN, 
		MEMBER,
		BLOCKED,
		EXCEPTION,
	}
	
	
	
	/**
	 * Constructs a new ownable.
	 * 
	 * 
	 * @param player
	 * @param lock
	 */
	public Ownable(PlayerData player) { // class constructor
		
		this.placer = player;
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
	
	public Security getAccess(PlayerData player) {
		
		
		return Security.MEMBER;
		
		/*
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
		*/
	};
}
