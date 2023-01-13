package net.pgfmc.core.util.files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Configi {
	
	private static Map<Configi, File> instances = new HashMap<>();
	
	public Configi()
	{
		instances.put(this, null);
	}
	
	public abstract void reload();
	public abstract void enable();
	public abstract void disable();
	
	public static final void reloadConfigify()
	{
		instances.forEach((configi, file) -> {
			configi.reload();
			Bukkit.getLogger().warning("[Configi] " + configi.getClass().getName() + " is reloaded!");
		});
		
	}
	
	public static final void enableConfigify()
	{
		instances.forEach((configi, file) -> {
			configi.enable();
			Bukkit.getLogger().warning("[Configi] " + configi.getClass().getName() + " is enabled!");
		});
		
	}
	
	public static final void disableConfigify()
	{
		instances.forEach((configi, file) -> {
			configi.disable();
			Bukkit.getLogger().warning("[Configi] " + configi.getClass().getName() + " is disabled!");
		});
		
	}
	
	public final FileConfiguration getFileConfiguration()
	{
		return Mixins.getDatabase(instances.get(this));
	}
	
	public final void setConfigFile(File file)
	{
		instances.put(this, file);
	}
	
	public final <T> boolean setDefaultValue(String key, T value)
	{
		File file = instances.get(this);
		
		if (file == null) return false;
		
		FileConfiguration config = Mixins.getDatabase(file);
		
		config.options().copyDefaults(true); // Allow default options to be saved to file
		config.addDefault(key, value); // Add a default
		
		Mixins.saveDatabase(config, file.getAbsolutePath());
		
		return true;
	}
	
}
