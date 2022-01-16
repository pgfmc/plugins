package net.pgfmc.bot.player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.playerdataAPI.PlayerData;

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
		
		// If list1 has any values with list 2
		// Word blacklist
		if (ProfanityFilter.hasProfanity(msg))
		{
			p.sendMessage("§4Please do not use blacklisted words!");
			e.setCancelled(true);
			return;
		}
		
		PlayerData pd = PlayerData.getPlayerData(p);
		
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
		e.setDeathMessage(e.getDeathMessage().replace(pd.getName(), pd.getRankedName()));
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
