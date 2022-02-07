package net.pgfmc.duel;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.duel.general.AttackEventHandler;
import net.pgfmc.duel.general.DuelEvents;

public class Main  extends JavaPlugin {
	
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new DuelEvents(), this);
		getServer().getPluginManager().registerEvents(new AttackEventHandler(), this);
	}
}
