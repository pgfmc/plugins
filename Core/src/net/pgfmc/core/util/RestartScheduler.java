package net.pgfmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartScheduler extends BukkitRunnable {
	
	private int secondsElapsed = 0;
	
	@Override
	public void run()
	{
		switch (secondsElapsed) {
		
		case 0:
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Scheduled restart in 10 minutes.");
			break;
		case 60 * 5:
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Scheduled restart in 5 minutes.");
			break;
		case 60 * 9:
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Scheduled restart in 60 seconds.");
			break;
		case (60 * 9) + 50:
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Scheduled restart in 10 seconds."
									+ ChatColor.RED + "\n" + "This won't take long!");
			break;
		case (60 * 9) + 57:
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Scheduled restart in 3 seconds."
									+ ChatColor.RED + "\n" + "Be back soon!");
			break;
		case 60 * 10:
			Bukkit.shutdown();
			break;
		default:
			break;
			
		}
		
		secondsElapsed += 1;
		
	}

}
