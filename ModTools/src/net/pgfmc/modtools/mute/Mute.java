package net.pgfmc.modtools.mute;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.punish.Punish;

public class Mute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) return false;
		
		PlayerData pd = PlayerData.getPlayerData(args[0]);
		
		if (pd == null)
		{
			sender.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
			return true;
		}
		
		if (pd.hasPermission("pgf.mute.exempt") || (sender.getName().equals(pd.getName())))
		{
			sender.sendMessage(ChatColor.RED + "This player cannot be muted!");
			return true;
		}
		
		if (Punish.isMute(pd))
		{
			sender.sendMessage(ChatColor.RED + "This player is already muted!");
			return true;
		}
		
		if (!CoreMain.PGFPlugin.BOT.isEnabled())
		{
			sender.sendMessage(ChatColor.RED + "Cannot mute/unmute, PGF-Bot is disabled.");
			// no return because yeah
		}
		
		sender.sendMessage(pd.getRankedName() + ChatColor.RED + " has been muted!");
		pd.sendMessage(ChatColor.RED + "You have been muted!");
		Punish.setMute(pd, true);
		
		return true;
	}

}
