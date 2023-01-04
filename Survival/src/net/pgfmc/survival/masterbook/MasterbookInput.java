package net.pgfmc.survival.masterbook;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.survival.cmd.home.SetHome;

public class MasterbookInput implements Listener {
	
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
