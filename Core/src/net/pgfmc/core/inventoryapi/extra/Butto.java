package net.pgfmc.core.inventoryapi.extra;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * interface to add functionality to the buttons.
 * @author CrimsonDart
 *
 */
@FunctionalInterface
public interface Butto {
	
	public void press(Player p, InventoryClickEvent e);
	
	/**
	 * The default button function used in place of null.
	 */
	public static final Butto defaultButto = (pd, e) -> {};
}
