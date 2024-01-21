package net.pgfmc.survival.masterbook.staff.inventorybackups;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.masterbook.staff.inventorybackups.noninv.InventoryBackup;
import net.pgfmc.survival.masterbook.staff.inventorybackups.noninv.InventoryBackupScheduler;

public class InventoryBackupConfirmInventory extends ConfirmInventory {
	
	private PlayerData playerdata;
	private PlayerData target;
	private InventoryBackup inventory;

	public InventoryBackupConfirmInventory(PlayerData playerdata, PlayerData target, InventoryBackup inventory) {
		super(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(inventory.getDate()) + " - " + inventory.getCause().name(), ChatColor.GREEN + "Rollback", ChatColor.RED + "Cancel");
		
		this.playerdata = playerdata;
		this.target = target;
		this.inventory = inventory;
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		
		if (target.isOnline() && inventory.restore())
		{
			p.sendMessage(ChatColor.GREEN + "Inventory restored.");
			target.getPlayer().sendMessage(ChatColor.GREEN + "A moderator has restored your inventory.");
			
			PlayerData.from(p).playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			target.playSound(target.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			
			p.openInventory(new InventoryBackupListInventory(playerdata, target).getInventory());
			
			return;
		}
		
		p.sendMessage(ChatColor.RED + "Inventory could not be restored.");
		playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		p.openInventory(new InventoryBackupListInventory(playerdata, target).getInventory());
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		p.openInventory(new InventoryBackupListInventory(playerdata, target).getInventory());
		
	}

}
