package net.pgfmc.core.api.inventory.extra;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;

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
		
		if (e.getClickedInventory() == null || e.getInventory() == null) return;

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        PlayerData pd = PlayerData.from(player);

        if ((e.getClickedInventory().getHolder() instanceof BaseInventory)) {
            e.setCancelled(true);
			((BaseInventory) e.getClickedInventory().getHolder()).press(e.getSlot(), e);
        } else if (pd.hasTag("reduceInventory") && disallowedActions.contains(e.getAction())) {
            e.setCancelled(true);
        }
	}
	
    @EventHandler
	public void onslideEvent(InventoryDragEvent e) {
		if (e.getInventory().getHolder() instanceof BaseInventory) {
			e.setCancelled(true);
			return;
		}
	}

    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        PlayerData pd = PlayerData.from((Player) e.getPlayer());
        if (e.getInventory().getHolder() instanceof BaseInventory) {
            pd.removeTag("reduceInventory");
        }
    }

    @EventHandler
    public void onOpenEvent(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        PlayerData pd = PlayerData.from((Player) e.getPlayer());
        if (e.getInventory().getHolder() instanceof BaseInventory) {
            pd.addTag("reduceInventory");
        }
    }




    
}
