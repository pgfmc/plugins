package net.pgfmc.survival.masterbook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.core.util.roles.RoleManager;
import net.pgfmc.survival.Rewards;
import net.pgfmc.survival.masterbook.back.BackConfirmInventory;
import net.pgfmc.survival.masterbook.home.HomeHomepageInventory;
import net.pgfmc.survival.masterbook.profile.ProfileInventory;
import net.pgfmc.survival.masterbook.rewards.RewardsListInventory;
import net.pgfmc.survival.masterbook.staff.StaffInventory;
import net.pgfmc.survival.masterbook.tpa.TpaListInventory;

public class MasterbookInventory implements InventoryHolder {
	
	private PlayerData playerdata;
	
	public MasterbookInventory(final PlayerData playerdata)
	{
		this.playerdata = playerdata;
	}
	
	public class Homepage extends BaseInventory {
		
		public Homepage()
		{
			super(27, "Command Menu");
			
			/* 
			 * AFK Toggle
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("pgf.cmd.afk"))
			{
				if (playerdata.hasTag("afk"))
				{
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new MasterbookInventory(playerdata).getInventory());
						
					});
					
					setItem(2, Material.BLUE_ICE).n(ChatColor.GRAY + "AFK: " + ChatColor.GREEN + "Enabled")
												.l(ChatColor.GRAY + "Take no damage while enabled.");
					
				} else if (!playerdata.hasTag("afk")) {
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new MasterbookInventory(playerdata).getInventory());
						
					});
					
					setItem(2, Material.ICE).n(ChatColor.GRAY + "AFK: " + ChatColor.RED + "Disabled")
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
				
				setItem(22, Material.MINER_POTTERY_SHERD).n(ChatColor.DARK_GREEN + "Teleport Back")
														.l(ChatColor.GRAY + "Go back to where you teleported from.");;
			
			} else {
				setAction(22, (player, event) -> {
					player.openInventory(new BackConfirmInventory(playerdata).getInventory());
				});
				
				setItem(22, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE).n(ChatColor.DARK_GREEN + "Teleport Back")
																		.l(ChatColor.GRAY + "Go back to where you teleported from.");
				
			}
			
			
			/* 
			 * Home Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 */
			setAction(20, (player, event) -> {
				player.openInventory(new HomeHomepageInventory(playerdata).getInventory());
			});
					
			setItem(20, Material.COMPASS).n(ChatColor.YELLOW + "Home Menu");
			
			
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
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			setAction(4, (player, event) -> {
				player.openInventory(new ProfileInventory(playerdata).getInventory());
				
			});
			
			final PGFRole role = RoleManager.getPlayerTopRole(playerdata);
			
			setItem(4, Skull.getHead(playerdata.getUniqueId()))
					.n(playerdata.getRankedName() + " (" + role.getName().substring(0,1).toUpperCase() + role.getName().substring(1).toLowerCase() + ")")
					.l(ChatColor.GRAY + "Open Profile.");
			
			/* 
			 * Ender Chest
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] XX [] [] []
			 */
			if (playerdata.hasPermission("pgf.cmd.donator.echest"))
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
			if (playerdata.hasPermission("pgf.cmd.donator.craft"))
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
			setAction(5, (player, event) -> {
				RequestListInventory inv = new RequestListInventory(playerdata);
				inv.setBack(0, new MasterbookInventory(playerdata).getInventory());
				
				player.openInventory(inv.getInventory());
				
			});
			
			setItem(5, Material.WRITABLE_BOOK).n(ChatColor.DARK_RED + "Requests Menu");
			
			/* 
			 * PVP Toggle
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (playerdata.hasPermission("pgf.cmd.pvp"))
			{
				if (playerdata.hasTag("pvp"))
				{
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new MasterbookInventory(playerdata).getInventory());
						playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					});
					
					setItem(3, Material.DIAMOND_SWORD).n(ChatColor.GRAY + "PVP: " + ChatColor.DARK_RED + "Enabled")
														.l(ChatColor.GRAY + "Fight other players while enabled.");
					
				} else {
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new MasterbookInventory(playerdata).getInventory());
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
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (role.compareTo(PGFRole.STAFF) <= 0)
			{
				setAction(13, (player, event) -> {
					player.openInventory(new StaffInventory(playerdata).getInventory());
					
				});
				
				setItem(13, Material.SPYGLASS).n(ChatColor.LIGHT_PURPLE + "Staff Commands");
				
			}
			
			
		}
		
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
}
