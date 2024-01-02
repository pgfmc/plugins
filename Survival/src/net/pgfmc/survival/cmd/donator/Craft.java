package net.pgfmc.survival.cmd.donator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.PGFAdvancement;

public class Craft implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		p.openWorkbench(null, true);
		
		// Grants advancement
		PGFAdvancement.CRAFTING_TABLE_ON_DEMAND.grantToPlayer(p);
		
		return true;
	}

}
