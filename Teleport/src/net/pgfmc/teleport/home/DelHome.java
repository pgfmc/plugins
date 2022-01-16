package net.pgfmc.teleport.home;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class DelHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		Player p = (Player) sender;
		
		if (args.length == 0)
		{
			return false;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(p);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			homes.remove(name);
			PlayerData.setData(p, "homes", homes).queue();
			p.sendMessage("§aHome §6" + name + "§a removed!");
		} else
		{
			p.sendMessage("§cCould not find home §6" + name + "§c.");
		}
		
		return true;
	}
	
	

}
