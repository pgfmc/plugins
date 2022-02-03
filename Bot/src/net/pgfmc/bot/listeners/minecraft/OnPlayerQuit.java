package net.pgfmc.bot.listeners.minecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerQuit implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		e.setQuitMessage("§7[§c-§7]§r " + PlayerData.getPlayerData(p).getRankedName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + PlayerData.getPlayerData(p).getNicknameRaw());
	}

}
