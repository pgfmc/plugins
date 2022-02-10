package net.pgfmc.bot.listeners.minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerAdvancementDone implements Listener {
	
	@EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent e) throws IOException {
		String logPath = CoreMain.pwd + File.separator + "logs" + File.separator + "latest.log";
		
		List<String> all = Files.readAllLines(Paths.get(logPath), Charset.forName("Cp1252"));
		String adv = all.get(all.size() - 1);
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (adv.contains(" has made the advancement "))
		{
			adv = adv.substring(adv.lastIndexOf(pd.getName()), adv.length());
			adv.replace(pd.getName(), pd.getDisplayName());
			
			Discord.sendMessage(adv).queue();
			Bukkit.getLogger().warning("Advancement Get!"); // DO NOT REMOVE THIS!!!!!!!!!!!!!!!! (IT BREAKS) XXX lol
		}
	}

}
