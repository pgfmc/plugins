package net.pgfmc.core.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Interface used to be implemented by classes that use commonly used methods, to save space.
 * @author CrimsonDart
 *
 */
public interface Mixins {
	
	/**
	 * gets a file at the set directory.
	 * Automatically creates a new file if it doesnt exist.
	 * @param dir The directory of the file being loaded.
	 * @return The file at the directory.
	 */
	static File getFile(String dir) {
		File file = new File(dir);
		
		if (!file.exists()) {
			Bukkit.getLogger().warning("Attempting to create file at " + dir);
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
				Bukkit.getLogger().warning("File at " + dir + " couldnt be created!");
				return null;
				
			}
			Bukkit.getLogger().warning("File created!");
			return file;
		}
		
		return file;
	}
	
	static FileConfiguration getDatabase(String dir) {
		return YamlConfiguration.loadConfiguration(getFile(dir));
	}
	
	static FileConfiguration getDatabase(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
	
	static void saveDatabase(FileConfiguration database, String dir) {
		try {
			database.save(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
