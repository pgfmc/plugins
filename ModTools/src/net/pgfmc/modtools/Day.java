package net.pgfmc.modtools;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Day implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		World w = ((Player) sender).getWorld();
		w.setTime(500);
		w.setWeatherDuration(1000);
		
		sender.sendMessage("§aWeather cleared!");
		
		return true;
	}

}
