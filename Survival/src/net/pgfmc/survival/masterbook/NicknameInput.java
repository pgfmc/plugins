package net.pgfmc.survival.masterbook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.api.playerdata.PlayerData;

public class NicknameInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (!pd.hasTag("nick")) return;
		
		pd.removeTag("nick");
		e.setCancelled(true);
		
		pd.getPlayer().performCommand("nick " + e.getMessage());
		
	}
	
}
