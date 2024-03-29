package net.pgfmc.survival.cmd.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class Homes extends PlayerCommand {

	public Homes(String name) {
		super(name);
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Location> getHomes(PlayerData pd)
	{
		return (HashMap<String, Location>) Optional.ofNullable(pd.getData("homes")).orElse(new HashMap<String, Location>());
	}
	
	
	public static List<String> getNamedHomes(PlayerData pd)
	{
		List<String> list = new ArrayList<>();
		for (String s : getHomes(pd).keySet()) {
			list.add(s);
		}
		return list;
		
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		pd.sendMessage(ChatColor.GREEN + "Homes: " + ChatColor.GOLD + Homes.getNamedHomes(pd));
		return true;
	}
	

}