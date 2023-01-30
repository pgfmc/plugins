package net.pgfmc.survival.masterbook.inv.home.inv;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.masterbook.inv.MasterbookInventory;

public class HomeHomepage extends BaseInventory {
	
	public HomeHomepage(PlayerData pd)
	{
		super(27, ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Home");
		
		setBack(0, new MasterbookInventory(pd).getInventory());
		
		setAction(13, (p, e) -> {
			p.openInventory(new HomeSelect(pd).getInventory());
		});
		
		setItem(13, Material.ENDER_PEARL).n(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Go to Home");
		
		setAction(11, (p, e) -> {
			p.openInventory(new HomeSet(pd).getInventory());
		});
		
		setItem(11, Material.OAK_SAPLING).n(ChatColor.RESET + "" + ChatColor.GREEN + "Set Home");
		
		setAction(15, (p, e) -> {
			p.openInventory(new HomeDelete(pd).getInventory());
		});
		
		setItem(15, Material.FLINT_AND_STEEL).n(ChatColor.RESET + "" + ChatColor.RED + "Delete Home");
		
	}
	
}
