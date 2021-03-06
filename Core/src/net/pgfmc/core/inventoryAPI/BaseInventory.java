package net.pgfmc.core.inventoryAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;

/**
 * The basic Inventory.
 * @author CrimsonDart
 * @version 4.0.2
 * @since 2.0.0
 *
 */
public abstract class BaseInventory implements InventoryHolder {

	// fields
		
	/**
	 * The list of functional buttons in an inventory.
	 */
	protected Butto[] buttons;
	
	/**
	 * The inventory itself.
	 */
	protected Inventory inv;
	
	public BaseInventory(int size, String name) {
		
		if (size != 27 && size != 54 && size != 5 && size != 9) return;
		
		
		this.inv = Bukkit.createInventory(this, size, name);
		
		buttons = new Butto[size];
	}
	
	public void setAction(int slot, Butto b) {
		if (slot + 1 > inv.getSize()) return;
		
		buttons[slot] = b;
	}
	
	public ItemWrapper setItem(int slot, ItemStack itemS) {
		if (slot + 1 > inv.getSize()) return null;
		
		ItemWrapper iw = new ItemWrapper(itemS);
		
		Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
			inv.setItem(slot, iw.gi());
		}, 0);
		
		return iw;
	}
	
	public ItemWrapper setItem(int slot, Material mat) {
		if (slot + 1 > inv.getSize()) return null;
		
		ItemWrapper iw = new ItemWrapper(mat);
		Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
			inv.setItem(slot, iw.gi());
		}, 0);
		
		return iw;
	}
	
	public void set(int slot, Buttonable button) {
		if (slot + 1 > inv.getSize()) return;
		
		buttons[slot] = button.toAction();
		Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
			inv.setItem(slot, button.toItem());
		}, 0);
	}
	
	/*
	 * Returns all buttons from the inventory.
	 * Changes to the returned array will NOT reflect changes in the inventory.
	 */
	public final Butto[] getActions() {
		return buttons.clone();
	}
	
	/**
	 * Get the button for the input index.
	 * @param index The slot of the inventory.
	 * @return Returns the button at the input slot, and null if there is no button at that slot.
	 */
	public Butto getAction(int index) {
		return buttons[index];
	}
	
	/**
	 * Presses the Button in slot <slot>.
	 * @param slot The slot that was clicked.
	 * @param p The player that clicked the slot.
	 * @param e The InventoryClickEvent that caused the press.
	 */
	public final void press(int slot, InventoryClickEvent e) {
		
		if (slot + 1 > inv.getSize()) return;
		
		Butto b = buttons[slot];
		if (b!= null) b.press((Player) e.getWhoClicked(), e);
		return;
	}
	
	/**
	 * Returns the Inventory's Inventory Object.
	 */
	@Override
	public Inventory getInventory() {
		return inv;
	}
}