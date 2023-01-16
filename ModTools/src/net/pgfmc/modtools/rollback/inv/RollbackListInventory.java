package net.pgfmc.modtools.rollback.inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.rollback.InventoryRollback;

public class RollbackListInventory extends ListInventory<RollbackInventory> {
	
	PlayerData pd;

	public RollbackListInventory(PlayerData pd) {
		super(InventoryType.CHEST.getDefaultSize(), pd.getRankedName() + "'s Backups");
		
		this.pd = pd;
		
		
	}

	@Override
	protected List<RollbackInventory> load() {
		@SuppressWarnings("unchecked")
		List<RollbackInventory> inventories = (List<RollbackInventory>) Optional.ofNullable(pd.getData("inventories")).orElse(new ArrayList<RollbackInventory>());
		
		return inventories;
		
	}

	@Override
	protected Butto toAction(RollbackInventory entry) {
		
		return (p, e) -> {
			p.closeInventory();
			p.openInventory(new RollbackConfirmInventory(InventoryRollback.INVENTORY_DATE_FORMAT.format(entry.getDate()), entry).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(RollbackInventory entry) {
		return new ItemWrapper(Material.WRITABLE_BOOK)
					.n(ChatColor.GOLD + InventoryRollback.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE
							+ "Experience: " + String.valueOf(entry.getExp())
							+ "\nInventory: " + entry.getInventoryContents().toString()
							+ "\nEnder Chest" + entry.getEchestInventoryContents().toString())
					.gi();
		
	}

}
