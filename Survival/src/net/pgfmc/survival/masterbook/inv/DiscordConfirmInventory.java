package net.pgfmc.survival.masterbook.inv;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;

public class DiscordConfirmInventory extends ConfirmInventory {
	
	private PlayerData pd;

	protected DiscordConfirmInventory(PlayerData pd) {
		super(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Unlink account?", ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Unlink", ChatColor.RESET + "" + ChatColor.GRAY + "Back");
		
		this.pd = pd;
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		p.performCommand("unlink");
		p.openInventory(new MasterbookInventory(pd).getInventory());
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.openInventory(new MasterbookInventory(pd).getInventory());
		
	}

}
