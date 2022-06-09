package net.pgfmc.masterbook.chat;

import org.bukkit.Location;
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
		
		if (pd.hasTag("nick")) {
			pd.removeTag("nick");
			e.setCancelled(true);
			Nick.setNick(pd.getPlayer(), e.getMessage());
			return;
		}
		
		// For homes
		
		Location loc = pd.getData("tempHomeLocation");
		
		if (pd.getData("tempHomeLocation") != null) {
			pd.setData("tempHomeLocation", null);
			e.setCancelled(true);
			SetHome.setHome(pd, e.getMessage(), loc);
			
		}
	}
}