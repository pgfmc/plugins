package net.pgfmc.core.api.request;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		for (Request r : RequestType.getInAllRequests(x -> {
			return (x.parent.isPersistent && (x.target.getUniqueId().equals(e.getPlayer().getUniqueId())));
		})) {
			String message = r.parent.getJoinMessage();
			if (message != null) {
				e.getPlayer().sendMessage(message);
			} else {
				Bukkit.getLogger().info(r.getType() + " is a persistent request! Its recommended you use a join message!");
			}
		}
	}
}
