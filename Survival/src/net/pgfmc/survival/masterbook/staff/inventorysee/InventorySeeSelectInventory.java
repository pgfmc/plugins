package net.pgfmc.survival.masterbook.staff.inventorysee;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.admin.Skull;

public class InventorySeeSelectInventory extends BaseInventory {

	protected InventorySeeSelectInventory(final PlayerData playerdata, final PlayerData target) {
		super(27, "Inventory See");
		
		setBack(0, new InventorySeeListInventory(playerdata).getInventory());
		
		setItem(13, Skull.getHead(target.getUniqueId())).n(target.getRankedName()).l(ChatColor.GRAY + "Select an inventory.");
		
		setAction(12, (player, event) -> {
			player.performCommand("invsee " + target.getName());
		});
		
		setItem(12, Material.CHEST).n(ChatColor.BLUE + "Open Inventory");
		
		
		setAction(14, (player, event) -> {
			player.performCommand("invsee " + target.getName() + " echest");
		});
		
		setItem(14, Material.ENDER_CHEST).n(ChatColor.BLUE + "Open Ender Chest");
		
	}

}
