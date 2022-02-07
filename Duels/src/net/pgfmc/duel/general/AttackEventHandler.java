package net.pgfmc.duel.general;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.duel.general.Duel.DuelState;
import net.pgfmc.duel.general.Duel.PlayerState;

public class AttackEventHandler implements Listener {
	
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
		
		if (!(e.getDamager() instanceof Player)) {
			
			Player attacker = (Player) e.getDamager();
			
			if (!(e.getEntity() instanceof Player)) return; // gets all players in the situation
				
			Player target = (Player) e.getEntity();
			
			// if in a battle already -- V
			
			if (!(target.getGameMode() == GameMode.SURVIVAL && attacker.getGameMode() == GameMode.SURVIVAL)) return; // makes sure both players are in survival
				
			PlayerData apd = PlayerData.getPlayerData(attacker);
			PlayerData tpd = PlayerData.getPlayerData(target);
			Duel ATK = apd.getData("duel");
			Duel DEF = tpd.getData("duel");
			
			if (ATK == DEF) {
				
				if (ATK == null && isHoldingSword(attacker)) { // if both are null : create Request
					
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
				} else if (ATK != null && ATK.getState() == DuelState.INBATTLE 
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
			} else if (ATK == null && DEF.getPlayers().get(PlayerData.getPlayerData(target)) == PlayerState.DUELING) { 
				e.setCancelled(true);
				DEF.join(PlayerData.getPlayerData(attacker));
				return;
				
			}
		
			if (!target.getUniqueId().equals(attacker.getUniqueId()))
			{
				e.setDamage(0);
			}
			
		}
	}
	
	private static boolean isHoldingSword(Player player) {
		Material mainHand = player.getInventory().getItemInMainHand().getType(); // used to make code more compact
		return(mainHand == Material.IRON_SWORD 
				|| mainHand == Material.DIAMOND_SWORD 
				|| mainHand == Material.GOLDEN_SWORD 
				|| mainHand == Material.STONE_SWORD 
				|| mainHand == Material.NETHERITE_SWORD 
				|| mainHand == Material.WOODEN_SWORD
				|| mainHand == Material.DIAMOND_AXE
				|| mainHand == Material.GOLDEN_AXE
				|| mainHand == Material.IRON_AXE
				|| mainHand == Material.STONE_AXE
				|| mainHand == Material.WOODEN_AXE
				|| mainHand == Material.NETHERITE_AXE
				);
	}
	
}
