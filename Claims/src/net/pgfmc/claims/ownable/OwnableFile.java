package net.pgfmc.claims.ownable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;



/**
 * Manages saving ownables. 
 * 
 * @author CrimsonDart
 * @since 1.1.0	
 * @version 4.0.2
 */
public class OwnableFile {
	
	
	public static void loadContainers() {
		
		File file = new File(Main.getPlugin().getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		if (database != null) {
			int amount = 0;
			for (String key : database.getKeys(false)) {
				
				ConfigurationSection configSec = database.getConfigurationSection(key);
				
				PlayerData pd = PlayerData.from(UUID.fromString(configSec.getString("player")));
				
				
				
				Vector4 vec = Vector4.fromString(key);
				new Claim(pd, vec, null);
				amount++;
			}
			Bukkit.getLogger().warning("Loaded " + amount + " Claim(s).");
		}
		
		//BlockManager.calcLoop(); // stats the calcloop, which loads each player's region into memory.
		
	}
	
	public static void saveContainer(Claim ob, FileConfiguration database) {
		
		if (ob.getLocation().getBlock().getType() != Material.LODESTONE) { 
			return;
		}
		
		String id = ob.getLocation().toString();
		PlayerData player = ob.getPlayer();
		
		// if location is not found, a new one is created.
		ConfigurationSection blocc = database.getConfigurationSection(id);
		if (blocc == null) {
			blocc = database.createSection(id);
		}
		
		blocc.set("player", player.getUniqueId().toString());
		
		database.set(id, blocc);
		
		// saves data.
		
	}
	
	public static void saveContainers() {
		
		File file = new File(Main.getPlugin().getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = new YamlConfiguration();
		
		for (Claim blocke : ClaimsTable.getAllClaims()) {
			saveContainer(blocke, database);
		}
		
		try {
			database.save(file);
			Bukkit.getLogger().warning("Claim locations saved!");
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		// ------------------------------------ end loop
	}
}
