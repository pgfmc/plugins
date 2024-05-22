package net.pgfmc.survival.menu.staff.inventorybackups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
		super(target.getRankedName() + "'s Inventories");
		
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
		ChatColor itemColor = ChatColor.WHITE;
		
		if (totalItemStackCount == 0) {
			itemDyeColor = Material.WHITE_DYE;
			itemColor = ChatColor.WHITE;
			
		} else if (totalItemStackCount <= 9) {
			itemDyeColor = Material.GRAY_DYE;
			itemColor = ChatColor.GRAY;
			
		} else if (totalItemStackCount <= 18) {
			itemDyeColor = Material.YELLOW_DYE;
			itemColor = ChatColor.YELLOW;
			
		} else if (totalItemStackCount < 27) {
			itemDyeColor = Material.ORANGE_DYE;
			itemColor = ChatColor.GOLD;
			
		} else if (totalItemStackCount == 27) {
			itemDyeColor = Material.RED_DYE;
			itemColor = ChatColor.RED;
			
		}
		
		List<String> inventoryContentsAsStrings = inventoryContents.stream().map(item -> item.getType().toString()).collect(Collectors.toList());
		inventoryContentsAsStrings.add(0, ChatColor.GRAY + "");
		
		return new ItemWrapper(itemDyeColor).n(itemColor + "(Cause: " + entry.getCause().name() + ") " + InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
				.l(inventoryContentsAsStrings).gi();
		
		
	}

}
