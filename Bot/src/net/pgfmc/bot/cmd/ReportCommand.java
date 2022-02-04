package net.pgfmc.bot.cmd;

import java.time.OffsetDateTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.Discord;

public class ReportCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		String m = args[0];
		Player a = (Player) sender;
		
		if(m.length() == 0) {
			a.sendMessage("Please provide a message.");
		} else {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Discord.red);
			eb.setAuthor(a.getName(), null, "https://crafatar.com/avatars/" + a.getUniqueId());
			eb.setTitle("Message received!");
			eb.setDescription(m + " - From" + a.getName());
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build());
			return true;
		}
		return true;
	}
}
