package net.pgfmc.core;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RestartManager {
	
	@SuppressWarnings("deprecation")
	public RestartManager()
	{
		
		// Purge CoreProtect data of 14 days or older
		try {
		
		} finally {}
		 
		final int minutesUntilTopOfHour = 60 - new Date().getMinutes();
		final int secondsUntilTopOfMinute = 60 - new Date().getSeconds();
		final int ticksUntilTopOfHour = ((minutesUntilTopOfHour * 60) + secondsUntilTopOfMinute) * 20; // Ticks until top of the hour
		
		final int nextHour = (new Date().getHours() == 23) ? (0) : (new Date().getHours() + 1);
		final int hoursUntilRestart = ((((1 - (nextHour / 23)) * 23) + 2) == 25) ? (1) : (((1 - (nextHour / 23)) * 23) + 2);
								// Finds the percent of the day completed, then 1 minus to get percent of
								// day NOT completed, then multiply by day to get units needed to complete
								// the day, then add 2 to get to 1 AM ... Need to account for 00:00 though (12 AM)
		
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.add(11, hoursUntilRestart);
		final Date restartDate = calendarNow.getTime();
		
		Bukkit.getScheduler().runTaskTimer(CoreMain.plugin, new Runnable()
		{
            public void run()
            {
            	// Potential problem with this -> Since it doesn't count with a stored variable, it might skip over a second or send a message twice or whatever
            	long secondsUntilRestart = Duration.between(new Date().toInstant(), restartDate.toInstant()).toSeconds();
            	switch ((int) secondsUntilRestart) {
            		case 60 * 10: // 10 minutes
            			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "10 minutes remain until server restart.");
            			break;
            		case 60 * 5: // 5 minutes
            			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "5 minutes remain until server restart.");
            			break;
            		case 60: // 1 minute
            			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "60 seconds remain until server restart.");
            			break;
            		case 10: // 10 seconds
            			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "10 seconds remain until server restart.");
            			break;
            		case 3: // 3 seconds
            			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Restarting in 3 seconds, be back soon!");
            			break;
                	default:
                		// TODO REMOVE AFTER TEST
                    	Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + String.valueOf((int) secondsUntilRestart));;
                    	break;

            	}
            	
            	if ((int) secondsUntilRestart <= 0)
            	{
            		Bukkit.getLogger().warning("Restarting server");
    				Bukkit.shutdown();
    				
            	}
            	
            }
        }, ticksUntilTopOfHour, 20).getTaskId(); // delay before start, delay before next loop (in game ticks)
		
	}

}
