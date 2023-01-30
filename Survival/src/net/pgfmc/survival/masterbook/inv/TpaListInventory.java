package net.pgfmc.survival.masterbook.inv;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;

public class TpaListInventory extends ListInventory<Player> {
	
	private PlayerData pd;

	public TpaListInventory(PlayerData pd) {
		super(27, ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Select who to teleport to!");
		
		this.pd = pd;
		
		setBack(0, new MasterbookInventory(pd).getInventory());
	}

	@Override
	public List<Player> load() {
		return Bukkit.getOnlinePlayers().stream()
				.filter(player -> !player.getUniqueId().equals(pd.getUniqueId()))
				.collect(Collectors.toList());
	}
	
	@Override
	protected Butto toAction(Player entry) {
		
		return (p, e) -> {
			p.performCommand("tpa " + entry.getName());
			p.openInventory(new MasterbookInventory(pd).getInventory());
		};
		
	}
	
	@Override
	protected ItemStack toItem(Player entry) {
		return new ItemWrapper(Material.PLAYER_HEAD).n(ChatColor.RESET + "" + ChatColor.GREEN + entry.getName()).gi();
	}
	
}
