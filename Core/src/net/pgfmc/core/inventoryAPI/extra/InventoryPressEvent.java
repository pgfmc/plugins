package net.pgfmc.core.inventoryAPI.extra;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import net.pgfmc.core.inventoryAPI.BaseInventory;

public class InventoryPressEvent implements Listener {
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof BaseInventory && e.getWhoClicked() instanceof Player) {
			e.setCancelled(true);
			((BaseInventory) e.getClickedInventory().getHolder()).press(e.getSlot(), e);
		}
	}
	
	public void onslideEvent(InventoryDragEvent e) {
		if (e.getInventory().getHolder() instanceof BaseInventory) {
			e.setCancelled(true);
			return;
		}
	}
}