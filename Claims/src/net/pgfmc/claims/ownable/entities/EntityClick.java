package net.pgfmc.claims.ownable.entities;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import net.pgfmc.claims.ownable.Ownable.Lock;
import net.pgfmc.claims.ownable.Ownable.Security;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class EntityClick implements Listener {
	
	@EventHandler
	public void EntityInteract(PlayerInteractAtEntityEvent e) {
		
		if (e.getPlayer() != null && e.getRightClicked() != null  && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			PlayerData pd = PlayerData.from(e.getPlayer());
			
			if ((e.getRightClicked().getType() == EntityType.MINECART_CHEST || 
					e.getRightClicked().getType() == EntityType.MINECART_HOPPER ||
					e.getRightClicked().getType() == EntityType.ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.ARMOR_STAND ||
					e.getRightClicked().getType() == EntityType.HORSE ||
					e.getRightClicked().getType() == EntityType.DONKEY ||
					e.getRightClicked().getType() == EntityType.MULE
				
					// if the entity has an inventory.
				
				)) {
				
				OwnableEntity cont = OwnableEntity.getContainer(e.getRightClicked());
				
				if (cont == null) { return; }
				
				Security sec = cont.getAccess(pd);
				
				switch(sec) {
				
				case OWNER: {
					if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
						
						// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
						
						switch(cont.getLock()) {
						case LOCKED:
							
							e.setCancelled(true);
							
							pd.sendMessage("§6Favorites only!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FAVORITES_ONLY);
							return;
							
						case FAVORITES_ONLY:
							e.setCancelled(true);
							
							pd.sendMessage("§6Friends only!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FRIENDS_ONLY);
							return;
							
						case FRIENDS_ONLY:

							e.setCancelled(true);
							
							pd.sendMessage("§6Unlocked!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.UNLOCKED);
							return;
							
						case UNLOCKED:
							
							e.setCancelled(true);
							
							pd.sendMessage("§6Set locked.");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.LOCKED);
							return;
							
						default:
							return;
						
						}
						
					} else {
						return;
					}
				}
				case FAVORITE: {
					if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
						
						// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
						
						switch(cont.getLock()) {
						case LOCKED:
							
							e.setCancelled(true);
							
							pd.sendMessage("§cAccess denied.");
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
							return;
							
						case FAVORITES_ONLY:
							e.setCancelled(true);
							
							pd.sendMessage("§6Friends only!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FRIENDS_ONLY);
							return;
							
						case FRIENDS_ONLY:

							e.setCancelled(true);
							
							pd.sendMessage("§6Unlocked!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.UNLOCKED);
							return;
							
						case UNLOCKED:
							
							e.setCancelled(true);
							
							pd.sendMessage("§6Favorites only!");
							pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FAVORITES_ONLY);
							return;
							
						default:
							return;
						}
						
					} else {
						return;
					}
				}
					
				
				case FRIEND: 
					if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
					
					e.setCancelled(true);
					
					pd.sendMessage("§cAccess Denied.");
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					return;
				}
				
				case UNLOCKED: 
					if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
						
						e.setCancelled(true);
						
						pd.sendMessage("§cAccess Denied.");
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						return;
					}
				
				case DISALLOWED: {
					e.setCancelled(true);
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
					EntityType mat = e.getRightClicked().getType();
					
					switch(mat) {
					
						case MINECART_CHEST: pd.sendMessage("§cThis Minecart Chest is locked!"); return;
						case MINECART_HOPPER: pd.sendMessage("§cThis Minecart Hopper is locked!"); return;
						case ITEM_FRAME: pd.sendMessage("§cThis Item Frame is locked!"); return;
						case GLOW_ITEM_FRAME: pd.sendMessage("§cThis Item Frame is locked!"); return;
						case ARMOR_STAND: pd.sendMessage("§cThis Armor Stand is locked!"); return;
						case HORSE: pd.sendMessage("§cThis Horse is locked!"); return;
						case DONKEY: pd.sendMessage("§cThis Donkey is locked!"); return;
						case MULE: pd.sendMessage("§cThis Mule is locked!"); return;
						
						default: return;
					}
				}
				case EXCEPTION: Bukkit.getLogger().warning("cont.isAllowed() returned Security.EXCEPTION!");
				}
			}
		}
	}
}