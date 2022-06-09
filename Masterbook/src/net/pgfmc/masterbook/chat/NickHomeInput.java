package net.pgfmc.masterbook.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teleport.home.SetHome;

public class NickHomeInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (pd.getData("nickTemp") != null) {
			
			e.setCancelled(true);
			Nick.setNick(pd.getPlayer(), e.getMessage());
			pd.setData("nickTemp", null);
			return;
		}
		
		// For homes
		if (pd.getData("tempHomeLocation") != null) {
			
			e.setCancelled(true);
			SetHome.setHome(pd, e.getMessage(), pd.getData("tempHomeLocation"));
			pd.setData("tempHomeLocation", null);
		}
	}
}