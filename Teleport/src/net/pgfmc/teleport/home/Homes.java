package net.pgfmc.teleport.home;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class Homes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		Player p = (Player) sender;
		
		p.sendMessage("§aHomes: §6" + Homes.getNamedHomes(p));
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Location> getHomes(OfflinePlayer p)
	{
		return Optional.ofNullable((HashMap<String, Location>) PlayerData.getData(p, "homes")).orElse(new HashMap<String, Location>());
	}
	
	/*
	 * Gets an List<Object> of named homes
	 * It says Object, but it is String
	 */
	public static List<Object> getNamedHomes(OfflinePlayer p)
	{
		return Arrays.asList(getHomes(p).keySet().toArray());
	}
	

}