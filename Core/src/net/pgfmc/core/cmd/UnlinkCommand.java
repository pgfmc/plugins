package net.pgfmc.core.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public class UnlinkCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			
			return true;	
		}
		
		final Player player = (Player) sender;
		PlayerData playerdata = PlayerData.from(player);
		
		if (playerdata.getData("discord") != null)
		{
			playerdata.setData("discord", null).queue().send();
			playerdata.sendMessage(ChatColor.RED + "Your Discord account has been unlinked.");
			
			return true;
			
		}
		
		playerdata.sendMessage(ChatColor.RED + "You dont have a Discord account to unlink.");
		
		return true;
	}
	
}
