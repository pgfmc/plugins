package net.pgfmc.teleport.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpaccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player b = (Player) sender;
		
		if (args.length >= 1) // has player argument
		{
			Player a = Bukkit.getPlayer(args[0]);
			if (a == null)
			{
				b.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
				return true;
			}
			
			if (!TpRequest.TPA.accept(a, b))
			{
				b.sendMessage("§cCould not find a teleport request.");
				return true;
			}
			
			return true;
		}
		
		if (!TpRequest.TPA.accept(b))
		{
			b.sendMessage("§cCould not find a teleport request.");
			return true;
		}
		
		return true;
	}

}
