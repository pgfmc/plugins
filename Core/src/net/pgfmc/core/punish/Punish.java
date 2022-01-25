package net.pgfmc.core.punish;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.Configify;
import net.pgfmc.core.util.Mixins;

public class Punish extends Configify {
	
	// id -> (epoch time?, time, unit, enum)
	private static HashMap<String, List<String>> toFile = new HashMap<>();
	
	public Punish() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "punish.yml"));
	}
	
	public enum Punishment {
		TIMEOUT;
		
		private HashMap<String, Integer> punished = new HashMap<>();
		
		public boolean add(String id, int time, char unit)
		{
			time = toSeconds(time, unit);
			
			if (time * 20 <= 0) {
				Bukkit.getLogger().warning("Invalid int for punishment time!");
				return false;
			}
			
			remove(id);
			punished.put(id, addTimer(this, id, time));
			return true;
		}
		
		public boolean remove(String id)
		{
			toFile.remove(id);
			if (punished.get(id) == null) return false;
			
			
			Bukkit.getScheduler().cancelTask(punished.get(id));
			return true;
		}
		
		public void serialize(String id, int time, char unit) {
			toFile.put(id, Arrays.asList(String.valueOf(new Date().getTime()), String.valueOf(time), String.valueOf(unit), this.name()));
		}
		
		private void deserialize(String id, List<String> data)
		{
			// Get how much time left is in the punishment
			// ms
			float oldTime = Long.valueOf(data.get(0));
			float nowTime = new Date().getTime();
			// ms -> s
			int deltaTime = Integer.valueOf(String.valueOf(nowTime - oldTime)) / 1000;
			int time = Math.max(1, toSeconds(Integer.valueOf(data.get(0)), data.get(1).charAt(0)) - deltaTime);
			
			add(id, time, 's');
		}
	}
	
	
	
	private static int addTimer(Punishment p, String id, int time)
	{
		return Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
			@Override
			public void run() {
				p.punished.remove(id);
			}
		}, time * 20);
	}
	
	private static int toSeconds(int time, char unit)
	{
		switch (unit) {
		case 's':
			break;
		case 'm':
			time *= 60f;
			break;
		case 'h':
			time *= 60f * 60f;
			break;
		case 'd':
			time *= 60f * 60f * 24f;
			break;
		case 'y':
			time *= 60f * 60f * 24f * 365f;
			break;
		default: break;
		}
		
		return time;
	}

	@Override
	public void reload() {
		@SuppressWarnings("unchecked")
		HashMap<String, List<String>> datas = (HashMap<String, List<String>>) getConfig().getMapList("punishments").stream().findFirst().get();
		
		datas.forEach((id, data) -> {
			Punishment p = Punishment.valueOf(data.get(3));
			p.deserialize(id, data);
		});
	}

	@Override
	public void enable() {
		reload();		
	}

	@Override
	public void disable() {
		getConfig().set("punishments", Arrays.asList(toFile));
	}
}
