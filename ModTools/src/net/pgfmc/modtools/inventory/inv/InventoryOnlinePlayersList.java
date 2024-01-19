package net.pgfmc.modtools.inventory.inv;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.inventory.inv.inv.InventoryBackupList;

public class InventoryOnlinePlayersList extends ListInventory<PlayerData> {
	
	public InventoryOnlinePlayersList() {
		super(27, "Online Players");
		
	}

	@Override
	protected List<PlayerData> load() {
		return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline()).stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(PlayerData entry) {
		
		return (p, e) -> {
			p.closeInventory();
			p.openInventory(new InventoryBackupList(entry).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(PlayerData entry) {
		return new ItemWrapper(Material.PLAYER_HEAD).n(entry.getRankedName()).gi();
		
	}

}
