package net.pgfmc.backup;

import org.bukkit.Bukkit;

import net.pgfmc.backup.backup.Backup;
/**
 * manages all the restart stuff.
 */
public class Restart {
	
	public int taskID;
	public int time = 0;
	
	
	
	/**
	 * sends messages to all players before the restart.
	 * 
	 * messages are sent at 10, 5 and 1 minutes, as well as at 10 and 3 seconds.
	 */
	public void init()
	{
		taskID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
		{
            public void run()
            {
            	int timeLeft = 60 * 60 * 24 - time; // 24 hours
            	
            	switch (timeLeft) {
            		case 60 * 10: // 10 minutes
            			Bukkit.broadcastMessage("§d10 minutes remain until server restart.");
            			break;
            		case 60 * 5: // 5 minutes
            			Bukkit.broadcastMessage("§d5 minutes remain until server restart.");
            			break;
            		case 60: // 1 minute
            			Bukkit.broadcastMessage("§d60 seconds remain until server restart.");
            			break;
            		case 10: // 10 seconds
            			Bukkit.broadcastMessage("§d10 seconds remain until server restart.");
            			break;
            		case 3: // 3 seconds
            			Bukkit.broadcastMessage("§d§lRestarting, be back soon!");
            			break;
                	default: break;
            	}
            	if (timeLeft <= 0)
            	{
            		new Backup().backup();
            		commit();
            	}
            	
            	time += 1;
            }
        }, 0, 20).getTaskId(); // delay before start, delay before next loop
	}
	
	/**
	 * This ends the scheduler
	 * Probably not needed, but I don't want to close the server while the scheduler is still running
	 */
	public void commit()
	{
		Bukkit.getScheduler().cancelTask(taskID);
	}

}
