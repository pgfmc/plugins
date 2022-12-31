package net.pgfmc.core.inventoryapi;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryapi.extra.Butto;
import net.pgfmc.core.inventoryapi.extra.Buttonable;

public abstract class ButtonInventory extends ListInventory<Buttonable> {

	public ButtonInventory(int size, String name) {
		super(size, name);
	}

	@Override
	protected abstract List<Buttonable> load();

	@Override
	protected Butto toAction(Buttonable entry) {
		return entry.toAction();
	}

	@Override
	protected ItemStack toItem(Buttonable entry) {
		return entry.toItem();
	}
}
