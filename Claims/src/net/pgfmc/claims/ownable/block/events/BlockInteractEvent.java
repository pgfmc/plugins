package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.claims.ownable.Ownable.Lock;
import net.pgfmc.claims.ownable.Ownable.Security;
import net.pgfmc.claims.ownable.block.BlockManager;
import net.pgfmc.claims.ownable.block.OwnableBlock;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

/**
Written by CrimsonDart

-----------------------------------

Interact Event.

-----------------------------------
 */
public class BlockInteractEvent implements Listener {
	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // code block for right-clicking on a block.
		
		// controls clicking containers and beacons;
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		// Right click not air
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock()) {
			Block block = e.getClickedBlock();
			
			// Player is in survival mode
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				if (e.getMaterial() != null && e.getMaterial().toString().contains("BUCKET")) { // Disables Bucket placing in claims
					
					OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(block));
					
					if (claim != null && claim.getAccess(pd) == Security.DISALLOWED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
				}
				
				OwnableBlock cont = OwnableBlock.getOwnable(block);
				
				if (cont != null) { // if block is a container
					
					if (e.getMaterial() == Material.LEVER) { // locking feature
						cont.cycleLock(pd);
						e.setCancelled(true);
						return;
					} else {
						
						switch(cont.getAccess(pd)) {
						case DISALLOWED:
							e.setCancelled(true);
							
							switch(block.getType()) {
							
							case BARREL: pd.sendMessage("§cThis barrel is locked!"); return;
							case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); return;
							case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); return;
							case CHEST: pd.sendMessage("§cThis chest is locked!"); return;
							case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); return;
							case DROPPER: pd.sendMessage("§cThis dropper is locked!"); return;
							case FURNACE: pd.sendMessage("§cThis furnace is locked!"); return;
							case HOPPER: pd.sendMessage("§cThis hopper is locked!"); return;
							case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); return;
							case SMOKER: pd.sendMessage("§cThis smoker is locked!"); return;
							case BEACON: pd.sendMessage("§cThis beacon is locked!"); return;
							default:
								return;
							}
						case EXCEPTION:
							e.setCancelled(true);
							
							switch(block.getType()) {
							
							case BARREL: pd.sendMessage("§cThis barrel is locked!"); return;
							case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); return;
							case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); return;
							case CHEST: pd.sendMessage("§cThis chest is locked!"); return;
							case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); return;
							case DROPPER: pd.sendMessage("§cThis dropper is locked!"); return;
							case FURNACE: pd.sendMessage("§cThis furnace is locked!"); return;
							case HOPPER: pd.sendMessage("§cThis hopper is locked!"); return;
							case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); return;
							case SMOKER: pd.sendMessage("§cThis smoker is locked!"); return;
							case BEACON: pd.sendMessage("§cThis beacon is locked!"); return;
							default:
								return;
							}
						default:
							return;
						
						}
					}
					
				// creates a new Block container if the chest isnt claimed.
				} else if (BlockManager.isOwnable(block.getType())) {
					
					OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(block));
					
					if (claim != null && claim.getAccess(pd) != Security.OWNER) {
						
						pd.sendMessage("§cCannot claim this chest, as it's in claimed land.");
						e.setCancelled(true);
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						return;
					} else {
						BlockManager.createBlockContainer(pd, block);
						
					}
				}
				
				/*
				// crazy time down here!
				
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
					e.hasBlock() && 
					e.hasItem() 
					&& (((e.getItem().getType() == Material.CHEST_MINECART || 
						e.getItem().getType() == Material.HOPPER_MINECART) 
					&& (block.getType() == Material.RAIL ||
						block.getType() == Material.POWERED_RAIL ||
						block.getType() == Material.ACTIVATOR_RAIL ||
						block.getType() == Material.DETECTOR_RAIL)) 
						|| (e.getItem().getType() == Material.ITEM_FRAME ||
						e.getItem().getType() == Material.GLOW_ITEM_FRAME) 
						|| e.getItem().getType() == Material.ARMOR_STAND))
					// --------------------------------
				{
					
					OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(block));
					
					if (claim != null && claim.isAllowed(pd) == Security.DISALLOWED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
					
					// We have to create a new Entity Container!!! (idk how lol)
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			            
						@Override
			            public void run() // 60 second long cooldown, in which the plugin will wait for 
						{
							
							Block bb;
							
							if (e.getItem().getType() == Material.ITEM_FRAME ||
									e.getItem().getType() == Material.GLOW_ITEM_FRAME) {
								bb = block.getRelative(e.getBlockFace());
							} else if ((e.getItem().getType() == Material.ARMOR_STAND)) {
								
								// gets the block above where the armor stand is projected to be (because that is technically where the armor stand would be located)
								
								Block bloke = block.getRelative(e.getBlockFace()).getRelative(BlockFace.UP);
								bb = block.getRelative(e.getBlockFace());
								
								ArrayList<Entity> entities = new ArrayList<>();
								
								for (Entity entity : bloke.getChunk().getEntities()) { // gets all entities in the chunk of the block location.
									
									if (entity != null) {
										entities.add(entity);
									}
								}
								
								Stream<Entity> entityStream = entities.stream();
								
								Optional<Entity> entity = entityStream
								.filter(x -> { // gets the youngest entity at the rail's position
									return (x.getLocation().getBlock().equals(bloke) || x.getLocation().getBlock().equals(bb));})
								.reduce((t, x) -> { // reduces the filtered selection of minecart Chests / Hoppers to the one that lived the least.
									if (t == null && x.getTicksLived() == 1) {
										return x;
									} else if (t.getTicksLived() > x.getTicksLived() && x.getTicksLived() == 1) {
										return x;
									} else {
										return t;
									}
								});
								entityStream.close();
								
								if (entity.isPresent()) {
									new OwnableEntity(pd, lockMode, entity.get().getUniqueId());
									return;
								}
								
								return;
								
							} else {
								bb = block;
							}
							
							ArrayList<Entity> entities = new ArrayList<>();
							
							for (Entity entity : bb.getChunk().getEntities()) { // gets all entities in the chunk of the block location.
								
								if (entity != null) { 
									entities.add(entity);
								}
							}
							
							Stream<Entity> entityStream = entities.stream();
							
							
							Optional<Entity> entity = entityStream.filter(x -> { // gets the youngest entity at the rail's position
								return x.getLocation().getBlock().equals(bb);
							}).reduce((t, x) -> { // reduces the filtered selection of minecart Chests / Hoppers to the one that lived the least.
								if (t == null && x.getTicksLived() == 1) {
									return x;
								} else if (t.getTicksLived() > x.getTicksLived() && x.getTicksLived() == 1) {
									return x;
								} else {
									return t;
								}
							});
							entityStream.close();
							
							if (entity.isPresent()) {
								new OwnableEntity(pd, lockMode, entity.get().getUniqueId());
								return;
							}
							
			            }
			        }, 1);
				}
				*/
			}
		} else if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.LEVER) {
			
			switch((Lock) pd.getData("lockMode")) {
			case LOCKED:
				
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: FAVORITES ONLY");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.FAVORITES_ONLY);
				return;
				
			case FAVORITES_ONLY:
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: FRIENDS ONLY");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.FRIENDS_ONLY);
				return;	
				
			case FRIENDS_ONLY:

				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: UNLOCKED");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: LOCKED");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.LOCKED);
				return;
			default:
				return;
			}
		}
	}
}