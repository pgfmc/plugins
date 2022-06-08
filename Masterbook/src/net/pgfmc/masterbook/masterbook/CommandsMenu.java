package net.pgfmc.masterbook.masterbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.ListInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.permissions.Roles.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.inv.RequestListInventory;
import net.pgfmc.core.util.DimManager;
import net.pgfmc.masterbook.Main;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.teleport.home.Homes;

public class CommandsMenu implements InventoryHolder {
	
	private PlayerData pd;
	
	public CommandsMenu(PlayerData pd) {
		this.pd = pd;
	}
	
	public class Homepage extends BaseInventory {
		
		public Homepage() {
			super(27, "Command Menu");
			
			/* 
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (pd.hasPermission("pgf.cmd.link")) {
				
				if (pd.getData("Discord") != null) {
					
					setAction(2, (p, e) -> {
						p.openInventory(new DiscordConfirm(pd).getInventory());
					});
					
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
			if (pd.hasPermission("pgf.cmd.afk")) {
				
				if (Afk.isAfk(pd.getPlayer())) {
					
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
			if (pd.hasPermission("pgf.cmd.back")) {
				
				setAction(5, (p, e) -> {
					p.openInventory(new BackConfirm(pd).getInventory());
				});
				
				setItem(5, Material.ARROW).n("§r§4Back").l("§r§7Go back to your last location");
			}
			
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * /dim command
			 */
			if (pd.hasPermission("pgf.cmd.goto")) {
				
				setAction(13, (p, e) -> {
					p.openInventory(new DimSelect(pd).getInventory());
				});
				setItem(13, Material.SPYGLASS).n("§r§9Dimensions").l("§r§7Go to other worlds!");
			}
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 * home menu
			 */
			setAction(20, (p, e) -> {
				p.openInventory(new HomeMenu(pd).getInventory());
			});
			setItem(20, Material.COMPASS).n("§r§eHomes");
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] XX [] [] [] [] []
			 * home menu
			 */
			if (pd.hasPermission("pgf.cmd.tp.tpa")) {
				
				if (Bukkit.getOnlinePlayers().size() == 1) {
					
					setAction(21, (p, e) -> {
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
					});
					setItem(21, Material.GRAY_CONCRETE).n("§r§5Tpa").l("§r§cNo players online.");
					
				} else {
					
					setAction(21, (p, e) -> {
						p.openInventory(new TpaList().getInventory());
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
			
			Role topRole = Roles.getTop(pd.getOfflinePlayer());
			/* in case I don't like this new UI change - bk
			Material emblem = Material.RAW_COPPER;
			switch (topRole) {
			case VETERAN:
				emblem = Material.LAPIS_LAZULI;
				break;
			case DONATOR:
				emblem = Material.RAW_GOLD;
				break;
			case DOOKIE:
				emblem = Material.COCOA_BEANS;
				break;
			case STAFF:
				emblem = Material.BLACK_DYE;
				break;
			case TRAINEE:
				emblem = Material.CHORUS_FRUIT;
				break;
			case MODERATOR:
				emblem = Material.POPPED_CHORUS_FRUIT;
				break;
			case DEVELOPER:
				emblem = Material.EMERALD;
				break;
			case ADMIN:
				emblem = Material.DIAMOND;
				break;
			case FOUNDER:
				emblem = Material.DIAMOND;
				break;
			default: break;
			};
			*/
			/* 
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * Nickname
			 */
			setAction(4, (p, e) -> {
				if (pd.hasPermission("pgf.cmd.donator.nick"))
				{
					pd.setData("nickTemp", "reset");
					p.closeInventory();
					p.sendMessage("§9Type your new nickname in chat.");
				} else
				{
					p.sendMessage("§cYou need donator for that!");
				}
				
			});
			setItem(4, Skull.getHead(pd.getUniqueId(), null))
			.n(pd.getRankedName() + " (" + topRole.name().charAt(0) + topRole.getName().substring(1) + ")")
			.l("§7Change nickname!");
			
			
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * home menu
			 */
			if (pd.hasPermission("pgf.cmd.donator.echest")) {
				
				setAction(22, (p, e) -> {
					p.closeInventory();
					p.performCommand("echest");
				});
				setItem(22, Material.ENDER_CHEST).n("§r§3Ender Chest").l("§r§9VIP perk!");
			}
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * Requests
			 */
			setAction(9, (p, e) -> {
				
				RequestListInventory inv = new RequestListInventory(PlayerData.from(p));
				inv.setItem(0, Material.FEATHER).n("§r§cBack");
				inv.setAction(0, (player, event) -> {
					player.openInventory(new Homepage().getInventory());
				});
				
				p.openInventory(inv.getInventory());
			});
			setItem(9, Material.LEVER).n("§r§4Requests");
			
			/* 
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * toggles PVP in-menu
			 */
			if (pd.hasPermission("pgf.cmd.pvp")) {
				
				if (pd.hasTag("pvp")) {
					setAction(6, (p, e) -> {
						enablePVP(this);
						p.performCommand("pvp");
					});
					
					setItem(6, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
					
				} else {
					
					setAction(6, (p, e) -> {
						disablePVP(this);
						p.performCommand("pvp");
					});
					setItem(6, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
					
					
					
				}
			}
		}
	}
	
	// all nested inventories are below.
	// so ye, classes within classes
	// how about that, huh?
	
	/**
	 * Discord Link/unlink command inventory.
	 * @author CrimsonDart
	 *
	 */
	private class DiscordConfirm extends BaseInventory {
		
		public DiscordConfirm(PlayerData pd) {
			super(27, "§r§8Unlink Account?");
			
			/*
			 * checks if discord is already linked, and creates buttons corresponding to this information.
			 * sets buttons to the already-coded commands, because there is no difference. 
			 */
			
			
			setAction(11, (p, e) -> {
				p.closeInventory(); // I think it is better if the unlink doesn't close the inventory
				p.performCommand("unlink");
				p.openInventory(new Homepage().getInventory());
			});
			setItem(11, Material.LIME_CONCRETE).n("§r§cUnlink");
			
			setAction(15, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(15, Material.RED_CONCRETE).n("§r§7Cancel");
		}
	}
	
	private class BackConfirm extends BaseInventory {
		public BackConfirm(PlayerData pd) {
			super(27, "§r§8Tp to last location?");
			
			setAction(11, (p, e) -> {
				p.closeInventory();
				p.performCommand("back");
			});
			setItem(11, Material.LIME_CONCRETE).n("§r§dTeleport");
			
			setAction(15, (p, e) -> {
				p.closeInventory();
				p.openInventory(new Homepage().getInventory());
			});
			setItem(15, Material.RED_CONCRETE).n("§r§7Cancel");
		}
	}
	
	private class DimSelect extends ListInventory<World> {
		
		public DimSelect(PlayerData pd) {
			super(27, "§r§5Dimension Select");
			
			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
		}

		@Override
		public List<World> load() {
			return DimManager.getAllWorlds(false);
		}

		@Override
		protected Butto toAction(World entry) {
			return (p, e) -> {
				p.performCommand("goto " + entry.getName());
			};
		}
		
		@Override
		protected ItemStack toItem(World entry) {
			return new ItemWrapper(Material.ENDER_PEARL).n("§r§9" + entry.getName()).gi();
		}
	}
	
	private class HomeMenu extends BaseInventory {
		
		public HomeMenu(PlayerData pd) {
			super(27, "§r§8Home");
      
      HashMap<String, Location> homes = Homes.getHomes(pd.getOfflinePlayer());
			
			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
			
			setAction(13, (p, e) -> {
				if (!p.hasPermission("pgf.cmd.home.home")) {
					p.sendMessage("§cYou don't have permission to execute this command.");
					return;
				}
				if (homes.size() == 0) {
					p.sendMessage("§cYou do not have any homes.");
					return;
				}
				p.openInventory(new HomeList("home ").getInventory());
			});
			setItem(13, Material.ENDER_PEARL).n("§r§dGo to Home");
			
			setAction(11, (p, e) -> {
				if (!p.hasPermission("pgf.cmd.home.set")) {
					p.sendMessage("§cYou don't have permission to execute this command.");
					return;
				}
				if (p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 5) {
					p.sendMessage("§cYou can only have up to 5 homes: " + Homes.getNamedHomes(p));
					return;
				} else if (!p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 3)
				{
					p.sendMessage("§cYou can only have up to 3 homes: " + Homes.getNamedHomes(p));
					return;
				}
				p.openInventory(new SetConfirm().getInventory());
			});
			setItem(11, Material.OAK_SAPLING).n("§r§aSet Home");
			
			setAction(15, (p, e) -> {
				if (!p.hasPermission("pgf.cmd.home.del")) {
					p.sendMessage("§cYou don't have permission to execute this command.");
					return;
				}
				if (homes.size() == 0) {
					p.sendMessage("§cYou do not have any homes.");
					return;
				}
				p.openInventory(new DelList(pd).getInventory());
			});
			setItem(15, Material.FLINT_AND_STEEL).n("§r§cDelete Home");
		}
		
		private class HomeList extends ListInventory<String> {
			String dingus;
			
			public HomeList(String dingus) {
				super(27, "§r§8Home Select");
				this.dingus = dingus;

				setAction(0, (p, e) -> {
					p.openInventory(new HomeMenu(pd).getInventory());
				});
				setItem(0, Material.FEATHER).n("§r§7Back");
			}

			@Override
			public List<String> load() {
				
				List<String> list = new ArrayList<>();
				for (String s : Homes.getHomes(pd.getPlayer()).keySet()) {
					list.add(s);
				}
				return list;
			}

			@Override
			protected Butto toAction(String entry) {
				
				return (p, e) -> {
					p.performCommand(dingus + entry);
					p.closeInventory();
				};
			}

			@Override
			protected ItemStack toItem(String entry) {
				return new ItemWrapper(Material.PAPER).n(entry).gi();
			}
		}
		
		/**
		 * /sethome confirm inventory.
		 * 
		 * @author CrimsonDart
		 * {@link net.pgfmc.core.ChatEvents}
		 *
		 */
		private class SetConfirm extends BaseInventory {
			public SetConfirm() {
				super(27, "§r§8Set home here?");
				
				setAction(11, (p, e) -> {
					pd.setData("tempHomeLocation", pd.getPlayer().getLocation());
					p.closeInventory();
					pd.sendMessage("§r§dType into chat to set the name of your Home!");
					pd.sendMessage("§r§dYou can only name the home for 4 minutes.");
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						
						@Override
						public void run()
						{
							if (pd.getData("tempHomeLocation") != null) {
								pd.setData("tempHomeLocation", null);
								pd.sendMessage("§r§cYour home couldnt be set.");
							}
						}
						
					}, 20 * 60 * 4);
				});
				setItem(11, Material.LIME_CONCRETE).n("§r§aSet Home");
				
				setAction(15, (p, e) -> {
					p.openInventory(new HomeMenu(pd).getInventory());
				});
				setItem(15, Material.RED_CONCRETE).n("§r§7Cancel");
			}
		}
		
		
		private class DelList extends ListInventory<String> {
			
			public DelList(PlayerData pd) {
				super(27, "§r§8Delete Home");
				
				setAction(0, (p, e) -> {
					p.openInventory(new HomeMenu(pd).getInventory());
				});
				setItem(0, Material.FEATHER).n("§r§7Back");
			}

			@Override
			public List<String> load() {
				List<String> list = new ArrayList<>();
				for (String s : Homes.getHomes(pd.getPlayer()).keySet()) {
					list.add(s);
				}
				return list;
			}

			@Override
			protected Butto toAction(String entry) {
				
				return (p, e) -> {
					p.performCommand("delhome " + entry);
					p.closeInventory();
				};
			}

			@Override
			protected ItemStack toItem(String entry) {
				return new ItemWrapper(Material.PAPER).n("§r§a" + entry).gi();
			}
		}
	}
	
	private class TpaList extends ListInventory<Player> {
		public TpaList() {
			super(27, "§r§8Select who to teleport to!");
			
			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
		}

		@Override
		public List<Player> load() {
			return Bukkit.getOnlinePlayers().stream()
					.filter(x-> {
						return (!x.getUniqueId().toString().equals(pd.getUniqueId().toString()));
					})
					.collect(Collectors.toList());
		}

		@Override
		protected Butto toAction(Player entry) {
			return (p, e) -> {
				p.performCommand("tpa " + entry.getName());
				p.openInventory(new Homepage().getInventory());
			};
		}

		@Override
		protected ItemStack toItem(Player entry) {
			return new ItemWrapper(Material.PLAYER_HEAD).n("§r§a" + entry.getName()).gi();
		}
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
	private static void enablePVP(BaseInventory ib) {
		
		ib.setAction(6, (p, e) -> {
			disablePVP(ib);
			p.performCommand("pvp");
		});
		ib.setItem(6, Material.WOODEN_SWORD).n("§r§7PVP: §cDisabled").l("§r§7Click to enable!");
		
		
	}
	
	private static void disablePVP(BaseInventory ib) {
		
		ib.setAction(6, (p, e) -> {
			enablePVP(ib);
			p.performCommand("pvp");
		});
		ib.setItem(6, Material.DIAMOND_SWORD).n("§r§7PVP: §aEnabled").l("§r§7Click to disable!");
	}
	
	private static void enableAFK(BaseInventory ib) {
		
		ib.setAction(3, (p, e) -> {
			disableAFK(ib);
			p.performCommand("afk");
		});
		ib.setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
	}
	
	private static void disableAFK(BaseInventory ib) {
		
		ib.setAction(3, (p, e) -> {
			enableAFK(ib);
			p.performCommand("afk");
		});
		ib.setItem(3, Material.ICE).n("§r§7AFK: §cDisabled").l("§r§7Click to enable!");
	}
}