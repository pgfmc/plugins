package net.pgfmc.survival.masterbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.core.util.roles.RoleManager;
import net.pgfmc.survival.masterbook.inv.BackConfirmInventory;
import net.pgfmc.survival.masterbook.inv.RewardsListInventory;
import net.pgfmc.survival.masterbook.inv.TpaListInventory;
import net.pgfmc.survival.masterbook.inv.home.inv.HomeHomepage;
import net.pgfmc.survival.masterbook.inv.profile.inv.ProfileInventory;

public class MasterbookInventory implements InventoryHolder {
	
	private PlayerData pd;
	
	public MasterbookInventory(PlayerData pd)
	{
		this.pd = pd;
	}
	
	public class Homepage extends BaseInventory {
		
		@SuppressWarnings("unchecked")
		public Homepage()
		{
			super(27, "Command Menu");
			
			/* 
			 * AFK Toggle
			 * [] [] XX [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (pd.hasPermission("pgf.cmd.afk"))
			{
				if (pd.hasTag("afk"))
				{
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new MasterbookInventory(pd).getInventory());
						
					});
					
					setItem(2, Material.BLUE_ICE).n(ChatColor.RESET + "" + ChatColor.GRAY + "AFK: " + ChatColor.GREEN + "Enabled")
												.l(ChatColor.RESET + "" + ChatColor.GRAY + "Take no damage while enabled.");
					
				} else if (!pd.hasTag("afk")) {
					setAction(2, (player, event) -> {
						player.performCommand("afk");
						player.openInventory(new MasterbookInventory(pd).getInventory());
						
					});
					
					setItem(2, Material.ICE).n(ChatColor.RESET + "" + ChatColor.GRAY + "AFK: " + ChatColor.RED + "Disabled")
											.l(ChatColor.RESET + "" + ChatColor.GRAY + "Take no damage while enabled.");
					
				}
				
			}
			
			/* 
			 * Back
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] XX [] [] [] []
			 */
			if (pd.getData("backLoc") == null)
			{
				setAction(22, (player, event) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					pd.sendMessage(ChatColor.RED + "No back location available.");
				});
				
				setItem(22, Material.MINER_POTTERY_SHERD).n(ChatColor.RESET + "" + ChatColor.DARK_GREEN + "Teleport Back")
														.l(ChatColor.RESET + "" + ChatColor.GRAY + "Go back to where you teleported from.");;
			
			} else {
				setAction(22, (player, event) -> {
					player.openInventory(new BackConfirmInventory(pd).getInventory());
				});
				
				setItem(22, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE).n(ChatColor.RESET + "" + ChatColor.DARK_GREEN + "Teleport Back")
																		.l(ChatColor.RESET + "" + ChatColor.GRAY + "Go back to where you teleported from.");
				
			}
			
			
			/* 
			 * Home Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] XX [] [] [] [] [] []
			 */
			setAction(20, (player, event) -> {
				player.openInventory(new HomeHomepage(pd).getInventory());
			});
					
			setItem(20, Material.COMPASS).n(ChatColor.RESET + "" + ChatColor.YELLOW + "Home Menu");
			
			
			/* 
			 * Teleport Menu
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] XX [] [] [] [] []
			 */
			if (Bukkit.getOnlinePlayers().size() > 1)
			{
				setAction(21, (player, event) -> {
					player.openInventory(new TpaListInventory(pd).getInventory());
				});
			
				setItem(21, Material.ENDER_PEARL).n(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Teleport Menu")
												.l(ChatColor.RESET + "" + ChatColor.GRAY + "Teleport to another player.");
				
			} else {
				setAction(21, (player, event) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				});
				
				setItem(21, Material.ENDER_EYE).n(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Teleport Menu")
												.l(ChatColor.RESET + "" + ChatColor.RED + "No players online.");
				
			}
			
			/* 
			 * Profile
			 * [] [] [] [] XX [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			setAction(4, (player, event) -> {
				player.openInventory(new ProfileInventory(pd).getInventory());
				
			});
			
			final PGFRole role = RoleManager.getPlayerTopRole(pd);
			
			setItem(4, Skull.getHead(pd.getUniqueId(), null))
					.n(pd.getRankedName() + " (" + role.getName().substring(0,1).toUpperCase() + role.getName().substring(1).toLowerCase() + ")")
					.l(ChatColor.RESET + "" + ChatColor.GRAY + "Open Profile.");
			
			/* 
			 * Ender Chest
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] XX [] [] []
			 */
			if (pd.hasPermission("pgf.cmd.donator.echest"))
			{
				setAction(23, (player, event) -> {
					player.performCommand("echest");
				});
				
			} else
			{
				setAction(23, (player, event) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					pd.sendMessage(ChatColor.RED + "Only donators can use this command.");
				});
				
			}
			
			setItem(23, Material.ENDER_CHEST).n(ChatColor.RESET + "" + ChatColor.DARK_AQUA + "Ender Chest")
											.l(ChatColor.RESET + "" + ChatColor.BLUE + "Open an Ender Chest. Donator perk!");
			
			/* 
			 * Crafting Table
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] XX [] []
			 */
			if (pd.hasPermission("pgf.cmd.donator.craft"))
			{
				setAction(24, (player, event) -> {
					player.performCommand("craft");
				});
				
			} else
			{
				setAction(24, (player, event) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					pd.sendMessage(ChatColor.RED + "Only donators can use this command.");
				});
				
			}
			
			setItem(24, Material.CRAFTING_TABLE).n(ChatColor.RESET + "" + ChatColor.DARK_AQUA + "Crafting Table")
											.l(ChatColor.RESET + "" + ChatColor.BLUE + "Open a Crafting Table. Donator perk!");
			
			
			/* 
			 * Requests Menu
			 * [] [] [] [] [] XX [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			setAction(5, (player, event) -> {
				RequestListInventory inv = new RequestListInventory(pd);
				inv.setBack(0, new MasterbookInventory(pd).getInventory());
				
				player.openInventory(inv.getInventory());
				
			});
			
			setItem(5, Material.WRITABLE_BOOK).n(ChatColor.RESET + "" + ChatColor.DARK_RED + "Requests Menu");
			
			/* 
			 * PVP Toggle
			 * [] [] [] XX [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			if (pd.hasPermission("pgf.cmd.pvp"))
			{
				if (pd.hasTag("pvp"))
				{
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new MasterbookInventory(pd).getInventory());
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					});
					
					setItem(3, Material.DIAMOND_SWORD).n(ChatColor.RESET + "" + ChatColor.GRAY + "PVP: " + ChatColor.DARK_RED + "Enabled")
														.l(ChatColor.RESET + "" + ChatColor.GRAY + "Fight other players while enabled.");
					
				} else {
					setAction(3, (player, event) -> {
						player.performCommand("pvp");
						player.openInventory(new MasterbookInventory(pd).getInventory());
						pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					});
					
					setItem(3, Material.WOODEN_SWORD).n(ChatColor.RESET + "" + ChatColor.GRAY + "PVP: Disabled")
													.l(ChatColor.RESET + "" + ChatColor.GRAY + "Fight other players while enabled.");
					
				}
				
			}
			
			/* 
			 * Rewards
			 * [] [] [] [] [] [] XX [] []
			 * [] [] [] [] [] [] [] [] []
			 * [] [] [] [] [] [] [] [] []
			 */
			final List<ItemStack> rewards = (List<ItemStack>) Optional.ofNullable(pd.getData("rewards")).orElse(new ArrayList<ItemStack>());
			final int numberOfRewards = rewards.size();
			
			if (numberOfRewards == 0)
			{
				setAction(6, (player, event) -> {
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				});
				
				setItem(6, Material.CHISELED_BOOKSHELF).n(ChatColor.RESET + "" + ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			} else
			{
				setAction(6, (player, event) -> {
					player.openInventory(new RewardsListInventory(pd).getInventory());
				});
				
				setItem(6, Material.BOOKSHELF).n(ChatColor.RESET + "" + ChatColor.YELLOW + "Rewards (" + numberOfRewards + ")");
				
			}
			
		}
		
	}
	
	@Override
	public Inventory getInventory() {
		return new Homepage().getInventory();
	}
	
}
