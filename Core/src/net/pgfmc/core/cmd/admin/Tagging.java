package net.pgfmc.core.cmd.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class Tagging implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 3)
		{
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		String path = args[1];
		String tag = args[2];
		
		if (p == null)
		{
			sender.sendMessage("§cPlayer not found");
			return true;
		}
		PlayerData pd = PlayerData.getPlayerData(p);
		pd.setData(path, tag).queue();
		
		sender.sendMessage(pd.getRankedName() + " §ahas been tagged! §6" + path + " -> " + tag);
		
		// true/false for feedback to target
		if (args.length >= 4 && Boolean.valueOf(args[3]))
		{
			p.sendMessage("§aYou have been tagged! §6" + path + " -> " + tag);
		}
		
		return true;
	}

}