package net.pgfmc.modtools.rollback.inv;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.modtools.rollback.inv.inv.RollbackBackupListInventory;

public class RollbackOnlinePlayersListInventory extends ListInventory<Player> {
	
	public RollbackOnlinePlayersListInventory() {
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
			p.openInventory(new RollbackBackupListInventory(PlayerData.from(entry)).getInventory());
		};
		
	}

	@Override
	protected ItemStack toItem(Player entry) {
		return new ItemWrapper(Material.PLAYER_HEAD).n(ChatColor.RESET + "" + ChatColor.GREEN + entry.getName()).gi();
		
	}

}