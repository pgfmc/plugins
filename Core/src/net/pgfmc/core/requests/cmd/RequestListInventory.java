package net.pgfmc.core.requests.cmd;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.ButtonInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.inventoryAPI.extra.Inventoryable;

public class RequestListInventory extends ButtonInventory implements Inventoryable {

	public RequestListInventory() {
		super(27, "");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Butto toAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack toItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseInventory toInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Buttonable> load() {
		// TODO Auto-generated method stub
		return null;
	}

}
