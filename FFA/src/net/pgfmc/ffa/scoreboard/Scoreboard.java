package net.pgfmc.ffa.scoreboard;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class Scoreboard {
	
	private static HashMap<UUID, Integer> killScore = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> deathScore = new HashMap<UUID, Integer>();
	
	public static void playerSlainPlayer(UUID victor, UUID loser)
	{
		int victorKills = Optional.ofNullable(killScore.get(victor)).orElse(0);
		int loserDeaths = Optional.ofNullable(deathScore.get(loser)).orElse(0);
		
		killScore.put(victor, victorKills++);
		deathScore.put(loser, loserDeaths++);
	}
	
	public static double getKD(UUID player)
	{
		int kills = Optional.ofNullable(killScore.get(player)).orElse(0);
		int deaths = Optional.ofNullable(deathScore.get(player)).orElse(1); // 1 here because you can't divide by 0
		
		return (double) kills/deaths;
	}
}
