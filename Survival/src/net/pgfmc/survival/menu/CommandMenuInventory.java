package net.pgfmc.survival.menu;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.Lang;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.core.util.roles.RoleManager;
import net.pgfmc.proxycore.serverselector.ServerSelectorInventory;
import net.pgfmc.survival.Rewards;
import net.pgfmc.survival.menu.back.BackConfirmInventory;
import net.pgfmc.survival.menu.home.HomeHomepageInventory;
import net.pgfmc.survival.menu.profile.ProfileInventory;
import net.pgfmc.survival.menu.rewards.RewardsListInventory;
import net.pgfmc.survival.menu.staff.StaffInventory;
import net.pgfmc.survival.menu.tpa.TpaListInventory;
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
			
			/* 
			 * AFK Toggle
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.afk"))
			{
				if (playerdata.hasTag("afk"))
				{
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						
					});
					
					setItem(2, Material.BLUE_ICE).n(ChatColor.GRAY + "AFK: " + ChatColor.GREEN + "Enabled")
												.l(ChatColor.GRAY + "Take no damage while enabled.");
					
				} else if (!playerdata.hasTag("afk")) {
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						
					});
					
					setItem(2, Material.GLASS).n(ChatColor.GRAY + "AFK: " + ChatColor.RED + "Disabled")
											.l(ChatColor.GRAY + "Take no damage while enabled.");
					
				}
				
			}
			
			/* 
			 * Back
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 */
			if (playerdata.getData("backLoc") == null)
			{
				setAction(22, (player, event) -> {
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(ChatColor.RED + "No back location available.");
				});
			
			} else {
				setAction(22, (player, event) -> {
					player.openInventory(new BackConfirmInventory(playerdata).getInventory());
				});
				
			}
			
			setItem(22, Material.SPECTRAL_ARROW).n(ChatColor.DARK_GREEN + "Teleport Back")
			.l(ChatColor.GRAY + "Go back to where you teleported from.");
			
			
			/* 
			 * Home Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 */
			setAction(20, (player, event) -> {
				player.openInventory(new HomeHomepageInventory(playerdata).getInventory());
			});
					
			setItem(20, Material.RED_BED).n(ChatColor.YELLOW + "Home Menu");
			
			
			/* 
			 * Teleport Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] XX [] [] [] [] []
			 */
			if (Bukkit.getOnlinePlayers().size() > 1)
			{
				setAction(21, (player, event) -> {
					player.openInventory(new TpaListInventory(playerdata).getInventory());
				});
			
				setItem(21, Material.ENDER_PEARL).n(ChatColor.DARK_PURPLE + "Teleport Menu")
												.l(ChatColor.GRAY + "Teleport to another player.");
				
			} else {
				setAction(21, (player, event) -> {
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				});
				
				setItem(21, Material.ENDER_EYE).n(ChatColor.DARK_PURPLE + "Teleport Menu")
												.l(ChatColor.RED + "No players online.");
				
			}
			
			/* 
			 * Profile
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			setAction(13, (player, event) -> {
				player.openInventory(new ProfileInventory(playerdata).getInventory());
				
			});
			
			final PGFRole role = RoleManager.getPlayerTopRole(playerdata);
			
			setItem(13, Skull.getHead(playerdata.getUniqueId()))
					.n(playerdata.getRankedName() + " (" + role.getName().substring(0,1).toUpperCase() + role.getName().substring(1).toLowerCase() + ")")
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
			
			/* 
			 * Crafting Table
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] XX [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.craft"))
			{
				setAction(24, (player, event) -> {
					player.performCommand("craft");
				});
				
			} else
			{
				setAction(24, (player, event) -> {
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(ChatColor.RED + "Only donators can use this command.");
				});
				
			}
			
			setItem(24, Material.CRAFTING_TABLE).n(ChatColor.DARK_AQUA + "Crafting Table")
											.l(ChatColor.BLUE + "Open a Crafting Table. Donator perk!");
			
			
			/* 
			 * Requests Menu
			 * [] [] [] [] [] XX [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			
			// Get all incoming requests for this player
			final Set<Request> requests = RequestType.getInAllRequests(request -> request.target == playerdata);
			
			if (requests == null || requests.isEmpty()) // no incoming requests
			{
				setAction(5, (player, event) -> {
					playerdata.sendMessage(ChatColor.RED + "There are no incoming requests.");
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					
				});
				
				setItem(5, Material.BOOK).n(ChatColor.DARK_RED + "Requests Menu (0)");
				
			} else // there are incoming requests
			{
				// number of incoming requests
				final int requestsCount = requests.size();
				
				setAction(5, (player, event) -> {
					final RequestListInventory inv = new RequestListInventory(playerdata);
					inv.setBack(0, new CommandMenuInventory(playerdata).getInventory());
					
					player.openInventory(inv.getInventory());
					
				});
				
				setItem(5, Material.WRITABLE_BOOK).n(ChatColor.DARK_RED + "Requests Menu (" + requestsCount + ")");
				
			}
			
			
			
			/* 
			 * PVP Toggle
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.survival.pvp"))
			{
				if (playerdata.hasTag("pvp"))
				{
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					});
					
					setItem(3, Material.DIAMOND_SWORD).n(ChatColor.GRAY + "PVP: " + ChatColor.DARK_RED + "Enabled")
														.l(ChatColor.GRAY + "Fight other players while enabled.");
					
				} else {
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new CommandMenuInventory(playerdata).getInventory());
						playerdata.playSound(playerdata.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					});
					
					setItem(3, Material.WOODEN_SWORD).n(ChatColor.GRAY + "PVP: Disabled")
													.l(ChatColor.GRAY + "Fight other players while enabled.");
					
				}
				
			}
			
			/* 
			 * Rewards
			 * [] [] [] [] [] [] XX [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			final int numberOfRewards = Rewards.getPlayerRewardsMap(playerdata).size();
			
			if (numberOfRewards == 0)
			{
				setAction(6, (player, event) -> {
					playerdata.sendMessage(ChatColor.RED + "There are no rewards currently.");
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				});
				
				setItem(6, Material.CHISELED_BOOKSHELF).n(ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			} else
			{
				setAction(6, (player, event) -> {
					player.openInventory(new RewardsListInventory(playerdata).getInventory());
				});
				
				setItem(6, Material.BOOKSHELF).n(ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			}
			
			
			/* 
			 * Staff Commands
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 */
			if (role.compareTo(PGFRole.STAFF) <= 0)
			{
				setAction(18, (player, event) -> {
					player.openInventory(new StaffInventory(playerdata).getInventory());
					
				});
				
				setItem(18, Material.SPYGLASS).n(ChatColor.LIGHT_PURPLE + "Staff Commands");
				
			}
			
			
			
			
			/* 
			 * Particle Effects
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (role.compareTo(PGFRole.VETERAN) <= 0 || playerdata.hasPermission("net.pgfmc.survival.particleeffects")) // has permission
			{
				setAction(4, (player, event) -> {
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
					setItem(4, Material.GLASS_BOTTLE).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "Off"
							, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				} else
				{
					setItem(4, Material.DRAGON_BREATH).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "" + particle.name()
							, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				}
				
			} else // No permission
			{
				setAction(4, (player, event) -> {
					final String particle_effect = playerdata.getData("particle_effect");
					
					// Reset to null if they don't have permission to use the particle effects
					if (particle_effect != null)
					{
						playerdata.setData("particle_effect", null).queue();
					}
					
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					playerdata.sendMessage(Lang.PERMISSION_DENIED.getLang());
					
				});
				
				setItem(4, Material.GLASS_BOTTLE).n(ChatColor.DARK_RED + "Particle Effects").l(Arrays.asList(ChatColor.RED + "Off"
						, ChatColor.GRAY + "Click to cycle particle effects. Donator perk!"));
				
			}
			
			/* 
			 * Soft Depends on Proxycore
			 * 
			 * Connect / Server Selector
			 * [] [] [] [] [] [] [] [] []
			 * XX [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("net.pgfmc.proxycore.connect"))
			{
				setItem(9, Material.COMPASS)
					.n(ChatColor.GOLD + "Server Selector")
					.l(ChatColor.GRAY + "Connect to another server on the network.");
				
				final Plugin proxycorePlugin = Bukkit.getServer().getPluginManager().getPlugin("PGF-Proxycore");
				
				if (proxycorePlugin != null && proxycorePlugin.isEnabled()) // Proxycore is loaded and working
				{
					setAction(9, (player, event) -> {
						final BaseInventory serverSelector = new ServerSelectorInventory(playerdata);
						serverSelector.setBack(0, new CommandMenuInventory(playerdata).getInventory());
						
						player.openInventory(serverSelector.getInventory());
					});
					
				} else // Proxycore is not loaded
				{
					setAction(9, (player, event) -> {
						player.sendMessage(ChatColor.RED + "The server selector is not available right now.");
						playerdata.playSound(Sound.ENTITY_VILLAGER_NO);
					});
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
}
