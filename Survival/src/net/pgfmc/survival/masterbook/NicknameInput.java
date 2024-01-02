package net.pgfmc.survival.masterbook;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

public class NicknameInput implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (!pd.hasTag("nick")) return;
		
		pd.removeTag("nick");
		
		// Scheduler needed because the nick command contains
		// an advancement grant, which requires it to be ran
		// synchronous to the server thread.
		//
		// This event is asynchronous
		//
		// This scheduler will run the contained code on the
		// next server tick (synchronous to the main thread)
		Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {

			@Override
			public void run() {
				e.getPlayer().performCommand("nick " + e.getMessage());
				
			}
			
		});
		
		e.setCancelled(true);
		
	}
	
}
