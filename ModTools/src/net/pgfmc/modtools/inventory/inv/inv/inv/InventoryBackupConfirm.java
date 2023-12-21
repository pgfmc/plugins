package net.pgfmc.modtools.inventory.inv.inv.inv;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.inventory.InventoryBackup;
import net.pgfmc.modtools.inventory.inv.InventoryOnlinePlayersList;

public class InventoryBackupConfirm extends ConfirmInventory {
	
	InventoryBackup inventory;

	public InventoryBackupConfirm(String name, InventoryBackup inventory) {
		super(name, ChatColor.GREEN + "Rollback", ChatColor.RED + "Cancel");
		
		this.inventory = inventory;
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		
		OfflinePlayer target = inventory.getOfflinePlayer();
		
		if (inventory.restore() && target.isOnline())
		{
			p.sendMessage(ChatColor.GREEN + "Inventory restored.");
			target.getPlayer().sendMessage(ChatColor.GREEN + "A moderator has restored your inventory.");
			
			PlayerData.from(p).playSound(Sound.ENTITY_PLAYER_LEVELUP);
			PlayerData.from(target).playSound(Sound.ENTITY_PLAYER_LEVELUP);
			
			return;
		}
		
		p.sendMessage(ChatColor.RED + "Inventory could not be restored.");
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		
		OfflinePlayer target = inventory.getOfflinePlayer();
		
		if (!target.isOnline()) return;
		
		p.openInventory(new InventoryOnlinePlayersList().getInventory());
		
	}

}
