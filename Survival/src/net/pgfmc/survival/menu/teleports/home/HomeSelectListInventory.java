package net.pgfmc.survival.menu.teleports.home;

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
import net.pgfmc.survival.menu.teleports.Teleports;

public class HomeSelectListInventory extends ListInventory<String> {
	
	private PlayerData pd;
	private HashMap<String, Location> homes;
	
	public HomeSelectListInventory(PlayerData pd)
	{
		super(27, "Home Select");
		
		this.pd = pd;
		this.homes = Homes.getHomes(pd);
		
		setBack(0, new Teleports(pd).getInventory());
	}
	
	@Override
	public List<String> load() {
		return homes.keySet().stream().collect(Collectors.toList());
	}
	
	@Override
	protected Butto toAction(String entry) {
		
		if (!pd.hasPermission("net.pgfmc.survival.home.home"))
		{
			return (player, event) -> {
				pd.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
			};
			
		}
		
		if (homes.size() == 0)
		{
			return (player, event) -> {
				pd.sendMessage(ChatColor.RED + "You do not have any homes.");
			};
			
		}
		
		return (player, event) -> {
			player.performCommand("home " + entry);
			player.closeInventory();
		};
		
	}
	
	@Override
	protected ItemStack toItem(String entry) {
		return new ItemWrapper(Material.PAPER).n(ChatColor.GOLD + entry).gi();
	}
	
}
