package net.pgfmc.masterbook.masterbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import net.pgfmc.core.CoreMain.PGFPlugin;
import net.pgfmc.core.cmd.Blocked;
import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.ButtonInventory;
import net.pgfmc.core.inventoryAPI.ListInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;
import net.pgfmc.core.util.DimManager;
import net.pgfmc.masterbook.Main;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;
import net.pgfmc.teleport.home.Homes;

public class CommandsMenu implements InventoryHolder {
	
	private PlayerData pd;
	
	public CommandsMenu(PlayerData pd) {
		this.pd = pd;
	}
	
	public class Homepage extends BaseInventory {
		
		public Homepage() {
			super(27, "Commands");
			
			List<String> perms = new ArrayList<>();
			
			for (PermissionAttachmentInfo s : pd.getPlayer().getEffectivePermissions()) {
				if (s.getValue()) {
					perms.add(s.getPermission());
				}
			}
			
			/* 
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (perms.contains("pgf.cmd.link")) {
				
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
			if (perms.contains("pgf.cmd.afk")) {
				
				if (Afk.isAfk(pd.getPlayer())) {
					
					setAction(3, (p, e) -> {
						
						p.performCommand("afk");
					});
					
					setItem(3, Material.BLUE_ICE).n("§r§7AFK: §aEnabled").l("§r§7Click to disable!");
				} else {
					setAction(3, (p, e) -> {
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
			if (perms.contains("pgf.cmd.back")) {
				
				setAction(5, (p, e) -> {
					p.openInventory(new BackConfirm(pd).getInventory());
				});
				
				setItem(5, Material.ARROW).n("§r§4Back").l("§r§7Go back to your last location");
			}
			
			/* 
			 * [] [] [] [] [] [] XX [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * shows the current world's seed on screen.
			 */
			setAction(6, (p, e) -> {
				p.closeInventory();
				p.openBook(Guidebook.getCopmleteBook());
			});
			setItem(6, Material.BOOK).n("§r§dInfo").l("§r§7Bring up the guidebook");
			
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * /dim command
			 */
			if (perms.contains("pgf.cmd.goto")) {
				
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
			if (perms.contains("pgf.cmd.home.*")) {
				setAction(20, (p, e) -> {
					p.openInventory(new HomeMenu(pd).getInventory());
				});
				setItem(20, Material.COMPASS).n("§r§eHomes");
			}
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] XX [] [] [] [] []
			 * home menu
			 */
			if (perms.contains("pgf.cmd.tp.tpa")) {
				
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
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] XX [] [] []
			 * home menu
			 */
			if (perms.contains("teams.friend.*") && PGFPlugin.TEAMS.isEnabled()) {
				
				setAction(23, (p, e) -> {
					p.openInventory(new FriendsList().getInventory());
				});
				setItem(23, Material.TOTEM_OF_UNDYING).n("§r§6Friends");
			}
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] XX [] []
			 * home menu
			 */
			if (perms.contains("bukkit.command.list") && PGFPlugin.TEAMS.isEnabled()) {
				
				setAction(24, (p, e) -> {
					p.openInventory(new PlayerList().getInventory());
				});
				setItem(24, Material.PLAYER_HEAD).n("§r§bPlayer List");
			}
			
			// Other buttons -
			
			/* 
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * home menu
			 */
			
			//List<Role> roles = pd.getData("Roles");
			
			//if (roles != null) {
			//	setButton(4, new Button(Material.EMERALD, PermissionsManager.getRolePrefix(roles.get(0)) + roles.get(0)));
			//};
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * home menu
			 */
			if (perms.contains("pgf.cmd.donator.echest")) {
				
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
				p.openInventory(new RequestList(pd).getInventory());
			});
			setItem(9, Material.LEVER).n("§r§4Requests");
			
			
			/* 
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 * Nickname
			 */
			if (perms.contains("pgf.cmd.donator.nick"))
			{
				setAction(18, (p, e) -> {
					PlayerData.setData(p, "nickTemp", "reset");
					p.closeInventory();
					p.sendMessage("§9Type your new nickname in chat.");
				});
				setItem(18, Material.NAME_TAG).n("§eNickname").l("§7Give yourself a nickname!");
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
			
			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
			
			setAction(13, (p, e) -> {
				p.openInventory(new HomeList("home ").getInventory());
			});
			setItem(13, Material.ENDER_PEARL).n("§r§dGo to Home");
			
			if (!(Homes.getHomes(pd.getOfflinePlayer()).size() >= 3)) {
				
				setAction(11, (p, e) -> {
					p.openInventory(new SetConfirm().getInventory());
				});
				setItem(11, Material.OAK_SAPLING).n("§r§aSet Home");
				
			}
			
			setAction(15, (p, e) -> {
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
					p.openInventory(new Homepage().getInventory());
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
					p.openInventory(new Homepage().getInventory());
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
					p.performCommand("delhome" + entry);
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
	
	public class FriendsList extends ListInventory<PlayerData> {
		
		public FriendsList() {
			super(27, "§r§8Friends List");

			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
		}
		
		@Override
		public List<PlayerData> load() {
			return Friends.getFriendsMap(pd).keySet().stream()
					.collect(Collectors.toList());
		}
		
		@Override
		protected Butto toAction(PlayerData entry) {
			return (p, d) -> {
				p.openInventory(new FriendOptions(pd, entry).getInventory());
			};
		}

		@Override
		protected ItemStack toItem(PlayerData entry) {
			return new ItemWrapper(Material.PLAYER_HEAD).n(entry.getRankedName()).gi();
		}
		
		
		
		public class FriendOptions extends BaseInventory {

			public FriendOptions(PlayerData player, PlayerData friend) {
				super(27, "§r§8Options for " + friend.getRankedName());
				
				
				setAction(12, (p, e) -> {
					Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
					player.sendMessage("§cYou have Unfriended " + friend.getName() + ".");
					player.playSound(Sound.BLOCK_CALCITE_HIT);
					// player.getPlayer().closeInventory(); // Better if not close
					p.openInventory(new FriendOptions(player, friend).getInventory());
					
				});
				setItem(12, Material.ARROW).n("§r§cUnfriend");
				
				Relation r = Friends.getRelation(player, friend);
				
				if (r == Relation.FRIEND) {
					
					setAction(14, (p, e) -> {
						
						Friends.setRelation(player, friend, Relation.FAVORITE);
						player.sendMessage("§r§6" + friend.getName() + " is now a favorite!");
						player.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
						// player.getPlayer().closeInventory(); // Better if not close
						p.openInventory(new FriendOptions(player, friend).getInventory());
						
					});
					setItem(14, Material.NETHER_STAR).n("§r§eFavorite");
					
				} else if (r == Relation.FAVORITE) {
					
					setAction(14, (p, e) -> {
						
						Friends.setRelation(player, friend, Relation.FRIEND);
						player.sendMessage("§r§c" + friend.getName() + " has Been unfavorited!");
						player.playSound(Sound.BLOCK_CALCITE_HIT);
						// player.getPlayer().closeInventory(); // Better if not close
						p.openInventory(new FriendOptions(player, friend).getInventory());
						
					});
					setItem(14, Material.NETHER_STAR).n("§r§6Unfavorite");
				}
			}
		}
	}
	
	public class PlayerList extends ListInventory<PlayerData> {
		
		public PlayerList() {
			super(27, "§r§8Player List");

			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
		}
		
		@Override
		protected Butto toAction(PlayerData entry) {
			return (p, e) -> {
				p.openInventory(new PlayerOptions(entry).getInventory());
			};
		}

		@Override
		protected ItemStack toItem(PlayerData entry) {
			return new ItemWrapper(Material.PLAYER_HEAD).n(entry.getRankedName()).l((entry.isOnline()) ? "§r§aOnline" : "§r§cOffline").gi();
		}
		
		@Override
		public List<PlayerData> load() {
			return PlayerData.stream()
					.filter(x-> {
						return (x != pd);
					})
					.sorted((o1, o2) -> { // player sorter.
						
						if (o1.isOnline() && o2.isOnline()) { // both online
							
							Relation r1 = Friends.getRelation(pd, o1);
							Relation r2 = Friends.getRelation(pd, o2);
							
							if (r1 == r2) { // both are equal.
								return 0;
							} else if (r1 == Relation.NONE && r2 != Relation.NONE) { // r2 friended &^ but not r1.
								return 1;
							} else if (r1 != Relation.NONE && r2 == Relation.NONE) { // r1 friended &^ but not r2.
								return -1;
							} else if (r1 == Relation.FRIEND && r2 == Relation.FAVORITE) {
								return 1;
							} else if (r1 == Relation.FAVORITE && r2 == Relation.FRIEND) {
								return -1;
							} else {
								return 0;
							}
						} else if (o1.isOnline()) { // 1 is online
							return -1;
						} else if (o2.isOnline()) { // 2 is online
							return 1;
						} else { // ------------------ none are online
							Relation r1 = Friends.getRelation(pd, o1);
							Relation r2 = Friends.getRelation(pd, o2);
							
							if (r1 == r2) { // both are equal.
								return 0;
							} else if (r1 == Relation.NONE && r2 != Relation.NONE) { // r2 friended &^ but not r1.
								return 1;
							} else if (r1 != Relation.NONE && r2 == Relation.NONE) { // r1 friended &^ but not r2.
								return -1;
							} else if (r1 == Relation.FRIEND && r2 == Relation.FAVORITE) {
								return 1;
							} else if (r1 == Relation.FAVORITE && r2 == Relation.FRIEND) {
								return -1;
							} else {
								return 0;
							}
						}
					})
					.collect(Collectors.toList());
		}
		
		
		private class PlayerOptions extends BaseInventory {
			
			public PlayerOptions(PlayerData player) {
				super(27, player.getRankedName());
				
				
				setAction(0, (p, e) -> {
					p.openInventory(new PlayerList().getInventory());
				});
				setItem(0, Material.FEATHER).n("§r§7Back");
				
				List<String> perms = new ArrayList<>();
				
				for (PermissionAttachmentInfo s : pd.getPlayer().getEffectivePermissions()) {
					if (s.getValue()) {
						perms.add(s.getPermission());
					}
				}
				
				if (perms.contains("teams.friend.*") && PGFPlugin.TEAMS.isEnabled()) {
					
					Relation r = Friends.getRelation(pd, player);
					if (r == Relation.FRIEND || r == Relation.FAVORITE) {
						setAction(11, (p, e) -> {
							p.openInventory(new UnfriendConfirm(pd, player).getInventory());
						});
						setItem(11, Material.TOTEM_OF_UNDYING).n("§r§cUnfriend");
						
						if (r == Relation.FAVORITE) {
							setAction(12, (p, e) -> {
								p.performCommand("unfav " + player.getName());
								p.openInventory(new PlayerOptions(player).getInventory());
							});
							setItem(12, Material.TOTEM_OF_UNDYING).n("§r§cUnfavorite");
							
						} else {
							setAction(12, (p, e) -> {
								p.performCommand("fav " + player.getName());
								p.openInventory(new PlayerOptions(player).getInventory());
							});
							setItem(12, Material.TOTEM_OF_UNDYING).n("§r§eFavorite");
							
						}
					} else {
						setAction(11, (p, e) -> {
							p.openInventory(new FriendConfirm(pd, player).getInventory());
						});
						setItem(11, Material.TOTEM_OF_UNDYING).n("§r§6Friend");
					}
				}
				
				if (perms.contains("pgf.cmd.block")) {
					if (Blocked.GET_BLOCKED(pd.getOfflinePlayer()).contains(player.getUniqueId())) {
						setAction(14, (p, e) -> {
							p.performCommand("unblock " + player.getName());
							p.openInventory(new PlayerOptions(player).getInventory());
						});
						setItem(14, Material.RED_STAINED_GLASS_PANE).n("§r§4Unblock");
						
					} else {
						setAction(14, (p, e) -> {
							p.performCommand("block " + player.getName());
							p.openInventory(new PlayerOptions(player).getInventory());
						});
						setItem(14, Material.WHITE_STAINED_GLASS_PANE).n("§r§4Block");
						
					}
				}
				// XXX setButton(15, new Button(Material.RED_BANNER, "§r§4Report", "§r§7If someone is bullying or\ngriefing you, use this!" + "\nWIP"));
				
			}
			
			private class FriendConfirm extends BaseInventory {
				
				public FriendConfirm(PlayerData pd, PlayerData player) {
					super(27, "§r§6Friend " + player.getName() + "?");
					
					
					setAction(11, (p, e) -> {
						// p.closeInventory();
						p.performCommand("friendrequest " + player.getName());
						p.openInventory(new PlayerOptions(player).getInventory());
					});
					setItem(11, Material.LIME_CONCRETE).n("§r§aSend Request");
					
					setAction(15, (p, e) -> {
						p.openInventory(new PlayerOptions(player).getInventory());
					});
					setItem(15, Material.RED_CONCRETE).n("§r§7Cancel");
					
				}
			}
			
			private class UnfriendConfirm extends BaseInventory {
				
				public UnfriendConfirm(PlayerData pd, PlayerData player) {
					super(27, "§r§cUnfriend " + player.getName() + "?");
					
					
					setAction(11, (p, e) -> {
						p.performCommand("unfriend " + player.getName());
						p.openInventory(new PlayerOptions(player).getInventory());
					});
					setItem(11, Material.LIME_CONCRETE).n("§r§cUnfriend");
					
					setAction(15, (p, e) -> {
						p.openInventory(new PlayerOptions(player).getInventory());
					});
					setItem(15, Material.RED_CONCRETE).n("§r§7Cancel");
				}
			}
		}
	}
	
	public class RequestList extends ButtonInventory {
		
		PlayerData pd;
		
		public RequestList(PlayerData pd) {
			super(27, "Pending Requests");
			this.pd = pd;

			setAction(0, (p, e) -> {
				p.openInventory(new Homepage().getInventory());
			});
			setItem(0, Material.FEATHER).n("§r§7Back");
		}

		@Override
		public List<Buttonable> load() {
			List<Buttonable> list = new ArrayList<>();
			
			Set<Request> set = RequestType.getInAllRequests(x -> {
				return (x.target == pd);
			});
			
			for (Request r : set) {
				list.add(r);
			}
			return list;
			
		}
	}

	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
}