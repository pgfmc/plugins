package net.pgfmc.survival.masterbook.staff.inventorybackups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.masterbook.staff.inventorybackups.noninv.InventoryBackup;
import net.pgfmc.survival.masterbook.staff.inventorybackups.noninv.InventoryBackupScheduler;

public class InventoryBackupList extends ListInventory<InventoryBackup> {
	
	PlayerData playerdata;
	PlayerData target;

	public InventoryBackupList(PlayerData playerdata, PlayerData target) {
		super(target.getRankedName() + "'s Inventories");
		
		this.playerdata = playerdata;
		this.target = target;
		
		setBack(0, new InventoryBackupsListInventory(playerdata).getInventory());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<InventoryBackup> load() {		
		return (List<InventoryBackup>) Optional.ofNullable(((List<InventoryBackup>) target.getData("inventories")))
		.orElse(new ArrayList<InventoryBackup>());
	}

	@Override
	protected Butto toAction(InventoryBackup entry) {
		
		return (p, e) -> {
			p.openInventory(new InventoryBackupConfirm(playerdata, target, entry).getInventory());
			
		};
		
	}

	@Override
	protected ItemStack toItem(InventoryBackup entry) {
		final int totalItemStackCount = Arrays.asList(entry.getInventoryContents())
								.stream()
								.filter(item -> item != null)
								.collect(Collectors.toList())
								.size();
		
		Material itemDyeColor = Material.BARRIER;
		
		if (totalItemStackCount == 0)
		{
			itemDyeColor = Material.WHITE_DYE;
		} else if (totalItemStackCount <= 9)
		{
			itemDyeColor = Material.GRAY_DYE;
		} else if (totalItemStackCount <= 18)
		{
			itemDyeColor = Material.YELLOW_DYE;
		} else if (totalItemStackCount < 27)
		{
			itemDyeColor = Material.ORANGE_DYE;
		} else if (totalItemStackCount == 27)
		{
			itemDyeColor = Material.RED_DYE;
		}
		
		return new ItemWrapper(itemDyeColor)
				.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
				.l(entry.getCause().name())
				.gi();
		
		
	}

}
