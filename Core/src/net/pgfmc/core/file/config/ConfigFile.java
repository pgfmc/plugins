package net.pgfmc.core.file.config;

import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.file.Configify;

public class ConfigFile extends Configify {
	
	private final HashSet<Entry<?>> entries = new HashSet<>();
	private final JavaPlugin plugin;
	
	private boolean isEnabled = false;
	
	public ConfigFile(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	private void load() {
		ConfigurationSection conf = plugin.getConfig();
		for (Entry<?> entry : entries) {
			if ()
			
			
			
		}
	}
	
	public <T> Entry<T> pushEntry(String name, T data) {
		if (!isEnabled) {
			System.out.println("Config has already been enabled!");
			return null;
		}
		
		Entry<T> entry = new Entry<T>(name);
		entry.setDefault(data);
		
		entries.add(entry);
		return entry;
	}
	
	public <T> void setData(String key, T data) {
		
		
		
		
	}
	
	
	
	

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enable() {
		isEnabled = true;
		load();
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

}
