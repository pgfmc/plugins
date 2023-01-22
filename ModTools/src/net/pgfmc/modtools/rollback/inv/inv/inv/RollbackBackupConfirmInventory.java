package net.pgfmc.modtools.rollback.inv.inv.inv;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.rollback.RollbackBackup;
import net.pgfmc.modtools.rollback.inv.RollbackOnlinePlayersListInventory;

public class RollbackBackupConfirmInventory extends ConfirmInventory {
	
	RollbackBackup inventory;

	public RollbackBackupConfirmInventory(String name, RollbackBackup inventory) {
		super(name, ChatColor.GREEN + "Rollback", ChatColor.RED + "Cancel");
		
		this.inventory = inventory;
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		
		Player target = inventory.getPlayer();
		
		if (inventory.restore() && target != null)
		{
			p.sendMessage(ChatColor.GREEN + "Inventory restored.");
			target.sendMessage(ChatColor.GREEN + "A moderator has restored your inventory.");
			
			PlayerData.from(p).playSound(Sound.ENTITY_PLAYER_LEVELUP);
			PlayerData.from(target).playSound(Sound.ENTITY_PLAYER_LEVELUP);
			
			return;
		}
		
		p.sendMessage(ChatColor.RED + "Inventory could not be restored.");
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.closeInventory();
		
		Player target = inventory.getPlayer();
		
		if (target == null) return;
		
		p.openInventory(new RollbackOnlinePlayersListInventory().getInventory());
		
	}

}
