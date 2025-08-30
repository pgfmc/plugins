package net.pgfmc.claims.ownable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.claims.Main;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;



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
				
				String playcer = configSec.getString("placer");
				
				PlayerData pd = (playcer.equals("creative")) ?
						null :
							PlayerData.from(UUID.fromString(playcer));
				
				Set<PlayerData> members = new HashSet<>();
				for (String berri : configSec.getStringList("members")) {
					members.add(PlayerData.from(UUID.fromString(berri)));
				}
				
				Vector4 vec = Vector4.fromString(key);
				Claim claim = new Claim(pd, vec, members);

                claim.doorsLocked = configSec.getBoolean("doors");
                claim.inventoriesLocked = configSec.getBoolean("inventories");
                claim.switchesLocked = configSec.getBoolean("switches");
                claim.monsterKilling = configSec.getBoolean("monsters");
                claim.livestockKilling = configSec.getBoolean("animals");
                claim.explosionsEnabled = configSec.getBoolean("explosions");

                configSec.getStringList("beacons")
                    .stream()
                    .forEach(x -> {
                        claim.beacons.add(Vector4.fromString(x));
                    });

				amount++;
			}

            Set<Claim> claims = ClaimsTable.getAllClaims();

            for (Claim claim : claims) {
                if (!claim.calculated) {
                    claim.calculateNetwork(false);
                }
            }
            for (Claim claim : claims) {
                claim.calculated = false;
            }

			Bukkit.getLogger().info("Loaded " + amount + " Claim(s).");
		}
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
		
		blocc.set("placer", (player == null) ?
				"creative" :
					player.getUniqueId().toString()
				);
		
		blocc.set("members", ob.getMembers()
				.stream().map(x -> {
					return x.getUniqueId().toString();
				}).collect(Collectors.toList()));

		blocc.set("beacons", ob.beacons
				.stream().map(x -> {
					return x.toString();
				}).collect(Collectors.toList()));

        blocc.set("doors", ob.doorsLocked);
        blocc.set("inventories", ob.inventoriesLocked);
        blocc.set("switches", ob.switchesLocked);
        blocc.set("monsters", ob.monsterKilling);
        blocc.set("animals", ob.livestockKilling);
        blocc.set("explosions", ob.explosionsEnabled);
		
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
			Bukkit.getLogger().info("Claim locations saved!");
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		// ------------------------------------ end loop
	}
}
