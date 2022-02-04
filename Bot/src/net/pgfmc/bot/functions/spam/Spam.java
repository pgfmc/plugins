package net.pgfmc.bot.functions.spam;

import java.util.Date;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.punish.Punish;

public class Spam {
	
	public static void check(Player p)
	{
		PlayerData pd = PlayerData.getPlayerData(p);
		
		// current time - last chat time <= 850ms, is not muted, does not have exemption
		if (!Punish.isMute(pd) && !Permissions.has(pd.getPlayer(), "pgf.spam.exempt") && new Date().getTime() - (long) Optional.ofNullable(pd.getData("spam.lastchat")).orElse(1000L) <= 550)
		{
			byte warnings = (byte) Optional.ofNullable(pd.getData("spam.warnings")).orElse((byte) 1);
			pd.setData("spam.warnings", (byte) (warnings + (byte) 1));
			
			if (warnings <= 3)
			{
				pd.sendMessage(ChatColor.RED + "Please, slow down (" + warnings + "/3)!");
			}
			
			if (warnings >= 3 && !Permissions.has(pd.getPlayer(), "pgf.spam.exempt"))
			{
				pd.sendMessage(ChatColor.DARK_RED + "You've been muted for spamming (60 seconds).");
				Discord.sendAlert(Discord.simplePlayerEmbed(pd.getPlayer(), "has been muted for spamming.", Discord.RED).build());
				Punish.setMute(pd, true);
				addTimer(pd);
			}
			
		}
		
		pd.setData("spam.lastchat", new Date().getTime());
	}
	
	private static void addTimer(PlayerData pd)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			public void run() {
				Discord.sendAlert(Discord.simplePlayerEmbed(pd.getPlayer(), "has been unmuted.", Discord.GREEN).build());
				Punish.setMute(pd, false).sendMessage(ChatColor.GREEN + "You've been unmuted.");;
				pd.setData("spam.warnings", (byte) 0);
			}
		}, 60 * 20);
	}
	
}
