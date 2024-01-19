package net.pgfmc.modtools.inventory.inv;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.inventory.inv.inv.InventoryBackupList;

public class InventoryOnlinePlayersList extends ListInventory<Player> {
	
	public InventoryOnlinePlayersList() {
		super(InventoryType.CHEST.getDefaultSize(), "Online Players");
		
	}

	@Override
	protected List<Player> load() {
		return new ArrayList<Player>(Bukkit.getOnlinePlayers());
	}

	@Override
	protected Butto toAction(Player entry) {
		
		return (p, e) -> {
			p.closeInventory();
			p.openInventory(new InventoryBackupList(PlayerData.from(entry)).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(Player entry) {
		return new ItemWrapper(Material.PLAYER_HEAD).n(ChatColor.RESET + "" + ChatColor.GREEN + entry.getName()).gi();
		
	}

}
