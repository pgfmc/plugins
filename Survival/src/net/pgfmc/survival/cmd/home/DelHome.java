package net.pgfmc.survival.cmd.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class DelHome extends PlayerCommand {

	public DelHome(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) return null;
		if (args.length == 1) {
			
			for (String s : Homes.getNamedHomes(pd)) {
				if (s.startsWith(args[0])) {
					list.add(s);
				}
			}
		}
		return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		if (args.length == 0)
		{
			return false;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(pd);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			homes.remove(name);
			pd.setData("homes", homes).queue();
			pd.sendMessage(ChatColor.GREEN + "Home " + ChatColor.GOLD + name + ChatColor.GREEN + " removed!");
		} else
		{
			pd.sendMessage(ChatColor.RED + "Could not find home " + ChatColor.GOLD + name + ChatColor.RED + ".");
		}
		
		return true;
	}
	
	

}
