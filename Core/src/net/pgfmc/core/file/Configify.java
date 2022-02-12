package net.pgfmc.core.file;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Configify {
	
	private static Set<Configify> configs = new HashSet<>();
	private File file;
	
	public Configify(File file)
	{
		configs.add(this);
		this.file = file;
		
		Mixins.getFile(file.getAbsolutePath());
	}
	
	public Configify()
	{
		configs.add(this);
	}
	
	/**
	 * Reload method, set variables and stuff when called (from file)
	 * Override this lol
	 */
	public abstract void reload();
	public abstract void enable();
	public abstract void disable();
	
	public final FileConfiguration getConfig()
	{
		if (file == null) return null;
		
		return Mixins.getDatabase(file);
	}
	
	/**
	 * Reloads all the configs
	 */
	public final static void reloadConfigify()
	{
		configs.forEach(c -> c.reload());
		Bukkit.getLogger().info("(" + configs.size() + ") Configify reloaded");
	}
	
	/**
	 * Enables all the configs
	 */
	public final static void enableConfigify()
	{
		configs.forEach(c -> c.enable());
		Bukkit.getLogger().info("(" + configs.size() + ") Configify enabled");
	}
	
	/**
	 * Disables all the configs
	 */
	public final static void disableConfigify()
	{
		configs.forEach(c -> c.disable());
		Bukkit.getLogger().info("(" + configs.size() + ") Configify disabled");
	}
	
	/**
	 * Set a default value, saves to the file
	 * 
	 * @param <T> Type parameter
	 * 
	 * @param key Key/path
	 * @param value Default value for key
	 * @return The object
	 */
	@SuppressWarnings("unchecked")
	public final <T> T setDefaultValue(String key, T value)
	{
		if (file == null) return null;
		
		FileConfiguration conf = getConfig();
		// Allow default options to be saved to file
		conf.options().copyDefaults(true);
		// Add a default
		conf.addDefault(key, value);
		
		try {
			conf.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (T) conf.getObject(key, value.getClass());
	}
	
	public void save(FileConfiguration config)
	{
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
