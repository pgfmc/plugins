package net.pgfmc.survival.menu;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.PGFRole;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.serverselector.ServerSelectorInventory;
import net.pgfmc.core.util.Lang;
import net.pgfmc.survival.Rewards;
import net.pgfmc.survival.menu.profile.ProfileInventory;
import net.pgfmc.survival.menu.rewards.RewardsListInventory;
import net.pgfmc.survival.menu.staff.StaffInventory;
import net.pgfmc.survival.menu.teleports.Teleports;
import net.pgfmc.survival.particleeffects.HaloEffect.HaloParticle;

public class CommandMenuInventory implements InventoryHolder {
	
	private PlayerData playerdata;
	
	public CommandMenuInventory(final PlayerData playerdata)
	{
		this.playerdata = playerdata;
	}
	
	public class Homepage extends BaseInventory {
		
		public Homepage()
		{
			super(InventoryType.CHEST, "Command Menu");

            
			final int afk_location = 3;
			/* 
			 * AFK Toggle
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.afk"))
			{
				if (playerdata.hasTag("afk"))
				{
					setAction(afk_location, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						
					});
					
					setItem(afk_location, Material.BLUE_ICE).n(ChatColor.GRAY + "AFK: " + ChatColor.GREEN + "Enabled")
												.l(ChatColor.GRAY + "Take no damage while enabled.");
					
				} else if (!playerdata.hasTag("afk")) {
					setAction(afk_location, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						
					});
					
					setItem(afk_location, Material.GLASS).n(ChatColor.GRAY + "AFK: " + ChatColor.RED + "Disabled")
											.l(ChatColor.GRAY + "Take no damage while enabled.");
				}
				
			}

			/* 
			 * Home Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 */
			//setAction(20, (player, event) -> {
			//	player.openInventory(new HomeHomepageInventory(playerdata).getInventory());
			//});
			//		
			//setItem(20, Material.RED_BED).n(ChatColor.YELLOW + "Home Menu");
			
			
            final int teleports = 13;
			/* 
			 * Teleport Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */

            // TODO

            setAction(teleports, (player, event) -> {
                player.openInventory(new Teleports(playerdata).getInventory());
            });

            setItem(teleports, Material.ENDER_PEARL).n(ChatColor.DARK_PURPLE + "Teleport Menu")
                .l(ChatColor.GRAY + "Different ways to get around.");
			
            final int profile = 5;
			/* 
			 * Profile
			 * [] [] [] [] [] XX [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			setAction(profile, (player, event) -> {
				player.openInventory(new ProfileInventory(playerdata).getInventory());
			});
			
			final PGFRole role = playerdata.getData("role");
			
			setItem(profile, Skull.getHead(playerdata.getUniqueId()))
					.n(playerdata.getRankedName() + " (" + role.toString() + ")")
					.l(ChatColor.GRAY + "Open Profile.");
			
			/* 
			 * Ender Chest
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] XX [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.echest"))
			{
				setAction(23, (player, event) -> {
					player.performCommand("echest");
				});
				
			} else
			{
				setAction(23, (player, event) -> {
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(ChatColor.RED + "Only donators can use this command.");
				});
				
			}
			
			setItem(23, Material.ENDER_CHEST).n(ChatColor.DARK_AQUA + "Ender Chest")
											.l(ChatColor.BLUE + "Open an Ender Chest. Donator perk!");
			
            final int crafting = 22;
			/* 
			 * Crafting Table
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.craft"))
			{
				setAction(crafting, (player, event) -> {
					player.performCommand("craft");
				});
				
			} else
			{
				setAction(crafting, (player, event) -> {
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(ChatColor.RED + "Only donators can use this command.");
				});
				
			}
			
			setItem(crafting, Material.CRAFTING_TABLE).n(ChatColor.DARK_AQUA + "Crafting Table")
											.l(ChatColor.BLUE + "Open a Crafting Table. Donator perk!");
			
			
			/* 
			 * Requests Menu
			 * [] [] [] [] [] XX [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			
			// Get all incoming requests for this player
		//	final Set<Request> requests = RequestType.getInAllRequests(request -> request.target == playerdata);
		//	
		//	if (requests == null || requests.isEmpty()) // no incoming requests
		//	{
		//		setAction(5, (player, event) -> {
		//			playerdata.sendMessage(ChatColor.RED + "There are no incoming requests.");
		//			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		//			
		//		});
		//		
		//		setItem(5, Material.BOOK).n(ChatColor.DARK_RED + "Requests Menu (0)");
		//		
		//	} else // there are incoming requests
		//	{
		//		// number of incoming requests
		//		final int requestsCount = requests.size();
		//		
		//		setAction(5, (player, event) -> {
		//			final RequestListInventory inv = new RequestListInventory(playerdata);
		//			inv.setBack(0, new CommandMenuInventory(playerdata).getInventory());
		//			
		//			player.openInventory(inv.getInventory());
		//			
		//		});
		//		
		//		setItem(5, Material.WRITABLE_BOOK).n(ChatColor.DARK_RED + "Requests Menu (" + requestsCount + ")");
		//		
		//	}
			
			
		    final int pvp = 4;	
			/* 
			 * PVP Toggle
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.pvp"))
			{
				if (playerdata.hasTag("pvp"))
				{
					setAction(pvp, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					});
					
					setItem(pvp, Material.DIAMOND_SWORD).n(ChatColor.GRAY + "PVP: " + ChatColor.DARK_RED + "Enabled")
														.l(ChatColor.GRAY + "Fight other players while enabled.");
					
				} else {
					setAction(pvp, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						playerdata.playSound(playerdata.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					});
					
					setItem(pvp, Material.WOODEN_SWORD).n(ChatColor.GRAY + "PVP: Disabled")
													.l(ChatColor.GRAY + "Fight other players while enabled.");
					
				}
				
			}
			
            final int rewards = 16;
			/* 
			 * Rewards
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] XX [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			final int numberOfRewards = Rewards.getPlayerRewardsMap(playerdata).size();
			
			if (numberOfRewards == 0)
			{
				setAction(rewards, (player, event) -> {
					playerdata.sendMessage(ChatColor.RED + "There are no rewards currently.");
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				});
				
				setItem(rewards, Material.CHISELED_BOOKSHELF).n(ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			} else
			{
				setAction(rewards, (player, event) -> {
					player.openInventory(new RewardsListInventory(playerdata).getInventory());
				});
				
				setItem(rewards, Material.BOOKSHELF).n(ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			}
			
			
            final int staff = 9;
			/* 
			 * Staff Commands
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (role.compareTo(PGFRole.STAFF) <= 0)
			{
				setAction(staff, (player, event) -> {
					player.openInventory(new StaffInventory(playerdata).getInventory());
					
				});
				
				setItem(staff, Material.SPYGLASS).n(ChatColor.LIGHT_PURPLE + "Staff Commands");
				
			}
			
			
			
			
            final int particles = 21;
			/* 
			 * Particle Effects
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] XX [] [] [] [] []
			 */
			if (role.compareTo(PGFRole.VETERAN) <= 0 || playerdata.hasPermission("net.pgfmc.survival.particleeffects")) // has permission
			{
				setAction(particles, (player, event) -> {
					final String particle_effect = playerdata.getData("particle_effect");
					
					if (particle_effect == null)
					{
						playerdata.setData("particle_effect", HaloParticle.CHERRY.toString()).queue();
					} else if (particle_effect.equals(HaloParticle.CHERRY.toString()))
					{
						playerdata.setData("particle_effect", HaloParticle.HEART.toString()).queue();
					} else if (particle_effect.equals(HaloParticle.HEART.toString()))
					{
						playerdata.setData("particle_effect", HaloParticle.NOTE.toString()).queue();
					} else if (particle_effect.equals(HaloParticle.NOTE.toString()))
					{
						playerdata.setData("particle_effect", null).queue();
					} else
					{
						playerdata.setData("particle_effect", null).queue();
					}
					
					playerdata.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 1.5F);
					player.openInventory(new CommandMenuInventory(playerdata).getInventory()); // refresh
					
				});
				
				final HaloParticle particle = HaloParticle.fromString(playerdata.getData("particle_effect"));
				
				if (particle == null)
				{
					setItem(particles, Material.GLASS_BOTTLE).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "Off"
							, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				} else
				{
					setItem(particles, Material.DRAGON_BREATH).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "" + particle.name()
							, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				}
				
			} else // No permission
			{
				setAction(particles, (player, event) -> {
					final String particle_effect = playerdata.getData("particle_effect");
					
					// Reset to null if they don't have permission to use the particle effects
					if (particle_effect != null)
					{
						playerdata.setData("particle_effect", null).queue();
					}
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(Lang.PERMISSION_DENIED.getLang());
					
				});
				
				setItem(particles, Material.GLASS_BOTTLE).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "Off"
						, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				
			}
			

            final int servers = 10;
			/* 
			 * Connect / Server Selector
			 * [] [] [] [] [] [] [] [] []
			 * [] XX [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect"))
			{
				setItem(servers, Material.COMPASS)
					.n(ChatColor.GOLD + "Server Selector")
					.l(ChatColor.GRAY + "Connect to another server on the network.");
				
				setAction(servers, (player, event) -> {
					final BaseInventory serverSelector = new ServerSelectorInventory(playerdata);
					serverSelector.setBack(0, new CommandMenuInventory(playerdata).getInventory());
					
					player.openInventory(serverSelector.getInventory());
				});
				
			}
			
		}
		
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
}
