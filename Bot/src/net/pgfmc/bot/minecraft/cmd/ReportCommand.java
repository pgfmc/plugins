package net.pgfmc.bot.minecraft.cmd;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class ReportCommand implements CommandExecutor {
	
	private HashMap<String, Long> commandCooldowns = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		PlayerData pd = PlayerData.from((Player) sender);
		
		// 2 minute cooldown
		if (commandCooldowns.get(pd.getUniqueId().toString()) != null)
		{
			Double secondsSinceLastReport = (double) ((new Date().getTime() - commandCooldowns.get(pd.getUniqueId().toString())) / 1000);
			if (secondsSinceLastReport <= 120.0)
			{
				String timeLeft = String.valueOf(120.0 - secondsSinceLastReport);
				pd.sendMessage(ChatColor.RED + "Please wait " + timeLeft.substring(0, timeLeft.length() - 2) + " seconds before using this command again.");
				return true;
			}
		}
		
		if(args.length == 0) {
			pd.sendMessage("Please provide a message.");
			return true;
		}
		
		commandCooldowns.put(pd.getUniqueId().toString(), new Date().getTime());
		
		String message = String.join(" ", args);
		
		EmbedBuilder eb = Discord.simplePlayerEmbed(pd.getOfflinePlayer(), "sent a report!", Colors.BLACK);
		eb.addField("Report content", message, false);
		eb.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert("<@&595557113581797381>").embed(eb.build()).queue();
		
		String discordId = pd.getData("Discord");
		if (discordId != null)
		{
			EmbedBuilder eb2 = Discord.simplePlayerEmbed(pd.getOfflinePlayer(), "| Report received!", Colors.BLACK);
			eb2.setDescription("We've received your report! It'll be reviewed soon.");
			eb2.addField("Report content", message, false);
			eb2.setTimestamp(OffsetDateTime.now());
			
			Discord.JDA.getUserById(discordId).openPrivateChannel()
			.flatMap(channel -> channel.sendMessage(eb2.build()))
			.queue();
		}
		
		pd.sendMessage(ChatColor.GREEN + "Report successfully sent to staff!");
		return true;
		
	}
}
