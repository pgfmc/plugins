package net.pgfmc.survival.inventories.masterbook;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.inventoryapi.BaseInventory;
import net.pgfmc.core.inventoryapi.ConfirmInventory;
import net.pgfmc.core.inventoryapi.ListInventory;
import net.pgfmc.core.inventoryapi.extra.Butto;
import net.pgfmc.core.inventoryapi.extra.ItemWrapper;
import net.pgfmc.core.permissions.Roles.PGFRole;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.inv.RequestListInventory;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.commands.Afk;
import net.pgfmc.survival.teleport.home.Homes;

public class CommandsMenu extends BaseInventory {
	
	public CommandsMenu(PlayerData pd)
	{
		super(27, "Command Menu");
		
		/* 
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (pd.hasPermission("pgf.cmd.link"))
		{
			if (pd.getData("Discord") != null)
			{
				setOptionalAction(2, new ConfirmInventory("§r§8Unlink Account?","§dUnlink Discord","§r§7Cancel") {

					@Override
					protected void confirmAction(Player p, InventoryClickEvent e) {
						p.closeInventory();
						p.performCommand("unlink");
						p.openInventory(new CommandsMenu(pd).getInventory());
						
					}

					@Override
					protected void cancelAction(Player p, InventoryClickEvent e) {
						p.openInventory(new CommandsMenu(pd).getInventory());
						
					}
					
				}, null);
				
				setItem(2, Material.AMETHYST_SHARD).n("§dUnlink Discord");
			
			} else {
				
				setAction(2, (p, e) -> {
					p.closeInventory();
					p.performCommand("link");
				});
				setItem(2, Material.QUARTZ).n("§dLink Discord");
				
			}
			
		}
		
		/* 
		 * [] [] [] XX [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * toggles AFK in-menu
		 */
		if (pd.hasPermission("pgf.cmd.afk"))
		{
			if (Afk.isAfk(pd.getPlayer()))
			{
				setAction(3, (p, e) -> {
					disableAFK(this);
					p.performCommand("afk");
				});
				
				setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
				
			} else {
				setAction(3, (p, e) -> {
					enableAFK(this);
					p.performCommand("afk");
				});
				
				setItem(3, Material.ICE).n("§r§7AFK: §cDisabled").l("§r§7Click to enable!");
				
			}
			
		}
		
		/* 
		 * [] [] [] [] [] XX [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * Back command
		 */
		if (pd.hasPermission("pgf.cmd.back"))
		{
			setOptionalAction(23, new ConfirmInventory("§r§8Tp to last location?", "§r§dTeleport", "§r§7Cancel") {
				
				@Override
				protected void confirmAction(Player p, InventoryClickEvent e) {
					p.closeInventory();
					p.performCommand("back");
					
				}

				@Override
				protected void cancelAction(Player p, InventoryClickEvent e) {
					p.closeInventory();
					//p.openInventory(new CommandsMenu(pd).getInventory());
					
				}
				
			}, null);
			
			ItemStack item = new ItemWrapper(Material.TIPPED_ARROW).n("§r§4Back").l("§r§7Go back to your last location").gi();
			
			PotionMeta pot = (PotionMeta) item.getItemMeta();
			PotionData potion = new PotionData(PotionType.INSTANT_DAMAGE);
			pot.setBasePotionData(potion);
			item.setItemMeta(pot);
			
			setItem(23, item);
		}
		
		/* 
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * home menu
		 */
		setOptionalAction(20, new BaseInventory(27, "§r§8Home") {
			}, bi -> {
			final HashMap<String, Location> homes = Homes.getHomes(pd);
			
			//bi.setBack(0, new CommandsMenu(pd));
			
			bi.setOptionalAction(13, new ListInventory<String>(27, "§r§8Home Select") {

				@Override
				public List<String> load() {
					return Homes.getHomes(pd).keySet().stream().collect(Collectors.toList());
				}
				
				@Override
				protected Butto toAction(String entry) {
					
					if (!pd.hasPermission("pgf.cmd.home.home"))
					{
						return (p, e) -> {
							pd.sendMessage("§cYou don't have permission to execute this command.");
						};
						
					}
					
					if (homes.size() == 0)
					{
						return (p, e) -> {
							pd.sendMessage("§cYou do not have any homes.");
						};
						
					}
					
					return (p, e) -> {
						p.performCommand("home " + entry);
						p.closeInventory();
					};
					
				}
				
				@Override
				protected ItemStack toItem(String entry) {
					return new ItemWrapper(Material.PAPER).n(entry).gi();
				}
				
				
			}, bii -> {
				//bii.setBack(0, new CommandsMenu(pd));
				
			});
			
			bi.setItem(13, Material.ENDER_PEARL).n("§r§dGo to Home");
			
			bi.setOptionalAction(11, new ConfirmInventory("§r§8Set home here?", "§r§aSet Home", "§r§7Cancel") {

				@Override
				protected void confirmAction(Player p, InventoryClickEvent e) {
					
					if (!p.hasPermission("pgf.cmd.home.set"))
					{
						p.sendMessage("§cYou don't have permission to execute this command.");
						return;
					}
					
					if (p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 5)
					{
						p.sendMessage("§cYou can only have up to 5 homes: " + Homes.getNamedHomes(pd));
						return;
					} else if (!p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 3)
					{
						p.sendMessage("§cYou can only have up to 3 homes: " + Homes.getNamedHomes(pd));
						return;
					}
					
					pd.setData("tempHomeLocation", pd.getPlayer().getLocation()); // TODO O_O
					p.closeInventory();
					
					pd.sendMessage("§r§dType into chat to set the name of your Home!");
					pd.sendMessage("§r§dYou can only name the home for 4 minutes.");
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						
						@Override
						public void run()
						{
							if (pd.getData("tempHomeLocation") == null) return;
							
							pd.setData("tempHomeLocation", null);
							pd.sendMessage("§r§cYour home could not be set.");
							
						}
						
					}, 20 * 60 * 4);
					
				}

				@Override
				protected void cancelAction(Player p, InventoryClickEvent e) {
					//p.openInventory(new CommandsMenu(pd).getInventory());
					
				}
				
				
			}, bii -> {
				//bii.setBack(0, new CommandsMenu(pd));
				
			});
			
			bi.setItem(11, Material.OAK_SAPLING).n("§r§aSet Home");
			
			bi.setOptionalAction(15, new ListInventory<String>(27, "§r§8Delete Home") {

				@Override
				public List<String> load() {
					return Homes.getHomes(pd).keySet().stream().collect(Collectors.toList());
				}
				
				@Override
				protected Butto toAction(String entry) {
					
					if (!pd.hasPermission("pgf.cmd.home.del"))
					{
						return (p, e) -> {
							p.sendMessage("§cYou don't have permission to execute this command.");
						};
					}
					
					if (homes.size() == 0)
					{
						return (p, e) -> {
							p.sendMessage("§cYou do not have any homes.");
						};
					}
					
					return (p, e) -> {
						p.performCommand("delhome " + entry);
						p.closeInventory();
					};
					
				}
				
				@Override
				protected ItemStack toItem(String entry) {
					return new ItemWrapper(Material.PAPER).n("§r§a" + entry).gi();
				}
				
			}, bii -> {
				//bii.setBack(0, new CommandsMenu(pd));
				
			});
			
			bi.setItem(15, Material.FLINT_AND_STEEL).n("§r§cDelete Home");
			
		});
		
		setItem(20, Material.COMPASS).n("§r§eHomes");
		
		/* 
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] XX [] [] [] [] []
		 * home menu
		 */
		if (pd.hasPermission("pgf.cmd.tp.tpa"))
		{
			if (Bukkit.getOnlinePlayers().size() == 1)
			{
				setAction(21, (p, e) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				});
				
				setItem(21, Material.GRAY_CONCRETE).n("§r§5Tpa").l("§r§cNo players online.");
				
			} else {
				setOptionalAction(21, new ListInventory<Player>(27, "§r§8Select who to teleport to!") {

					@Override
					public List<Player> load() {
						return Bukkit.getOnlinePlayers().stream()
								.filter(player -> !player.getUniqueId().equals(pd.getUniqueId()))
								.collect(Collectors.toList());
					}
					
					@Override
					protected Butto toAction(Player entry) {
						
						return (p, e) -> {
							p.performCommand("tpa " + entry.getName());
							p.openInventory(new CommandsMenu(pd).getInventory());
						};
						
					}
					
					@Override
					protected ItemStack toItem(Player entry) {
						return new ItemWrapper(Material.PLAYER_HEAD).n("§r§a" + entry.getName()).gi();
					}
					
				}, bi -> {
					bi.setBack(0, new CommandsMenu(pd));
				});
				
				setItem(21, Material.ENDER_PEARL).n("§r§5Tpa").l("§r§7Teleport to another player!");
				
			}
			
		}
		
		// Other buttons -
		
		/* 
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * home menu
		 */
		
		PGFRole role = (PGFRole) Optional.ofNullable(pd.getData("role")).orElse(PGFRole.MEMBER);
		/* 
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * Nickname
		 */
		setAction(4, (p, e) -> {
			if (pd.hasPermission("pgf.cmd.donator.nick"))
			{
				pd.addTag("nick"); // TODO make this better or soemthing
				p.closeInventory();
				p.sendMessage("§9Type your new nickname in chat.");
				
			} else {
				p.sendMessage("§cYou need donator for that!");
				
			}
			
		});
		
		setItem(4, Skull.getHead(pd.getUniqueId(), null))
				.n(pd.getRankedName() + " (" + role.getName().substring(0,1).toUpperCase() + role.getName().substring(1).toLowerCase() + ")")
				.l("§7Change nickname!");
		
		/* 
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * home menu
		 */
		if (pd.hasPermission("pgf.cmd.donator.echest"))
		{
			setAction(22, (p, e) -> {
				p.closeInventory();
				p.performCommand("echest");
			});
			
			setItem(22, Material.ENDER_CHEST).n("§r§3Ender Chest").l("§r§9Donator perk!");
			
		}
		
		/* 
		 * [] [] [] [] [] [] [] [] []
		 * XX [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * Requests
		 */
		setAction(24, (p, e) -> {
			RequestListInventory inv = new RequestListInventory(PlayerData.from(p));
			setBack(0, new CommandsMenu(pd));
			
			p.openInventory(inv.getInventory());
			
		});
		
		setItem(24, Material.LEVER).n("§r§4Requests");
		
		/* 
		 * [] [] [] XX [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * toggles PVP in-menu
		 */
		if (pd.hasPermission("pgf.cmd.pvp"))
		{
			if (pd.hasTag("pvp"))
			{
				setAction(5, (p, e) -> {
					enablePVP(this);
					p.performCommand("pvp");
				});
				
				setItem(5, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
				
			} else {
				setAction(5, (p, e) -> {
					disablePVP(this);
					p.performCommand("pvp");
				});
				
				setItem(5, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
				
			}
			
		}
		
		 if (pd.hasPermission("pgf.cmd.lodestone"))
		 {
			 if (pd.hasTag("loded"))
			 {
				 setItem(6, Material.GRAY_CONCRETE).n("§cNo Rewards!");
				 
			 } else {
				 setItem(6, Material.LODESTONE).n("§aGet a free Lodestone!").l("§8Lodestones can be used to claim land.");
				 
				 setAction(6, (p, e) -> {
					 pd.getPlayer().performCommand("getclaim");
					 p.closeInventory();
					 
				 });
				 
			 }
			 
		 }
		 
	}
	
	private static void enablePVP(BaseInventory inventory)
	{
		inventory.setAction(5, (p, e) -> {
			disablePVP(inventory);
			p.performCommand("pvp");
		});
		
		inventory.setItem(5, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
		
	}
	
	private static void disablePVP(BaseInventory inventory)
	{
		inventory.setAction(5, (p, e) -> {
			enablePVP(inventory);
			p.performCommand("pvp");
		});
		
		inventory.setItem(5, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
		
	}
	
	private static void enableAFK(BaseInventory inventory)
	{
		inventory.setAction(3, (p, e) -> {
			disableAFK(inventory);
			p.performCommand("afk");
		});
		
		inventory.setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
		
	}
	
	private static void disableAFK(BaseInventory inventory)
	{
		inventory.setAction(3, (p, e) -> {
			enableAFK(inventory);
			p.performCommand("afk");
		});
		
		inventory.setItem(3, Material.ICE).n("§r§7AFK: §cDisabled").l("§r§7Click to enable!");
		
	}
	
}
