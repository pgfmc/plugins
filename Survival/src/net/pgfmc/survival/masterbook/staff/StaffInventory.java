package net.pgfmc.survival.masterbook.staff;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.masterbook.MasterbookInventory;
import net.pgfmc.survival.masterbook.staff.giverewards.GiveRewardsListInventory;
import net.pgfmc.survival.masterbook.staff.inventorybackups.InventoryBackupsListInventory;
import net.pgfmc.survival.masterbook.staff.inventorysee.InventorySeeListInventory;

public class StaffInventory extends BaseInventory {

	public StaffInventory (final PlayerData playerdata) {
		super(27, "Staff Commands");
		
		setBack(0, new MasterbookInventory(playerdata).getInventory());
		
		/* 
		 * Give Rewards
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
			
		setAction(6, (player, event) -> {
			player.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
		});
		
		setItem(6, Material.BOOKSHELF).n(ChatColor.YELLOW + "Give Rewards");
		
		
		/* 
		 * Inventory See
		 * [] [] [] [] [] XX [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(5, (player, event) -> {
			player.openInventory(new InventorySeeListInventory(playerdata).getInventory());
		});
		
		setItem(5, Material.PLAYER_HEAD).n(ChatColor.YELLOW + "Inventory See");
		
		
		/* 
		 * Inventory Backups
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(4, (player, event) -> {
			player.openInventory(new InventoryBackupsListInventory(playerdata).getInventory());
		});
		
		setItem(4, Material.CHEST_MINECART).n(ChatColor.YELLOW + "Inventory Backups");
		
	}

}
