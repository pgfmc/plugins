package net.pgfmc.core.inventoryAPI.extra;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import net.pgfmc.core.inventoryAPI.BaseInventory;

public class InventoryPressEvent implements Listener {
	
	private static Set<InventoryAction> disallowedActions = EnumSet.of(
			InventoryAction.HOTBAR_MOVE_AND_READD,
			InventoryAction.HOTBAR_SWAP,
			InventoryAction.MOVE_TO_OTHER_INVENTORY,
			InventoryAction.COLLECT_TO_CURSOR,
			InventoryAction.UNKNOWN
		);
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof BaseInventory) {
			if (disallowedActions.contains(e.getAction())) {
				e.setCancelled(true);
			}
		}
		
		if (e.getClickedInventory().getHolder() instanceof BaseInventory && e.getWhoClicked() instanceof Player) {
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