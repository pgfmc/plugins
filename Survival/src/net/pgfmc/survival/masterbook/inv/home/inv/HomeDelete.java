package net.pgfmc.survival.masterbook.inv.home.inv;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.cmd.home.Homes;

public class HomeDelete extends ListInventory<String> {
	
	private PlayerData pd;
	private HashMap<String, Location> homes;

	public HomeDelete(PlayerData pd) {
		super(27, ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Delete Home");
		
		this.pd = pd;
		this.homes = Homes.getHomes(pd);
		
		setBack(0, new HomeHomepage(pd).getInventory());
	}

	@Override
	public List<String> load() {
		return Homes.getHomes(pd).keySet().stream().collect(Collectors.toList());
	}
	
	@Override
	protected Butto toAction(String entry) {
		
		if (!pd.hasPermission("pgf.cmd.home.del"))
		{
			return (player, event) -> {
				player.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
			};
		}
		
		if (homes.size() == 0)
		{
			return (player, event) -> {
				player.sendMessage(ChatColor.RED + "You do not have any homes.");
			};
		}
		
		return (player, event) -> {
			player.performCommand("delhome " + entry);
			player.openInventory(new HomeHomepage(pd).getInventory());
		};
		
	}
	
	@Override
	protected ItemStack toItem(String entry) {
		return new ItemWrapper(Material.PAPER).n(ChatColor.RESET + "" + ChatColor.GOLD + entry).gi();
	}

}
