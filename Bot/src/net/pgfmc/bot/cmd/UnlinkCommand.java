package net.pgfmc.bot.cmd;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class UnlinkCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;	
		}
			
		PlayerData pd = PlayerData.from((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null) {
			
			pd.setData("Discord", null).save();
			Roles.recalculate(pd);
			pd.sendMessage("§cYour Discord account has been unlinked.");
			return true;
			
		} else {
			
			pd.sendMessage("§cYou dont have a Discord account to unlink.");
			return true;
		}
	}
}
