package net.pgfmc.modtools.rollback.inv;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;

public class RollbackConfirmInventory extends ConfirmInventory {
	
	RollbackInventory inventory;

	protected RollbackConfirmInventory(String name, RollbackInventory inventory) {
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
		
		p.openInventory(new RollbackListInventory(PlayerData.from(target)).getInventory());
		
	}

}
