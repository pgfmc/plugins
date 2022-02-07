package net.pgfmc.core.file.config;

import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.file.Configify;

@Deprecated
public class ConfigFile extends Configify {
	
	private final HashSet<Entry<?>> entries = new HashSet<>();
	private final JavaPlugin plugin;
	
	private static boolean isEnabled = false;
	
	public ConfigFile(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	private void load() {
		ConfigurationSection conf = plugin.getConfig();
		for (Entry<?> entry : entries) {
			if (!conf.isConfigurationSection(entry.key)) {
				
				
				
				
				
			}
			
			
			
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
		
		for (Entry<?> entry : entries) {
			if (entry.key.equals(key) &&
					data.getClass().equals(entry.getValue().getClass())) {
				//Entry<T> entri = (Entry<T>) entry;
				entry.setValue(data);
				
				
			}
			
			
			
		}
		
		
	}
	
	
	
	

	@Override
	public final void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void enable() {
		isEnabled = true;
		load();
		
	}

	@Override
	public final void disable() {
		// TODO Auto-generated method stub
		
	}

}
