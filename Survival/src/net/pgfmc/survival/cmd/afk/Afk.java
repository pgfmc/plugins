package net.pgfmc.survival.cmd.afk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.PGFAdvancement;
import net.pgfmc.core.api.playerdata.PlayerData;

public class Afk implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player player = (Player) sender;
		
		AfkEvents.toggleAfk(PlayerData.from(player));
		
		// Grants advancement
		//
		// A side effect of placing this here
		// is that the advancement is granted on either
		// enable or disable
		PGFAdvancement.DINNERS_READY.grantToPlayer(player);
		
		return true;
	}

}
