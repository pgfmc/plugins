package net.pgfmc.modtools.tools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class Debug implements CommandExecutor {
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		PlayerData p = PlayerData.from((Player) sender);
		
		if (p != null) {
			
			boolean t = p.isDebug();
			
			if (t) {
				p.sendMessage("§6Debug disabled!");
				p.setDebug(false);
			} else {
				p.sendMessage("§6Debug enabled!");
				p.setDebug(true);
			}
		}
		return true;
	}
}
