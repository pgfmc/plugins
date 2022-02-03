package net.pgfmc.masterbook.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class NicknameInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd.getData("nickTemp") == null) return;
		
		Nick.setNick(pd.getPlayer(), e.getMessage());
		pd.setData("nickTemp", null);
		
		e.setCancelled(true);
	}
}