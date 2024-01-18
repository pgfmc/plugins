package net.pgfmc.survival.masterbook.inv.staff.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.masterbook.MasterbookInventory;
import net.pgfmc.survival.masterbook.inv.staff.inv.giverewards.inv.GiveRewardsListInventory;

public class StaffInventory extends BaseInventory {

	public StaffInventory (PlayerData playerdata) {
		super(27, ChatColor.GRAY + "Staff Commands");
		
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
		
		setItem(6, Material.BOOKSHELF).n(ChatColor.RESET + "" + ChatColor.YELLOW + "Give Rewards");
		
		
	}

}
