package net.pgfmc.claims.ownable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.Ownable.Lock;
import net.pgfmc.claims.ownable.block.BlockManager;
import net.pgfmc.claims.ownable.block.OwnableBlock;
import net.pgfmc.claims.ownable.entities.OwnableEntity;
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
				
				PlayerData pd = PlayerData.getPlayerData(UUID.fromString(configSec.getString("player")));
				
				Lock lock;
				
				if (configSec.getString("Lock") == null) {
					lock = Lock.FRIENDS_ONLY;
				} else {
					lock = Lock.valueOf(configSec.getString("Lock"));
				}
				
				Vector4 vec = Vector4.fromString(key);
				new OwnableBlock(pd, vec, lock);
				amount++;
			}
			Bukkit.getLogger().warning("Loaded " + amount + " Ownables.");
		}
		
		// Entity Containers
		
		file = new File(Main.getPlugin().getDataFolder() + File.separator + "EntityContainers.yml"); // Creates a File object
		database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		if (database != null) {
			for (String key : database.getKeys(false)) {
				
				ConfigurationSection configSec = database.getConfigurationSection(key);
				
				UUID uuid = UUID.fromString(key);
				
				PlayerData pd = PlayerData.getPlayerData(UUID.fromString(configSec.getString("player")));
				
				Lock lock = Lock.valueOf(configSec.getString("Lock"));
				
				new OwnableEntity(pd, lock, uuid);
			}
		}
		
		//BlockManager.calcLoop(); // stats the calcloop, which loads each player's region into memory.
		
	}
	
	public static void saveContainer(OwnableBlock ob, FileConfiguration database) {
		
		if (!BlockManager.isOwnable(ob.getLocation().getBlock().getType())) { 
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
		blocc.set("Lock", ob.getLock().toString());
		
		database.set(id, blocc);
		
		// saves data.
		
	}
	
	public static void saveContainers() {
		
		File file = new File(Main.getPlugin().getDataFolder() + File.separator + "BlockContainers.yml"); // Creates a File object
		FileConfiguration database = new YamlConfiguration();
		
		for (OwnableBlock blocke : BlockManager.getContainers()) { // for all BlockContainers and beacons.
			
			saveContainer(blocke, database);
		}
		
		for (OwnableBlock blocke : BlockManager.getClaims()) {
			saveContainer(blocke, database);
		}
		
		try {
			database.save(file);
			Bukkit.getLogger().warning("Container location saved!");
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		// ------------------------------------ end loop
		
		// Entity Containers
		
		file = new File(Main.getPlugin().getDataFolder() + File.separator + "EntityContainers.yml"); // Creates a File object
		database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
		
		for (UUID entity : OwnableEntity.getContainers().keySet()) { // for all BlockContainers and beacons.
			
			OwnableEntity ent = OwnableEntity.getContainer(entity);
			PlayerData player = ent.getPlayer();
			
			// if location is not found, a new one is created.
			ConfigurationSection blocc = database.getConfigurationSection(entity.toString());
			if (blocc == null) {
				blocc = database.createSection(entity.toString());
			}
			
			blocc.set("player", player.getUniqueId().toString());
			blocc.set("Lock", ent.getLock().toString());
			database.set(entity.toString(), blocc);
			
			// saves data.
			try {
				database.save(file);
				Bukkit.getLogger().warning("Container location saved!");
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
}
