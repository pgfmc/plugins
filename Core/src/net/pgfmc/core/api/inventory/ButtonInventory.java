package net.pgfmc.core.api.inventory;

import java.util.List;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.inventory.extra.Buttonable;

public abstract class ButtonInventory extends ListInventory<Buttonable> {

	@Deprecated
	public ButtonInventory(int size, String name) {
		super(size, name);
	}
	
	public ButtonInventory(InventoryType type, String name) {
		super(type.getDefaultSize(), name);
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
