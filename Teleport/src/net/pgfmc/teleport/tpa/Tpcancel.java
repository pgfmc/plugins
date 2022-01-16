package net.pgfmc.teleport.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.requestAPI.Requester.Reason;

public class Tpcancel implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player a = (Player) sender;
		
		if (args.length >= 1) // player argument
		{
			Player b = Bukkit.getPlayer(args[0]);
			
			if (b == null)
			{
				a.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
				return true;
			}
			
			if (!TpRequest.TPA.deny(b, a))
			{
				a.sendMessage("§cNo requests to cancel.");
				return true;
			}
			
			return true;
		}
		
		TpRequest.TPA.expireRelations(a.getUniqueId(), Reason.Force);
		a.sendMessage("§cAll tpa requests cancelled.");
		
		return true;
	}
}
