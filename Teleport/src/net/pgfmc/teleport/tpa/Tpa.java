package net.pgfmc.teleport.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Tpa implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player a = (Player) sender;
		
		if (args.length == 0) // gui/ no player argument
		{
			// TODO GUI
			return false;
		}
		
		System.out.print("Hi");
		Player b = Bukkit.getPlayer(args[0]);
		if (b == null)
		{
			a.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
			return true;
		} else if (b == a) {
			a.sendMessage("§cYou cannot teleport to yourself.");
			return true;
		}
		
		TpRequest.TPA.createRequest(a, b);
		
		return true;
	}

}
