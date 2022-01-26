package net.pgfmc.core.requests.cmd;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.cmd.base.PlayerCmd;
import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Inventoryable;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class RequestListCommand extends PlayerCmd implements Inventoryable {

	public RequestListCommand() {
		super("requestList");
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
		return new RequestListInventory();
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

}
