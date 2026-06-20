package net.pgfmc.survival.menu.staff.inventorybackups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.menu.staff.inventorybackups.noninv.InventoryBackup;
import net.pgfmc.survival.menu.staff.inventorybackups.noninv.InventoryBackupScheduler;
import net.pgfmc.survival.menu.staff.manageplayers.ManagePlayerInventory;

public class InventoryBackupListInventory extends ListInventory<InventoryBackup> {
	
	PlayerData playerdata;
	PlayerData target;

	public InventoryBackupListInventory(PlayerData playerdata, PlayerData target) {
		super(Component.text().append(target.getRankedName())
                .append(Component.text("'s Inventories")).build());
		
		this.playerdata = playerdata;
		this.target = target;
		
		setBack(0, new ManagePlayerInventory(playerdata, target).getInventory());
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
			p.openInventory(new InventoryBackupConfirmInventory(playerdata, target, entry).getInventory());
			
		};
		
	}

	@Override
	protected ItemStack toItem(InventoryBackup entry) {
		final List<ItemStack> inventoryContents = Arrays.asList(entry.getInventoryContents()).stream()
																			.filter(item -> item != null)
																			.collect(Collectors.toList());
		final int totalItemStackCount = inventoryContents.size();
		
		Material itemDyeColor = Material.BARRIER;
		NamedTextColor itemColor = NamedTextColor.WHITE;
		
		if (totalItemStackCount == 0) {
			itemDyeColor = Material.WHITE_DYE;
			itemColor = NamedTextColor.WHITE;
			
		} else if (totalItemStackCount <= 9) {
			itemDyeColor = Material.GRAY_DYE;
			itemColor = NamedTextColor.GRAY;
			
		} else if (totalItemStackCount <= 18) {
			itemDyeColor = Material.YELLOW_DYE;
			itemColor = NamedTextColor.YELLOW;
			
		} else if (totalItemStackCount < 27) {
			itemDyeColor = Material.ORANGE_DYE;
			itemColor = NamedTextColor.GOLD;
			
		} else if (totalItemStackCount == 27) {
			itemDyeColor = Material.RED_DYE;
			itemColor = NamedTextColor.RED;
			
		}
		
		List<Component> inventoryContentsAsStrings = inventoryContents.stream().map(item -> Component.text(item.getType().toString(), NamedTextColor.GRAY)).collect(Collectors.toList());
		
		return new ItemWrapper(itemDyeColor)
            .name(Component.text("(Cause: " + entry.getCause().name() + ") " + InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()), itemColor))
			.lore(inventoryContentsAsStrings).item();
	}
}
