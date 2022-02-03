package net.pgfmc.teams.duel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.Main;

/**
 * Class that instantiates a Duel;
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 *
 */
public class Duel {
	
	private World world;
	
	private Map<PlayerData, PlayerState> Players = new HashMap<>();
	
	private static Set<Duel> instances = new HashSet<>();
	
	private DuelState state = DuelState.BATTLEPENDING;
	
	/**
	 * An enum to represent each stage in a duel;
	 * 
	 * <REQUESTPENDING> is when the duel challenge hasn't been accepted yet.
	 * <BATTLEPENDING> is when the duel has been accepted and the duel is about to start
	 * <INBATTLE> is when the duel has started!
	 * <TIMEOUT> idk
	 */
	public enum DuelState {
		BATTLEPENDING,
		INBATTLE,
		END
	}
	
	public enum PlayerState {
		BYSTANDER,
		JOINING,
		DUELING,
		KILLED
	}
	
	/**
	 * creates a new duel, where PR and CH are who begins the duel.
	 * @param PR player PR's PlayerData
	 * @param CH player CH's PlayerData
	 */
	public Duel(PlayerData PR, PlayerData CH) {
		
		world = PR.getPlayer().getWorld();
		join(PR);
		join(CH);
		
		Duel d = this;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				d.setState(DuelState.INBATTLE);
			}
		}, 60);
	}
	
	public DuelState getState() { // returns the state of the duel
		return(state);
	}
	
	public void setState(DuelState gimmer) { // Changes the state
		state = gimmer;
	}
	
	public Map<PlayerData, PlayerState> getPlayers() {
		return(Players);
	}
	
	public World getWorld() {
		return(world);
	}
	
	/**
	 * Prepares the player for the duel.
	 * @param pd The playerData of the player that is joining.
	 */
	public void join(PlayerData pd) {
		
		pd.setData("duelHit", false);
		pd.setData("duel", this);
		Players.put(pd, PlayerState.JOINING);
		
		Player p = pd.getPlayer();
		
		p.setHealth(20.0); // -------------------sets health to full, restores all hunger, and increases saturation
		p.setFoodLevel(20);
		p.setSaturation(2);
		
		// The Duel begin animation.
		p.sendTitle("§c3", "", 2, 16, 2); // ------------------------------------------------------- onscreen animations and countdown
		
		HashMap<String, Integer> introAnimation = new HashMap<>();
		introAnimation.put("§c2", 20);
		introAnimation.put("§c1", 40);
		introAnimation.put("§6D    U    E    L    !", 60);
		introAnimation.put("§6D   U   E   L   !", 62);
		introAnimation.put("§6D  U  E  L  !", 64);
		introAnimation.put("§6D U E L !", 66);
		introAnimation.put("§6DUEL!", 68);
		
		for (String key : introAnimation.keySet()) { // runs through the list
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					p.sendTitle(key, "", 0, 20, 0);
				}
			}, introAnimation.get(key));
		}
		
		// Queues the state change from JOINING to DUELING
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				Players.put(pd, PlayerState.DUELING);
			}
		}, 60);
	}
	
	public void playerDie(PlayerData ded, PlayerData killer, boolean wasKilled) {
		
		// restores their health if they are still in the game.
		if (ded.getPlayer() != null) {
			Player gamer = ded.getPlayer();
			
			gamer.setHealth(20.0);
			
			if (!wasKilled) {
				if (gamer.getBedSpawnLocation() != null) {
					gamer.teleport(gamer.getBedSpawnLocation());
				} else {
					gamer.teleport(gamer.getWorld().getSpawnLocation());
				}
			}
		}
		
		Players.put(ded, PlayerState.KILLED);
		
		// all alive players in the duel.
		List<PlayerData> HELLOGAMERS = Players.entrySet().stream()
			.filter(x -> {
				return (x.getValue() == PlayerState.DUELING || x.getValue() == PlayerState.JOINING);
			})
			.map(x -> {
				return x.getKey();
			})
			.collect(Collectors.toList());
			
		// Player duel Death messages for everyone in the duel.
		for (PlayerData pd : HELLOGAMERS) {
			
			if (killer != null) {
				pd.sendMessage("§n" + killer.getRankedName() + " §r§6defeated §n" + ded.getRankedName() + "§r§6!");
				pd.playSound(Sound.ENTITY_PLAYER_BIG_FALL);
			} else if (wasKilled) {
				pd.sendMessage("§n" + ded.getRankedName() + "§r§6 was defeated.");
				pd.playSound(Sound.ENTITY_PLAYER_BIG_FALL);
			}
		}
		
		if (HELLOGAMERS.size() == 1) { // if there is only one person left in the duel
			
			Duel d = this;
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				
				@Override
	            public void run() {
					
					PlayerData Winner = HELLOGAMERS.get(0);
					
					for (PlayerData pd : Players.keySet()) {
						if (pd.getUniqueId().toString().equals(Winner.getUniqueId().toString())) {
							continue;
						}
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
						pd.sendMessage("§n" + Winner.getRankedName() + " §r§ahas won the duel!");
					}
					
					Winner.playSound(Sound.ENTITY_PLAYER_LEVELUP);
					Winner.getPlayer().setHealth(20.0);
					
					setState(DuelState.END);
	            	
	    			for (PlayerData planar : Players.keySet()) {
	        			planar.setData("duel", null);
	        		}
	    			instances.remove(d);
	            }
	        }, 3); // .15 seconds
		}
		
		for (PlayerData planar : Players.keySet()) {
			planar.setData("duel", null);
		}
		instances.remove(this);
	}
	
	public void sendMessage(String message) {
		for (PlayerData pd : Players.keySet()) {
			pd.sendMessage(message);
		}
	}
	
	public void duelKick(PlayerData simp) { // ends the duel, and restores health
		playerDie(simp, null, false);
	}
}
