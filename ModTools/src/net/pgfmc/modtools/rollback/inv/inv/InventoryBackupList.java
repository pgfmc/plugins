package net.pgfmc.modtools.rollback.inv.inv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.rollback.InventoryBackup;
import net.pgfmc.modtools.rollback.InventoryBackupScheduler;
import net.pgfmc.modtools.rollback.inv.InventoryOnlinePlayersList;
import net.pgfmc.modtools.rollback.inv.inv.inv.InventoryBackupConfirm;

public class InventoryBackupList extends ListInventory<InventoryBackup> {
	
	PlayerData pd;

	public InventoryBackupList(PlayerData pd) {
		super(InventoryType.CHEST.getDefaultSize(), pd.getRankedName() + "'s Inventories");
		
		this.pd = pd;
		
		setBack(0, new InventoryOnlinePlayersList().getInventory());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<InventoryBackup> load() {		
		return (List<InventoryBackup>) Optional.ofNullable(((List<InventoryBackup>) pd.getData("inventories")))
		.orElse(new ArrayList<InventoryBackup>());
	}

	@Override
	protected Butto toAction(InventoryBackup entry) {
		
		return (p, e) -> {
			if (!p.hasPermission("pgf.admin.inventory.restore"))
			{
				p.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
				return;
			}
			
			p.closeInventory();
			p.openInventory(new InventoryBackupConfirm(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()) + " - " + entry.getCause().name(), entry).getInventory());
			
		};
		
	}

	@Override
	protected ItemStack toItem(InventoryBackup entry) {
		final int itemStackCount = Arrays.asList(entry.getInventory().getContents())
								.stream()
								.filter(item -> item != null)
								.collect(Collectors.toList())
								.size();
		
		if (itemStackCount == 0)
		{
			return new ItemWrapper(Material.WHITE_DYE)
					.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(entry.getCause().name())
					.gi();
		}
		
		if (itemStackCount <= 9)
		{
			return new ItemWrapper(Material.GRAY_DYE)
					.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(entry.getCause().name())
					.gi();
		}
		
		if (itemStackCount <= 18)
		{
			return new ItemWrapper(Material.YELLOW_DYE)
					.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(entry.getCause().name())
					.gi();
		}
		
		if (itemStackCount < 27)
		{
			return new ItemWrapper(Material.ORANGE_DYE)
					.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(entry.getCause().name())
					.gi();
		}
		
		if (itemStackCount == 27)
		{
			return new ItemWrapper(Material.RED_DYE)
					.n(InventoryBackupScheduler.INVENTORY_DATE_FORMAT.format(entry.getDate()))
					.l(entry.getCause().name())
					.gi();
		}
		
		return new ItemWrapper(Material.BARRIER).gi(); // This shouldn't happen lol
		
		
	}

}
