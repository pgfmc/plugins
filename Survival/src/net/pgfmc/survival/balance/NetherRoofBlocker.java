package net.pgfmc.survival.balance;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.pgfmc.survival.Main;

public class NetherRoofBlocker implements Listener {
	
	@EventHandler
	public void whenOnNetherRoof(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (!(p.getGameMode() == GameMode.SURVIVAL && p.getLocation().getWorld().getEnvironment() == World.Environment.NETHER
				&& e.getTo().getY() >= 127)) return;
		
		if (e.getFrom().getY() >= 127) return;
		
		p.sendMessage(ChatColor.DARK_RED + "Nether roof is banned! If you proceed, you will take damage.");
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!(p.getLocation().getWorld().getEnvironment() == World.Environment.NETHER
						&& p.getLocation().getY() >= 127 || p.getGameMode() != GameMode.SURVIVAL || p.isDead()))
				{
					p.sendMessage(ChatColor.DARK_RED + "Thank you.");
					cancel();
				} else
				{
					p.sendMessage(ChatColor.DARK_RED + "DEALING DAMAGE. NETHER ROOF IS BANNED.");
					p.damage(5.0);
				}
				
			}
		}.runTaskTimer(Main.plugin, 20 * 5, 20);
		
		
		
		
	}
	
	@EventHandler
	public void whenPlaceBlockOnNetherRoof(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!(p.getGameMode() == GameMode.SURVIVAL && p.getLocation().getWorld().getEnvironment() == World.Environment.NETHER
				&& e.getClickedBlock().getY() >= 127 && e.getBlockFace() == BlockFace.DOWN)) return;
		
		p.sendMessage(ChatColor.DARK_RED + "Access to the nether roof is disabled.");
		e.setCancelled(true);
	}

}
