package net.pgfmc.modtools.tools.tag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class HasTag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 2)
		{
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		String path = args[1];
		
		if (p == null)
		{
			sender.sendMessage("§cPlayer not found");
			return true;
		}
		PlayerData pd = PlayerData.getPlayerData(p);
		Object tagObj = pd.getData(path);
		
		if (tagObj == null)
		{
			sender.sendMessage(ChatColor.GOLD + pd.getRankedName() + " does not have tag set at " + path);
			return true;
		}
		
		boolean tag = (boolean) tagObj;
		
		if (!tag)
		{
			sender.sendMessage(ChatColor.GOLD + pd.getRankedName() + "'s tag is false at " + path);
		} else
		{
			sender.sendMessage(ChatColor.GOLD + pd.getRankedName() + "'s tag is true at " + path);
		}
		
		return true;
	}

}
