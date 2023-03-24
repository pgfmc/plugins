package net.pgfmc.core.api.teleport;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpawnProtect implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (p.hasPermission("modifyworld.*")) return;
		
		//p.sendMessage("cYou do not have permission to do that.");
		e.setCancelled(true);
	}

}
