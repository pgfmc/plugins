package net.pgfmc.friends.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.friends.data.FriendRequest;

public class AttackEventHandler implements Listener {
	
	@EventHandler
	public void attackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			
			Player target = (Player) e.getEntity();
			
			// if in a battle already -- V
			if (target.getGameMode() == GameMode.SURVIVAL && attacker.getGameMode() == GameMode.SURVIVAL) { // makes sure both players are in survival
				
				PlayerData apd = PlayerData.getPlayerData(attacker);
				PlayerData tpd = PlayerData.getPlayerData(target);
				
				if (isFlower(attacker.getInventory().getItemInMainHand().getType())) { // checks if the player is holding a flower
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
	
}
