package net.pgfmc.teleport.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Deprecated
// I will come back to this probably later (never)
public class Tpahere implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
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
		
		Player b = Bukkit.getPlayer(args[0]);
		if (b == null)
		{
			a.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
			return true;
		}
		
		//TpRequest.TPAHERE.createRequest(a, b);
		
		return true;
	}
}
