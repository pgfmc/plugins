package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;

import net.pgfmc.claims.ownable.block.table.ClaimSection;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

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
public class Claim {
	
	// protected String name;
	private PlayerData placer;
	private Vector4 vector;
	private Set<PlayerData> members = new HashSet<>();
    public boolean explosionsEnabled = false;
    public boolean doorsLocked = true;
    public boolean switchesLocked = true;
    public boolean inventoriesLocked = true;
    public boolean monsterKilling = true;
    public boolean livestockKilling = false;
    public ArrayList<Vector4> beacons = new ArrayList<Vector4>();
    private ArrayList<Claim> network = new ArrayList<Claim>();

    public boolean calculated = false; // Wether or not this claim's network has been calculated this tick.
	
	/**
	 * Defines access states.
	 * 
	 * Each constant defines a different relationship between 
	 * the owner, the 
	 * 
	 * @author CrimsonDart
	 *
	 */
	public enum Security {
		ADMIN, 
		MEMBER,
		BLOCKED,
	}
	
	public Claim(PlayerData player, Vector4 vec, Set<PlayerData> members) {
		vector = vec;
		Block block = vec.getBlock();

		if (block.getType() != Material.LODESTONE) {
			return;
		}
		ClaimsTable.put(this);
        this.network.add(this);

		this.placer = player;
        this.members = members;
	}
	
	public void forwardUpdateFrom(Claim claim) {
        for (Claim clame : getNetwork()) {
            clame.placer = claim.placer;
            clame.members = claim.members;
            clame.explosionsEnabled = claim.explosionsEnabled;
            clame.doorsLocked = claim.doorsLocked;
            clame.switchesLocked = claim.switchesLocked;
            clame.inventoriesLocked = claim.inventoriesLocked;
            clame.monsterKilling = claim.monsterKilling;
            clame.livestockKilling = claim.livestockKilling;
        }
	}

    // Adding a claim to this claim's network, will add it to all in the network.
    private boolean addMerge(Claim add) {
        if (!add.calculated) {
            this.network.add(add);
            add.network = this.network;
            add.forwardUpdateFrom(this);
            add.calculated = true;
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Claim> getNetwork() {
        return this.network;
    }

    public void calculateNetwork(boolean reset) {
        ArrayList<Claim> network = new ArrayList<>(); 

        network.add(this);
        this.network = network;
        this.calculated = true;
        appendMergedClaims(network);

        if (reset) {
            for (Claim claim : network) {
                claim.calculated = false;
            }
        }
    }

    private void appendMergedClaims(ArrayList<Claim> network) {

        for (Claim claim : ClaimsTable.getNearbyClaims(this.getLocation(), Range.MERGE)) {
            if (this.addMerge(claim) && claim.getPlayer() == network.get(0).getPlayer()) {
                claim.appendMergedClaims(network);
            }
        }
    }

    public Pair getBeaconInfo() {
        ArrayList<Vector4> beacons = new ArrayList<>();
        ArrayList<PotionEffect> effects = new ArrayList<>();
        ArrayList<Claim> claims = getNetwork();

        for (Claim claim : claims) {
            for (Vector4 pos : claim.beacons) {
                Block block = pos.getBlock();
                if (block == null || !(block.getType() == Material.BEACON)) {continue;}

                boolean allow = true;
                for (Vector4 beacon : beacons) {
                    if (beacon.equals(pos)) {
                        allow = false;
                        break;
                    }
                }

                if (!allow) {continue;}

                beacons.add(pos);
                Beacon beacon = (Beacon) block.getState();
                
                if (beacon.getPrimaryEffect() != null) {
                    addEffect(effects, beacon.getPrimaryEffect());
                }
                if (beacon.getSecondaryEffect() != null) {
                    addEffect(effects, beacon.getSecondaryEffect());
                }
            }
        }

        return new Pair(effects, beacons.size()); 
    }

    private static void addEffect(ArrayList<PotionEffect> effects, PotionEffect effect) {
        for (int i = 0; i < effects.size(); i++) {
            PotionEffect comp = effects.get(i);

            if (comp.getType() != effect.getType()) {continue;}
            if (comp.getAmplifier() >= effect.getAmplifier()) {return;}
            effects.set(i, effect);
            return;
        }
        effects.add(effect);
    }

    public void addEffectsFromClaim(ArrayList<PotionEffect> effects) {
        for (Vector4 pos : this.beacons) {
            Block block = pos.getBlock();
            if (block == null || !(block.getType() == Material.BEACON)) {continue;}
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getPrimaryEffect() != null) {
                addEffect(effects, beacon.getPrimaryEffect());
            }
            if (beacon.getSecondaryEffect() != null) {
                addEffect(effects, beacon.getSecondaryEffect());
            }
        }
    }
    
    //public int countMergedBeacons() {

    //    Set<Claim> claims = getMergedClaims();
    //    ArrayList<Vector4> beacons = new ArrayList<>();
    //    
    //    for (Claim claim : claims) {
    //        for (Vector4 pos : claim.beacons) {
    //            Block block = pos.getBlock();
    //            if (block == null || !(block.getType() == Material.BEACON)) {continue;}

    //            boolean allow = true;
    //            for (Vector4 beacon : beacons) {
    //                if (beacon.equals(pos)) {
    //                    allow = false;
    //                    break;
    //                }
    //            }
    //            if (allow) {
    //                beacons.add(pos);
    //            }
    //        }
    //    }

    //    return beacons.size();
    //}

    //public ArrayList<PotionEffect> getBuffs() {

    //    ArrayList<PotionEffect> effects = new ArrayList<>();
    //    Set<Claim> claims = getMergedClaims();
    //    
    //    for (Claim claim : claims) {
    //        for (Vector4 pos : claim.beacons) {
    //            Block block = pos.getBlock();
    //            if (block == null || !(block.getType() == Material.BEACON)) {continue;}
    //            Beacon beacon = (Beacon) block.getState();

    //            if (beacon.getPrimaryEffect() != null) {
    //                effects.add(beacon.getPrimaryEffect());
    //            }

    //            if (beacon.getSecondaryEffect() != null) {
    //                effects.add(beacon.getSecondaryEffect());
    //            }
    //        }
    //    }

    //    return effects;
    //}

	
	/**
	 * Removes this ownable.
	 */
	public void remove() {
		ClaimsTable.remove(this);
		return;
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
	public static Claim getOwnable(Block block) { // gets a container from block
		
		if (block.getType() == Material.LODESTONE) 
			return ClaimsTable.getOwnable(new Vector4(block));
		else return null;
	}
	
	public static Claim getOwnable(Vector4 v) {
		return ClaimsTable.getOwnable(v);
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
	
	public Set<PlayerData> getMembers() {
		return members;
	}
	
	public Security getAccess(PlayerData player) {
		
        if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return Security.ADMIN;
        }

		if (placer == null) {
			return Security.BLOCKED;
		}
		
		if (player == placer) {
			return Security.ADMIN;
		} else if (members.contains(player)) {
			return Security.MEMBER;
		} else {
			return Security.BLOCKED;
		}
	};
}

















