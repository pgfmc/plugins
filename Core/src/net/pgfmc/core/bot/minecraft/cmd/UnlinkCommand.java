package net.pgfmc.core.bot.minecraft.cmd;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.roles.Roles;

public class UnlinkCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			
			return true;	
		}
			
		PlayerData pd = PlayerData.from((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null)
		{
			pd.setData("Discord", null).save();
			Roles.setRole(pd);
			pd.sendMessage(ChatColor.RED + "Your Discord account has been unlinked.");
			
			return true;
			
		}
		
		pd.sendMessage(ChatColor.RED + "You dont have a Discord account to unlink.");
		
		return true;
	}
	
}
