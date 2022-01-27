package net.pgfmc.bot.cmd;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.bot.player.Roles;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class UnlinkCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("�cOnly players can execute this command.");
			return true;	
		}
			
		PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null) {
			
			pd.setData("Discord", null).queue();
			Roles.recalculateRoles(pd);
			Permissions.recalcPerms(pd);
			pd.sendMessage("�cYour Discord account has been unlinked.");
			return true;
			
		} else {
			
			pd.sendMessage("�cYou dont have a Discord account to unlink.");
			return true;
		}
	}
}
