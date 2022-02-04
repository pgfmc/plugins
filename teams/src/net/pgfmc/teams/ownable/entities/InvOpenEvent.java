package net.pgfmc.teams.ownable.entities;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Lock;

public class InvOpenEvent implements Listener {
	
	@EventHandler
	public void openEvent(InventoryOpenEvent e) {
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getPlayer() instanceof Player && e.getInventory().getHolder() instanceof Entity) {
			
			Bukkit.getLogger().warning(((Entity) e.getInventory().getHolder()).getUniqueId().toString());
			
			OwnableEntity cont = OwnableEntity.getContainer((Entity) e.getInventory().getHolder());
			
			if (cont != null) {
				
				PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) e.getPlayer());
				Entity entity = cont.getEntity();
				
				
				if ((entity.getType() == EntityType.MINECART_CHEST || 
					entity.getType() == EntityType.MINECART_HOPPER ||
					entity.getType() == EntityType.ITEM_FRAME ||
					entity.getType() == EntityType.GLOW_ITEM_FRAME ||
					entity.getType() == EntityType.ARMOR_STAND ||
					entity.getType() == EntityType.HORSE ||
					entity.getType() == EntityType.DONKEY ||
					entity.getType() == EntityType.MULE
					
						// if the entity has an inventory.
					
					)) {
					
					switch(cont.getAccess(pd)) {
					
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
								
								pd.sendMessage("§6Fully locked!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.LOCKED);
								return;
								
							default:
								return;
							
							}
						}
						return;
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
						}
						return;
					}
					
					case FRIEND: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							e.setCancelled(true);
							
							pd.sendMessage("§cAccess denied.");
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
							return;
						}
					}
					case UNLOCKED: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							e.setCancelled(true);
							
							pd.sendMessage("§cAccess denied.");
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
							return;
						}
					}
					
					case DISALLOWED: {
						e.setCancelled(true);
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
						EntityType mat = entity.getType();
						
						switch(mat) {
						
							case MINECART_CHEST: pd.sendMessage("§cThis Minecart Chest is locked!"); return;
							case MINECART_HOPPER: pd.sendMessage("§cThis Minecart Hopper is locked!"); return;
							case ITEM_FRAME: pd.sendMessage("§cThis Item Frame is locked!"); return;
							case GLOW_ITEM_FRAME: pd.sendMessage("§cThis Item Frame is locked!"); return;
							case ARMOR_STAND: pd.sendMessage("§cThis Armor Stand is locked!"); return;
							case HORSE: pd.sendMessage("§cThis Horse is locked!"); return;
							case DONKEY: pd.sendMessage("§cThis Donkey is locked!"); return;
							case MULE: pd.sendMessage("§cThis Mule is locked!"); return;
							
							default: /*	String name = mat.name();

										
				
										name = name.toLowerCase();
										name = name.replace("_", " ");
										String[] list = name.split(" ");
							
										name = "";
										for (String string : list) {
								
											char[] charArray = string.toCharArray();
											charArray[0] = Character.toUpperCase(charArray[0]);
											name = name + new String(charArray) + " ";
										}
										name = name.stripTrailing();
										player.sendMessage("§cThis " + name + " is locked!"); */ 
										return;
						}
					}
					case EXCEPTION: Bukkit.getLogger().warning("cont.isAllowed() returned Security.EXCEPTION!");
					}
				}
			}
			Bukkit.getLogger().warning("out 1");
		}
	}
}
