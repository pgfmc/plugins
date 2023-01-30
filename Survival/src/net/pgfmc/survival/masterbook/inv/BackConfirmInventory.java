package net.pgfmc.survival.masterbook.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;

public class BackConfirmInventory extends ConfirmInventory {
	
	private PlayerData pd;
	
	protected BackConfirmInventory(PlayerData pd)
	{
		super(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Go back?", ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Teleport", ChatColor.RESET + "" + ChatColor.GRAY + "Back");
		
		this.pd = pd;
	}
	
	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		p.performCommand("back");
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		p.openInventory(new MasterbookInventory(pd).getInventory());
		
	}

}
