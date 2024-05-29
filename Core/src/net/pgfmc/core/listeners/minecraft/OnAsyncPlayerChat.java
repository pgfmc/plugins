package net.pgfmc.core.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncPlayerChat implements Listener {
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
	{
		e.setCancelled(true);
	}

}
