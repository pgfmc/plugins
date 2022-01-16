package net.pgfmc.core.configify;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.Mixins;

public abstract class Configify {
	
	private static LinkedList<Configify> configs = new LinkedList<>();
	private File file;
	
	public Configify(File file)
	{
		configs.add(this);
		this.file = file;
	}
	
	/**
	 * Reload method, set variables and stuff when called (from file)
	 * Override this lol
	 */
	public abstract void reload();
	
	public final FileConfiguration getConfig()
	{
		return Mixins.getDatabase(file);
	}
	
	/**
	 * Reload all the configs that extend this class
	 */
	public final static void reloadConfigs()
	{
		configs.stream().forEach(c -> c.reload());
		System.out.println("(" + configs.size() + ") Configify reloaded");
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
	protected final <T> T setDefaultValue(String key, T value)
	{
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
}
