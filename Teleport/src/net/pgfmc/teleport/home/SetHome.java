package net.pgfmc.teleport.home;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class SetHome implements CommandExecutor {

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
		
		setHome(p, String.join("_", args), p.getLocation());
		
		return true;
	}
	
	public static void setHome(Player p, String name, Location loc)
	{
		HashMap<String, Location> homes = Homes.getHomes(p);
		
		name = name.toLowerCase().strip().replace(" ", "_");
		
		if (homes.containsKey(name))
		{
			p.sendMessage("§cYou cannot have duplicate home names: §6" + name);
			return;
		}
		
		if (p.hasPermission("pgf.cmd.home.donator") && homes.size() >= 5)
		{
			p.sendMessage("§cYou can only have up to 5 homes: §6" + Homes.getNamedHomes(p));
			return;
		} else if (homes.size() >= 3)
		{
			p.sendMessage("§cYou can only have up to 3 homes: §6" + Homes.getNamedHomes(p));
			return;
		}
		
		if (loc != null)
		{
			homes.put(name, loc);
		} else
		{
			homes.put(name, p.getLocation());
		}
		
		p.sendMessage("§aSet home §6" + name + "§a!");
		PlayerData.setData(p, "homes", homes).queue();
	}

}
