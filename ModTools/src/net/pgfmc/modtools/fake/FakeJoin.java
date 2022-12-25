package net.pgfmc.modtools.fake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class FakeJoin implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		PlayerData pd = PlayerData.from((Player) sender);
		
		if (pd.hasTag("fake-leave"))
		{
			pd.sendMessage(ChatColor.RED + "Can't fake join again!");
			return true;
		}
		
		Bukkit.broadcastMessage("§7[§a+§7]§r " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getDisplayName());
		
		pd.removeTag("fake-leave");
		
		return true;
	}

}
