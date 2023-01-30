package net.pgfmc.survival.masterbook.inv.home.inv;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.masterbook.inv.MasterbookInventory;

public class HomeSet extends ConfirmInventory {
	
	private PlayerData pd;
	private HashMap<String, Location> homes;

	protected HomeSet(PlayerData pd) {
		super(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Set home here?", ChatColor.RESET + "" + ChatColor.GREEN + "Set Home", ChatColor.RESET + "" + ChatColor.GRAY + "Cancel");
		
		this.pd = pd;
		this.homes = Homes.getHomes(pd);
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		
		if (!p.hasPermission("pgf.cmd.home.set"))
		{
			p.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
			return;
		}
		
		if (p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 5)
		{
			p.sendMessage(ChatColor.RED + "You can only have up to 5 homes: " + Homes.getNamedHomes(pd));
			return;
		} else if (!p.hasPermission("pgf.cmd.donator.home") && homes.size() >= 3)
		{
			p.sendMessage(ChatColor.RED + "You can only have up to 3 homes: " + Homes.getNamedHomes(pd));
			return;
		}
		
		pd.setData("tempHomeLocation", pd.getPlayer().getLocation()); // TODO O_O
		p.closeInventory();
		
		pd.sendMessage(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Type into chat to set the name of your Home!");
		pd.sendMessage(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "You can only name the home for 4 minutes.");
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				if (pd.getData("tempHomeLocation") == null) return;
				
				pd.setData("tempHomeLocation", null);
				pd.sendMessage(ChatColor.RESET + "" + ChatColor.RED + "Your home could not be set.");
				
			}
			
		}, 20 * 60 * 4);
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		p.openInventory(new MasterbookInventory(pd).getInventory());
		
	}

}
