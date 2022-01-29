package net.pgfmc.bot.player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
public class ChatEvents implements Listener {
	
	private static boolean altColor = false;
	private static String lastSender = "null lol"; // lol (not null so no possible errors lol
	
	String pwd = Main.plugin.getServer().getWorldContainer().getAbsolutePath();
	
	@EventHandler
	public void onMessage(AsyncPlayerChatEvent e)
		{
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
				eb.setColor(Discord.red);
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
		    System.out.println(a);
		    */

			
			e.setFormat(pd.getRankedName() + "§8 -> " + getMessageColor(p.getUniqueId().toString()) + msg);
			
			Discord.sendMessage(pd.getNicknameRaw() + " -> " + msg);
		}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		e.setJoinMessage("§7[§a+§7]§r " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getNicknameRaw());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		e.setQuitMessage("§7[§c-§7]§r " + PlayerData.getPlayerData(p).getRankedName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + PlayerData.getPlayerData(p).getNicknameRaw());
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		PlayerData pd = PlayerData.getPlayerData(e.getEntity());
		
		e.setDeathMessage(e.getDeathMessage().replace(pd.getName(), pd.getNicknameRaw()));
		Discord.sendMessage("<:DEATH:907865162558636072> " + e.getDeathMessage());
		e.setDeathMessage(e.getDeathMessage().replace(pd.getNicknameRaw(), pd.getRankedName()));
	}
	
	@EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent e) throws IOException {
		String logPath = pwd + File.separator + "logs" + File.separator + "latest.log";
		
		List<String> all = Files.readAllLines(Paths.get(logPath), Charset.forName("Cp1252"));
		String adv = all.get(all.size() - 1);
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (adv.contains(" has made the advancement "))
		{
			adv = adv.substring(adv.lastIndexOf(pd.getName()), adv.length());
			adv.replace(pd.getName(), pd.getNicknameRaw());
			
			Discord.sendMessage(adv);
			System.out.println("Advancement Get!"); // DO NOT REMOVE THIS!!!!!!!!!!!!!!!! (IT BREAKS) XXX lol
		}
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
