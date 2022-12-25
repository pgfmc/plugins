package net.pgfmc.bot.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.bot.functions.AccountLinking;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class LinkCommand implements CommandExecutor {

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
			pd.sendMessage(ChatColor.RED + "Your Discord has already been linked.");
			pd.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/unlink" + ChatColor.RED + " to unlink your account.");
			return true;
		}
		
		pd.sendMessage(ChatColor.GREEN + "Generating code...");
		Thread thread = new Thread() {
			public void run() {
				String code = AccountLinking.generateCode();
				pd.setData("linkCode", code);
				sender.sendMessage(ChatColor.GOLD + "Message the code " + ChatColor.WHITE + "[ " + code + " ]" + ChatColor.GOLD + " to " + ChatColor.GREEN + "PGF.bot " + ChatColor.GOLD + "in dms");
				sender.sendMessage(ChatColor.GOLD + "to link your account.");
				
				Bukkit.getLogger().warning("Account linking Thread ended!");
			}
		};
		thread.start();
		Bukkit.getLogger().warning("Account linking Thread started!");
		
		return true;
	}
	
	
}