package net.pgfmc.bot.functions.spam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.entities.Member;
import net.pgfmc.bot.Main;

public class Punishable {
	
	// Contains UUIDS of Player and User that are in timeout
	private HashMap<String, Punishable> punishables = new HashMap<>();
	
	public Punishable(String id) {
		punishables.put(id, this);
	}
	
	public static Punishable get(Player p) {
		return get(p.getUniqueId().toString());
	}
	
	public static Punishable get(Member m) {
		return get(m.getId());
	}
	
	public static Punishable get(String id) {
		return Optional.ofNullable(punishables.get(id)).orElse(new Punishable(id));
	}
	/**
	 * Get if a player/user is in timeout
	 * 
	 * @param id The user or player's UUID
	 * @return if the player is in timeout
	 */
	public boolean isTimeout(String id) {
		return timeouts.containsKey(id);
	}
	
	public void timeoutRemove(Player p)
	{
		Punishable pun = timeouts.get(p.getUniqueId().toString());
		
		timeouts.remove(p.getUniqueId().toString());
	}
	
	private Punishable timeout(Player p, int seconds)
	{
		if (isTimeout(p.getUniqueId().toString())) {
			timeoutRemove(p);
			timeouts.get(p.getUniqueId().toString()).
		}
		timeouts.add(p.getUniqueId().toString());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				timeouts.remove(p.getUniqueId().toString());
			}
		}, 0);
	}
	
	private Punishable timeout(Player p, String[] args)
	{
		if (!p.isOnline()) {
			Bukkit.getLogger().warning("Player cannot be null in Punishable.timeout!");
			return null;
		}
		
		String msg = buildMessage("timed out", args);
		if (msg == null) return null;
		
		p.sendMessage(ChatColor.RED + msg);
		
		
		
		return this;
	}
	
	/**
	 * Build a punishment message!
	 * Valid units: seconds, minutes, hours, days, years, and the first character of those
	 * 
	 * @param tense The punishment in past tense
	 * @param args <time><unit> <optional: reason> or <time> <unit> <optional: reason>
	 * @return A message similar to this: "You have been [banned] for [time] [unit]: [reason]" or null
	 */
	private String buildMessage(String tense, String[] args)
	{
		if (args == null || args.length == 0) {
			Bukkit.getLogger().warning("Invalid arguments in Punishable.timeout!");
			return null;
		}
		
		String timeStr = args[0].substring(0, args[0].length() - 1).replaceAll("[^0-9]", "");
		String unit = args[0].substring(args[0].length()).replaceAll("[^A-Za-z]", "").toLowerCase();
		String reason = "";
		if (args.length > 1) {
			// timeout time unit reason
			if (args[1].length() == 1) {
				unit = args[1].replaceAll("[^A-Za-z]", "").toLowerCase();
				reason = String.join(" ", args).replaceFirst(timeStr, "").replaceFirst(args[1], "");
			}
			// timeout time+unit reason
			reason = String.join(" ", args).replaceFirst(timeStr, "");
		}
		
		int time;
		try {
			if (timeStr.length() == 0) throw new NumberFormatException();
			time = Integer.valueOf(timeStr);
		} catch (NumberFormatException e) {
			Bukkit.getLogger().warning("Incorrect syntax for time in Punishable.timeout!");
			return null;
		}
		
		HashMap<String, String> units = new HashMap<>(
									Map.of(
									"s", "seconds",
									"m", "minutes",
									"h", "hours",
									"d", "days",
									"y", "years")
									);
		
		if (unit.length() > 1) {
			unit = unit.substring(0, 1);
		} else if (unit.length() == 0) {
			unit = "s";
		}
		
		if (!units.containsKey(unit)) {
			Bukkit.getLogger().warning("Incorrect syntax for unit in Punishable.timeout!");
			return null;
		}
		
		if (reason == null || reason.length() == 0) return "You have been " + tense + " for " + time + " " + units.get(unit) + ".";
		
		return "You have been " + tense + " for " + time + " " + units.get(unit) + ": " + reason;
	}
}
