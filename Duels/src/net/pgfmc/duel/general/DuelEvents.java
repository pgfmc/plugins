package net.pgfmc.duel.general;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.duel.general.Duel.DuelState;
import net.pgfmc.duel.general.Duel.PlayerState;

/**
 * Class that manages Events relating to duels.;
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 *
 */
public class DuelEvents implements Listener {
	
	@EventHandler
	public void noDamage(EntityDamageEvent e) { // --------------------- sends a player back to their spawn point after they get defeated in a duel
		

		if (!(e.getEntity() instanceof Player)) return;
		
		Player gamer = (Player) e.getEntity();
		
		Duel BlakeIsBest = PlayerData.from(gamer).getData("duel");
		
		if (BlakeIsBest == null) return;
		if (!((BlakeIsBest.getState() == DuelState.BATTLEPENDING ||  BlakeIsBest.getState() == DuelState.INBATTLE)
				&& gamer.getGameMode() == GameMode.SURVIVAL)) return;
			
		if (e.getFinalDamage() < gamer.getHealth()) return; // if they would die on the next hit
			
		if (!(boolean) PlayerData.from(gamer).getData("duelHit") && (
				e.getCause() == DamageCause.FALL
				|| e.getCause() == DamageCause.LAVA
				|| e.getCause() == DamageCause.HOT_FLOOR
				|| e.getCause() == DamageCause.SUICIDE)) { // if the player wasnt hit by a player, the damage would kill them normally.
			BlakeIsBest.playerDie(PlayerData.from(gamer), null, false);
			return;
		}
		
		e.setCancelled(true);
		
		BlakeIsBest.playerDie(PlayerData.from(gamer), null, true);
		
	}
	
	@EventHandler
	public void inventoryRestorerPt1(PlayerQuitEvent e) { // method for when a player in a duel leaves the server
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		Duel d = pd.getData("duel");
		if (d != null) {
			d.playerDie(pd, null, false);
		}
	}
	
	@EventHandler
	public void dropsItem(PlayerDropItemEvent e) { //when someone drops an item in battle
		
		PlayerData simp = PlayerData.from(e.getPlayer());
		Duel BlakeIsBest = simp.getData("duel");
		Material mainHand = e.getItemDrop().getItemStack().getType();
		
		if (BlakeIsBest != null && (BlakeIsBest.getState() == DuelState.INBATTLE || BlakeIsBest.getState() == DuelState.BATTLEPENDING) && (mainHand == Material.IRON_SWORD || mainHand == Material.DIAMOND_SWORD || mainHand == Material.GOLDEN_SWORD || mainHand == Material.STONE_SWORD || mainHand == Material.NETHERITE_SWORD || mainHand == Material.WOODEN_SWORD)) {
			
			e.setCancelled(true);
			BlakeIsBest.playerDie(simp, null, false);
			BlakeIsBest.sendMessage("�n" + simp.getRankedName() + " �r�cforfeits the Duel!");
		}
	}
	
	@EventHandler
	public void interdimensionBlock(PlayerChangedWorldEvent e) { // cancels the duel if one person goes into another dimension / hub 
		PlayerData simp = PlayerData.from(e.getPlayer());
		
		Duel BlakeIsBest = simp.getData("duel");
		
		if (BlakeIsBest != null) {
			
			if (e.getPlayer().getLocation().getWorld() != BlakeIsBest.getWorld()) {
				BlakeIsBest.playerDie(simp, null, false);
				simp.sendMessage("�cYou have left the �lDuel �r�c because you entered a different world!");
				BlakeIsBest.sendMessage("�n" + simp.getRankedName() + "�r�c has forfeit.");
			}
		}
	}
	
	@EventHandler
	public void creativeModeGamg(PlayerGameModeChangeEvent e) { // kicks the player from the duel if they exit survival mode
		PlayerData simp = PlayerData.from(e.getPlayer());
		Duel BlakeIsBest = simp.getData("duel");
		
		if (BlakeIsBest != null) {
			
			if (e.getNewGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
				BlakeIsBest.playerDie(simp, null, false);
				simp.sendMessage("�cYou have left the �lDuel �r�c because you changed your gamemode!");
				BlakeIsBest.sendMessage("�n" + simp.getRankedName() + " �r�chas been disqualified.");
			}
		}
	}
	
	@EventHandler
	public void deAggro(EntityTargetLivingEntityEvent e) { // -------------- disables aggro if a mob targets a player in a duel
	
		if (e.getTarget() instanceof Player && e.getEntity() instanceof Monster) {
			
			PlayerData simp = PlayerData.from((Player) e.getTarget());
			Duel BlakeIsBest = simp.getData("duel");
			
			if (BlakeIsBest != null) {
				
				if (BlakeIsBest.getPlayers().get(simp) == PlayerState.DUELING || BlakeIsBest.getPlayers().get(simp) == PlayerState.JOINING) {
					e.setCancelled(true);
				}
			}
		}
	}
}
