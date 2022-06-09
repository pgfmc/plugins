package net.pgfmc.masterbook.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teleport.home.SetHome;

public class HomeInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		// For homes
		if (pd.getData("tempHomeLocation") == null) return;
		
		SetHome.setHome(pd, e.getMessage(), pd.getData("tempHomeLocation"));
		pd.setData("tempHomeLocation", null);
		
		e.setCancelled(true);
	}
}
