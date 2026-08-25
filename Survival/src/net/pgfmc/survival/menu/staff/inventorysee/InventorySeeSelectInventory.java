package net.pgfmc.survival.menu.staff.inventorysee;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.cmd.Skull;
import net.pgfmc.survival.menu.staff.manageplayers.ManagePlayerInventory;

public class InventorySeeSelectInventory extends BaseInventory {

	public InventorySeeSelectInventory(final PlayerData playerdata, final PlayerData target) {
		super(InventoryType.CHEST, Component.text("Inventory See"));
		
		setBack(0, new ManagePlayerInventory(playerdata, target).getInventory());
		
		setItem(13, Skull.getHead(target.getUniqueId()))
            .name(target.getRankedName())
            .lore(Component.text("Select an inventory.", NamedTextColor.GRAY));
		
		setAction(12, (player, event) -> {
			player.performCommand("invsee " + target.getName());
		});
		
		setItem(12, Material.CHEST)
            .name(Component.text("Open Inventory", NamedTextColor.BLUE));
		
		
		setAction(14, (player, event) -> {
			player.performCommand("invsee " + target.getName() + " echest");
		});
		
		setItem(14, Material.ENDER_CHEST)
            .name(Component.text("Open Ender Chest", NamedTextColor.BLUE));
	}
}
