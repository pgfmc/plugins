package net.pgfmc.survival.menu.tpa;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.menu.teleports.Teleports;

public class TpaListInventory extends ListInventory<PlayerData> {
	
	private PlayerData pd;

	public TpaListInventory(PlayerData pd) {
		super(27, "Teleport Menu");
		
		this.pd = pd;
		
		setBack(0, new Teleports(pd).getInventory());
	}

	@Override
	public List<PlayerData> load() {
		// Converts to List<Player> (ignores/removes the executor)
				return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline() && playerdata != this.pd)
									.stream().collect(Collectors.toList());
	}
	
	@Override
	protected Butto toAction(PlayerData entry) {
		
		return (player, event) -> {
			player.performCommand("tpa " + entry.getName());
			player.closeInventory();
		};
		
	}
	
	@Override
	protected ItemStack toItem(PlayerData entry) {
		return new ItemWrapper(Skull.getHead(entry.getUniqueId())).n(ChatColor.GREEN + entry.getRankedName()).gi();
	}
	
}
