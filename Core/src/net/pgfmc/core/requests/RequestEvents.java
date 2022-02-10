package net.pgfmc.core.requests;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class RequestEvents implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) { 
		
		for (Request r : RequestType.getInAllRequests(x -> {
			return (x.parent.endsOnQuit && (x.asker.getUniqueId().equals(e.getPlayer().getUniqueId()) || x.target.getUniqueId().equals(e.getPlayer().getUniqueId())));
		})) {
			
			r.end(EndBehavior.QUIT);
		}
	}
}
