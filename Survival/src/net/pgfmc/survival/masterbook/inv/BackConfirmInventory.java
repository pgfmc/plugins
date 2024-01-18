package net.pgfmc.survival.masterbook.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.masterbook.MasterbookInventory;

public class BackConfirmInventory extends ConfirmInventory {
	
	private PlayerData pd;
	
	public BackConfirmInventory(PlayerData pd)
	{
		super(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Teleport Back", ChatColor.RESET + "" + ChatColor.DARK_GREEN + "Continue", ChatColor.RESET + "" + ChatColor.GRAY + "Cancel");
		
		this.pd = pd;
	}
	
	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		p.performCommand("back");
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.openInventory(new MasterbookInventory(pd).getInventory());
		
	}

}
