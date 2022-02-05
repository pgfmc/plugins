package net.pgfmc.core.requests.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.Inventoryable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public class RequestListCommand extends PlayerCommand implements Inventoryable {

	public RequestListCommand() {
		super("requestList");
	}

	@Override
	public ItemStack toItem() {
		return null;
	}

	@Override
	public BaseInventory toInventory() {
		return new RequestListInventory();
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return new ArrayList<>();
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		Set<Request> rs = RequestType.getInAllRequests(x -> {
			return (x.target == pd);
		});
		
		for (Request r : rs) {
			pd.sendMessage(r.getType() + " Request from " + r.asker.getNicknameRaw());
		}
		
		return false;
	}

}
