package net.pgfmc.bot.cmd;

import org.bukkit.Bukkit;
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
			sender.sendMessage("§Only players can execute this command.");
			return true;
		}
		
		PlayerData pd = PlayerData.from((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null)
		{
			pd.sendMessage("§cYour Discord has already been linked.");
			pd.sendMessage("§cUse §r/unlink §cto unlink your account.");
			return true;
		}
		
		pd.sendMessage("§aGenerating code...");
		Thread thread = new Thread() {
			public void run() {
				String code = AccountLinking.generateCode();
				pd.setData("linkCode", code);
				sender.sendMessage("§6Message the code §f[ " + code + " ] §6to §aPGF.bot §6in dms");
				sender.sendMessage("§6to link your account.");
				
				Bukkit.getLogger().warning("Account linking Thread ended!");
			}
		};
		thread.start();
		Bukkit.getLogger().warning("Account linking Thread started!");
		
		return true;
	}
	
	
}