package net.pgfmc.core.api.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ConfirmInventory extends BaseInventory {

	protected ConfirmInventory(String name, String confirm, String cancel) {
		super(27, name);
		
		
		setItem(11, Material.GREEN_CONCRETE).n(confirm);
		setItem(15, Material.RED_CONCRETE).n(cancel);
		setAction(11, (p, e) -> confirmAction(p, e));
		setAction(15, (p, e) -> cancelAction(p, e));
	}
	
	protected abstract void confirmAction(Player p, InventoryClickEvent e);
	protected abstract void cancelAction(Player p, InventoryClickEvent e);
	
}
