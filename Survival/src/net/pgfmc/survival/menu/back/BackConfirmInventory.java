package net.pgfmc.survival.menu.back;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.menu.teleports.Teleports;

public class BackConfirmInventory extends ConfirmInventory {
	
	private PlayerData pd;
	
	public BackConfirmInventory(PlayerData pd)
	{
		super(Component.text("Teleport Back"),
                Component.text("Continue", NamedTextColor.DARK_GREEN),
                Component.text("Cancel", NamedTextColor.GRAY));
		
		this.pd = pd;
	}
	
	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		p.performCommand("back");
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.openInventory(new Teleports(pd).getInventory());
		
	}

}
