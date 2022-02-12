package net.pgfmc.core.teleportAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.permissions.Permissions;

/**
 * Attempts to protect the player in a undefendable state
 * eg. Traveling between worlds, joining the server, teleporting
 * @author bk
 *
 */
public class SpawnProtect implements Listener {
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		TEMP_PROTECT(e.getPlayer(), 20 * 4);
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e)
	{
		TEMP_PROTECT(e.getPlayer(), 20 * 4);
	}
	
	public static void TEMP_PROTECT(Player p, int ticks)
	{
		p.setInvulnerable(true);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() { @Override public void run() {
			p.setInvulnerable(false);
			
		}}, ticks);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (Permissions.has(e.getPlayer(), "world.modify.break")) return;
		
		e.getPlayer().sendMessage("§cYou do not have permission to do that.");
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (Permissions.has(e.getPlayer(), "world.modify.place")) return;
		
		e.getPlayer().sendMessage("§cYou do not have permission to do that.");
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (Permissions.has(e.getPlayer(), "world.modify.interact")) return;
		
		e.getPlayer().sendMessage("§cYou do not have permission to do that.");
		e.setCancelled(true);
	}

}
