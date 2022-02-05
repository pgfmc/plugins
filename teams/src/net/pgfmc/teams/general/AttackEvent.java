package net.pgfmc.teams.general;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.teams.duel.Duel;
import net.pgfmc.teams.duel.Duel.DuelState;
import net.pgfmc.teams.duel.Duel.PlayerState;
import net.pgfmc.teams.duel.DuelRequest;
import net.pgfmc.teams.friends.FriendRequest;
import net.pgfmc.teams.ownable.entities.OwnableEntity;

/**
 * class to route events to different locations to organise better
 * @author CrimsonDart
 * @since 1.2.0
 * 
 */
public class AttackEvent implements Listener {
	
	@EventHandler
	public void attackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof Player &&
				e.getDamager() instanceof Projectile && 
				((Projectile) e.getDamager()).getShooter() instanceof Player) {
			
			Player attacker = (Player) ((Projectile) e.getDamager()).getShooter();
			Player target = (Player) e.getEntity();
			
			if (attacker.getUniqueId().equals(target.getUniqueId())) return;
			
			if (target.getGameMode() == GameMode.SURVIVAL && attacker.getGameMode() == GameMode.SURVIVAL) { // makes sure both players are in survival
				
				PlayerData apd = PlayerData.getPlayerData(attacker);
				PlayerData tpd = PlayerData.getPlayerData(target);
				Duel ATK = apd.getData("duel");
				
				if (ATK != null && ATK == tpd.getData("duel") && ATK.getState() == DuelState.INBATTLE 
						&& ATK.getPlayers().get(apd) == PlayerState.DUELING
						&& ATK.getPlayers().get(tpd) == PlayerState.DUELING) { // if same Duel : allow attack
					
					if (e.getFinalDamage() >= target.getHealth()) {
						
						e.setDamage(0);
						ATK.playerDie(tpd, apd, true);
						return;
					}
					
					if (e.getDamager().getType() == EntityType.ARROW) {
						apd.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME);
					}
					
					tpd.setData("duelHit", true);
					return;
				}
			}
			e.setCancelled(true);
		}
		
		if (e.getDamager() instanceof Player) {
			
			Player attacker = (Player) e.getDamager();
			
			if (e.getEntity() instanceof Player) { // gets all players in the situation
				
				Player target = (Player) e.getEntity();
				
				// if in a battle already -- V
				
				if (target.getGameMode() == GameMode.SURVIVAL && attacker.getGameMode() == GameMode.SURVIVAL) { // makes sure both players are in survival
					
					PlayerData apd = PlayerData.getPlayerData(attacker);
					PlayerData tpd = PlayerData.getPlayerData(target);
					Duel ATK = apd.getData("duel");
					Duel DEF = tpd.getData("duel");
					
					boolean equal = (ATK == DEF);
					boolean ATKnull = (ATK == null);
					
					if (equal) {
						
						if (ATKnull && isHoldingSword(attacker)) { // if both are null : create Request
							
							
							
							
							Request r = DuelRequest.DR.findRequest(tpd, apd);
							
							if (r == null) {
								
								DuelRequest.DR.createRequest(apd, tpd);
								
								e.setCancelled(true);
								return;
								
							} else {
								r.end(EndBehavior.ACCEPT);
								e.setCancelled(true);
								return;
								
							}
						} else if (!ATKnull && ATK.getState() == DuelState.INBATTLE 
								&& ATK.getPlayers().get(apd) == PlayerState.DUELING
								&& ATK.getPlayers().get(tpd) == PlayerState.DUELING) { // if same Duel : allow attack
							
							if (e.getFinalDamage() >= target.getHealth()) {
								
								e.setDamage(0);
								ATK.playerDie(tpd, apd, true);
								return;
							}
							
							tpd.setData("duelHit", true);
							
							return;
							
						}
					} else if (ATKnull && DEF.getPlayers().get(PlayerData.getPlayerData(target)) == PlayerState.DUELING) { 
						e.setCancelled(true);
						DEF.join(PlayerData.getPlayerData(attacker));
						return;
						
					} else if (isFlower(attacker.getInventory().getItemInMainHand().getType())) { // checks if the player is holding a flower
						
						
						
						Request r = FriendRequest.FR.findRequest(tpd, apd);
						
						if (r != null) {
							r.end(EndBehavior.ACCEPT);
						} else {
							
							FriendRequest.FR.createRequest(apd, tpd);
							
						}
						e.setCancelled(true);
						return;
					}

				}
				
				if (!target.getUniqueId().equals(attacker.getUniqueId()))
				{
					e.setDamage(0);
				}
				
			} else if (e.getEntity() instanceof InventoryHolder && OwnableEntity.getContainer(e.getEntity().getUniqueId()) != null)  {
				
				switch(OwnableEntity.getContainer(e.getEntity()).getAccess(PlayerData.getPlayerData(attacker))) {
				case OWNER: return;
				case FAVORITE: return;
				case FRIEND: return;
				default: {
					e.setCancelled(true);
					return;
				}
				}
			}
		}
	}
	
	/**
	 * Pass in any Material; then it will return if it's a flower or not.
	 * @param material Passed in Material.
	 * @return Wether or not the Material is a flower. (includes 2 block high flowers)
	 */
	private static boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		return (material == Material.BLUE_ORCHID 
				|| material == Material.ROSE_BUSH 
				|| material == Material.DANDELION 
				|| material == Material.ORANGE_TULIP 
				|| material == Material.PINK_TULIP 
				|| material == Material.RED_TULIP 
				|| material == Material.WHITE_TULIP 
				|| material == Material.SUNFLOWER 
				|| material == Material.OXEYE_DAISY 
				|| material == Material.POPPY 
				|| material == Material.ALLIUM 
				|| material == Material.AZURE_BLUET 
				|| material == Material.CORNFLOWER 
				|| material == Material.LILY_OF_THE_VALLEY 
				|| material == Material.WITHER_ROSE 
				|| material == Material.PEONY 
				|| material == Material.LILAC);
	}
	
	private static boolean isHoldingSword(Player player) {
		Material mainHand = player.getInventory().getItemInMainHand().getType(); // used to make code more compact
		return(mainHand == Material.IRON_SWORD 
				|| mainHand == Material.DIAMOND_SWORD 
				|| mainHand == Material.GOLDEN_SWORD 
				|| mainHand == Material.STONE_SWORD 
				|| mainHand == Material.NETHERITE_SWORD 
				|| mainHand == Material.WOODEN_SWORD);
	}
}
