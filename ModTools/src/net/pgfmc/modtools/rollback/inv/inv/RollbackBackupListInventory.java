package net.pgfmc.modtools.rollback.inv.inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.rollback.RollbackBackup;
import net.pgfmc.modtools.rollback.RollbackScheduler;
import net.pgfmc.modtools.rollback.inv.RollbackOnlinePlayersListInventory;
import net.pgfmc.modtools.rollback.inv.inv.inv.RollbackBackupConfirmInventory;

public class RollbackBackupListInventory extends ListInventory<RollbackBackup> {
	
	PlayerData pd;

	public RollbackBackupListInventory(PlayerData pd) {
		super(InventoryType.CHEST.getDefaultSize(), pd.getRankedName() + "'s Inventories");
		
		this.pd = pd;
		
		setBack(0, new RollbackOnlinePlayersListInventory().getInventory());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<RollbackBackup> load() {		
		return (List<RollbackBackup>) Optional.ofNullable(((List<RollbackBackup>) pd.getData("inventories")))
		.orElse(new ArrayList<RollbackBackup>());
	}

	@Override
	protected Butto toAction(RollbackBackup entry) {
		
		return (p, e) -> {
			p.closeInventory();
			p.openInventory(new RollbackBackupConfirmInventory(RollbackScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()) + " - " + entry.getCause().name(), entry).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(RollbackBackup entry) {
		return new ItemWrapper(Material.WRITABLE_BOOK).n(RollbackScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate())).l(entry.getCause().name()).gi();
	}

}
