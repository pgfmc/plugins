package net.pgfmc.bot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		e.setJoinMessage("§7[§a+§7]§r " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getDisplayNameRaw());
	}

}
