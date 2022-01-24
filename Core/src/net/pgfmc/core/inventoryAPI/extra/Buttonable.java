package net.pgfmc.core.inventoryAPI.extra;

import org.bukkit.inventory.ItemStack;

/**
 * interface to be added to any class that can be translated into a button :)
 * @author CrimsonDart
 * @since 4.2.6
 * @version 4.2.6
 *
 */
public interface Buttonable {
	
	/**
	 * Returns The action that should run when this object is pressed.
	 */
	public abstract Butto toAction();
	
	/**
	 * Returns the way this object should look in an inventory.
	 */
	public abstract ItemStack toItem();
	
}
