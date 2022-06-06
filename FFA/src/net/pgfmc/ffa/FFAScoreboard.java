package net.pgfmc.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class FFAScoreboard {
	
	final private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	
	public void updateScoreboard()
	{
        
        for (Player p : Bukkit.getOnlinePlayers())
        {
        	PlayerData pd = PlayerData.from(p);
        	
			Objective o = scoreboard.registerNewObjective("ffa-scoreboard", "dummy", "");
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
			
			double kills = pd.getData("kills");
        	double deaths = pd.getData("deaths");
        	
        	double kdr = kills / Math.max(deaths, 1); // Can't divide by zero
			
			Score kd = o.getScore(ChatColor.GOLD + String.valueOf(kdr));
			kd.setScore(0);
			
			p.setScoreboard(scoreboard);
        }
        
	}
	
}
