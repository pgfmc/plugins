package net.pgfmc.survival.masterbook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.cmd.donator.Nick;

public class NicknameInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (!pd.hasTag("nick")) return;
		
		pd.removeTag("nick");
		
		Nick.setNick(pd, e.getMessage());
		
		e.setCancelled(true);
		
	}
	
}
