package net.pgfmc.survival.menu.staff.inventorysee;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.survival.menu.staff.manageplayers.ManagePlayerInventory;

public class InventorySeeSelectInventory extends BaseInventory {

	public InventorySeeSelectInventory(final PlayerData playerdata, final PlayerData target) {
		super(InventoryType.CHEST, "Inventory See");
		
		setBack(0, new ManagePlayerInventory(playerdata, target).getInventory());
		
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
