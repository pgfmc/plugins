package net.pgfmc.bot.minecraft.listeners;

import java.time.OffsetDateTime;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.discord.Spam;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.punish.Punish;

/**
 * Makes all the chat colorful :)
 * @author bk (basically CrimsonDart now)
 *
 */
public class OnAsyncPlayerChat implements Listener {

	private static boolean altColor = false;
	private static String lastSender = "null lol"; // lol (not null so no possible errors lol
	
	String pwd = Main.plugin.getServer().getWorldContainer().getAbsolutePath();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e)
	{
		if (e.isCancelled()) return;
		
		String msg = e.getMessage();
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.from(p);
		
		if (Punish.isMute(pd))
		{
			p.sendMessage(ChatColor.RED + "You are currently muted.");
			e.setCancelled(true);
			return;
		} else
		{
			Spam.check(p);
		}
		
		if (msg.length() > 95)
		{
			p.sendMessage(ChatColor.RED + "Your message is too long (max 95 characters).");
			msg = msg.substring(0, 95) + "(...)";
		}
		
		// If list1 has any values with list 2
		// Word blacklist
		if (ProfanityFilter.hasProfanity(msg))
		{
			p.sendMessage(ChatColor.RED + "Please do not use blacklisted words!");
			e.setCancelled(true);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Colors.RED.getColor());
			eb.setAuthor(p.getName(), null, "https://crafatar.com/avatars/" + p.getUniqueId());
			eb.setTitle("Blacklisted word detected! (Minecraft)");
			eb.setDescription("A blacklisted word was detected by " + p.getName() + " in Minecraft.");
			eb.addField("User", p.getName(), false);
			eb.addField("Message", "|| " + msg + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			return;
		}
		
		/*
	    List<String> a = Arrays.asList(msg.substring(msg.indexOf("@")).split("@"));
	    a = a.stream().map(fl -> fl.substring(0, fl.indexOf(" "))).collect(Collectors.toList());
	    Bukkit.getLogger().warning(a);
	    */
		
		e.setFormat(pd.getRankedName() + ChatColor.DARK_GRAY + " -> " + getMessageColor(p.getUniqueId().toString()) + "%2$s"); // %2$s means 2nd argument (the chat message), %1$s would be the player's display name
		
		Discord.sendMessage(pd.getDisplayName() + " -> " + msg).queue();
	}
	
	public static ChatColor getMessageColor(String sender) {
		
		if (sender.equals(lastSender)) {
			return (altColor) ? ChatColor.GRAY : ChatColor.WHITE;
			
		}
		lastSender = sender;
		altColor = !altColor;
		return (altColor) ? ChatColor.GRAY : ChatColor.WHITE;
	}
	
}
