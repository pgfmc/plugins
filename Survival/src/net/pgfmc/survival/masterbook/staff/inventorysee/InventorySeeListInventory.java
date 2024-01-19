package net.pgfmc.survival.masterbook.staff.inventorysee;

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

public class InventorySeeListInventory extends ListInventory<PlayerData> {
	
	private PlayerData playerdata;

	public InventorySeeListInventory(final PlayerData playerdata) {
		super(27, "Select Inventory");
		
		this.playerdata = playerdata;
		
		setBack(0, new StaffInventory(playerdata).getInventory());
		
	}

	@Override
	protected List<PlayerData> load() {
		// Converts to List<Player> (ignores/removes the staff member)
		return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline())
								.stream().collect(Collectors.toList());
		// return PlayerData.getPlayerDataSet(playerdata -> playerdata.isOnline() && playerdata != this.playerdata)
							//.stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(PlayerData entry) {
		return (player, event) -> {
			player.openInventory(new InventorySeeSelectInventory(playerdata, entry).getInventory());
		};
	}

	@Override
	protected ItemStack toItem(PlayerData entry) {
		return new ItemWrapper(Skull.getHead(entry.getUniqueId())).n(entry.getRankedName()).l(ChatColor.GRAY + "Click to select an inventory.").gi();
	}

}
