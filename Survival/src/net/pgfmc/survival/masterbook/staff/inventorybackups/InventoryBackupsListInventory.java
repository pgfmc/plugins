package net.pgfmc.survival.masterbook.staff.inventorybackups;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.masterbook.staff.StaffInventory;

public class InventoryBackupsListInventory extends ListInventory<PlayerData> {
	
	private PlayerData playerdata;

	public InventoryBackupsListInventory(final PlayerData playerdata) {
		super("Select Player");
		
		this.playerdata = playerdata;
		
		setBack(0, new StaffInventory(playerdata).getInventory());
		
	}

	@Override
	protected List<PlayerData> load() {
		// Converts to List<PlayerData>
		return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline())
								.stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(PlayerData entry) {
		return (player, event) -> {
			player.openInventory(new InventoryBackupList(playerdata, entry).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(PlayerData entry) {
		return new ItemWrapper(Skull.getHead(entry.getUniqueId())).n(entry.getRankedName()).l(ChatColor.GRAY + "Click to see backups.").gi();
	}

}
