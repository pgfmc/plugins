package net.pgfmc.bot.listeners.minecraft;

import java.time.OffsetDateTime;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.functions.spam.Spam;
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
		PlayerData pd = PlayerData.getPlayerData(p);
		
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
			p.sendMessage("§4Please do not use blacklisted words!");
			e.setCancelled(true);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Discord.RED);
			eb.setAuthor(p.getName(), null, "https://crafatar.com/avatars/" + p.getUniqueId());
			eb.setTitle("Blacklisted word detected! (Minecraft)");
			eb.setDescription("A blacklisted word was detected by " + p.getName() + " in Minecraft.");
			eb.addField("User", p.getName(), false);
			eb.addField("Message", "|| " + msg + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build());
			return;
		}
		
		/*
	    List<String> a = Arrays.asList(msg.substring(msg.indexOf("@")).split("@"));
	    a = a.stream().map(fl -> fl.substring(0, fl.indexOf(" "))).collect(Collectors.toList());
	    Bukkit.getLogger().warning(a);
	    */

		
		e.setFormat(pd.getRankedName() + "§8 -> " + getMessageColor(p.getUniqueId().toString()) + msg);
		
		Discord.sendMessage(pd.getNicknameRaw() + " -> " + msg);
	}
	
	public static String getMessageColor(String sender) {
		
		if (sender.equals(lastSender)) {
			return (altColor) ? "§7" : "§f";
			
		}
		lastSender = sender;
		altColor = !altColor;
		return (altColor) ? "§7" : "§f";
	}
	
}
