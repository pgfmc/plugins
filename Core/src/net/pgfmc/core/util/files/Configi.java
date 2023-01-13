package net.pgfmc.core.util.files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Configi {
	
	private static Map<Configi, File> configs = new HashMap<>();
	
	public Configi()
	{
		configs.put(this, null);
	}
	
	public abstract void reload();
	public abstract void enable();
	public abstract void disable();
	
	public static final void reloadConfigify()
	{
		configs.forEach((configi, file) -> {
			configi.reload();
			Bukkit.getLogger().info(file.getName() + " is reloaded (" + configi.getClass().getSimpleName() + ")");
		});
		
	}
	
	public static final void enableConfigify()
	{
		configs.forEach((configi, file) -> {
			configi.enable();
			Bukkit.getLogger().info(file.getName() + " is enabled (" + configi.getClass().getSimpleName() + ")");
		});
		
	}
	
	public static final void disableConfigify()
	{
		configs.forEach((configi, file) -> {
			configi.disable();
			Bukkit.getLogger().info(file.getName() + " is disabled (" + configi.getClass().getSimpleName() + ")");
		});
		
	}
	
	public final FileConfiguration getFileConfiguration()
	{
		return Mixins.getDatabase(configs.get(this));
	}
	
	public final void setConfigFile(File file)
	{
		configs.put(this, file);
	}
	
	public final <T> boolean setDefaultValue(String key, T value)
	{
		File file = configs.get(this);
		
		if (file == null) return false;
		
		FileConfiguration config = Mixins.getDatabase(file);
		
		config.options().copyDefaults(true); // Allow default options to be saved to file
		config.addDefault(key, value); // Add a default
		
		Mixins.saveDatabase(config, file.getAbsolutePath());
		
		return true;
	}
	
}
