package net.pgfmc.survival.masterbook.inv.home;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.cmd.home.SetHome;

public class HomeInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		Location loc = pd.getData("tempHomeLocation");
		
		if (loc == null) return;
			
		pd.setData("tempHomeLocation", null);
		SetHome.setHome(pd, e.getMessage(), loc);
		
		e.setCancelled(true);
	}
	
}
