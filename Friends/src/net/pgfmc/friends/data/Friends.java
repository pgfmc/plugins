package net.pgfmc.friends.data;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Mixins;
import net.pgfmc.friends.Main;

/**
 * Manages friends. Use this class' static methods to access friends, NOT PlayerData.
 * The friends are stored in a {@code HashMap<PlayerData friend, Relation r>}.
 * Friends are stored using yml functionality from Bukkit.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class Friends {
	
	public enum Relation {
		SELF,
		NONE,
		FRIEND,
		FAVORITE;
		
		public static boolean isFriend(Relation r) {
			return (r == FAVORITE || r == FRIEND);
		}
		
		public boolean isFriend() {
			return (this == FAVORITE || this == FRIEND);
		}
	}
	
	/**
	 * Stores data for friends functionality
	 * @param p1 Player 1; 
	 * @param r12 the relationship between p1 and p2 from p1's point of view
	 * @param p2 Player 2;
	 * @param r21 the relationship between p1 and p2 from p2's point of view
	 */
	public static void setRelation(PlayerData p1, Relation r12, PlayerData p2, Relation r21) {
		
		if (p1 == p2) return; // if <player> and <friend> are equal
		if (p1 == null) return;
		if (p2 == null) return;
		
		getFriendsMap(p1).put(p2, r12);
		getFriendsMap(p2).put(p1, r21);
		
		save();
	}
	
	public static void setRelation(PlayerData POV, PlayerData friend, Relation relate) {
		if (POV != null && friend != null && relate != null) {
			getFriendsMap(POV).put(friend, relate);
			save();
		}
	}
	
	public static Relation getRelation(PlayerData POV, PlayerData friend) {
		if (POV.toString().equals(friend.toString())) {
			return Relation.SELF;
		}
		HashMap<PlayerData, Relation> list = POV.getData("friends");
		
		Relation r = list.get(friend);
		if (r == null) {
			r = Relation.NONE;
		}
		return r;
	}
	
	public static HashMap<PlayerData, Relation> getFriendsMap(PlayerData pd) {
		return pd.getData("friends");
	}
	
	/**
	 * 
	 * Saves all friends to database.yml
	 * 
	 * for each playerdata, it stores a list of all other playerdatas with their statuses.
	 * NONE and SELF statuses are not stored
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void save() {
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		
		for (PlayerData pd: PlayerData.getPlayerDataSet()) {
			
			ConfigurationSection conf = verifyConf(verifyConf(database, "friends"), pd.getUniqueId().toString());
			
			for (Entry<PlayerData, Relation> val : ((HashMap<PlayerData, Relation>) pd.getData("friends")).entrySet()) {
				
				if (val.getValue() == Relation.NONE || val.getValue() == Relation.SELF) {
					conf.set(val.getKey().getUniqueId().toString(), null);
					continue;
				}
				
				conf.set(val.getKey().getUniqueId().toString(), val.getValue().toString());
			}
		}
		Mixins.saveDatabase(database, Main.databasePath);
	}
	
	private static ConfigurationSection verifyConf(ConfigurationSection conf, String path) {
		ConfigurationSection out = conf.getConfigurationSection(path);
		
		if (out == null) {
			out = conf.createSection(path);
		}
		
		return out;
	}
	
	/**
	 * loads all friends
	 */
	public static void load() {
		
		FileConfiguration database = Mixins.getDatabase(Main.databasePath);
		ConfigurationSection friends = verifyConf(database, "friends");
		
		for (PlayerData pd : PlayerData.getPlayerDataSet()) {
			ConfigurationSection config = friends.getConfigurationSection(pd.getUniqueId().toString());
			if (config == null) {
				return;
			}
			
			config.getKeys(false).stream()
			.forEach(x-> {
				setRelation(pd, PlayerData.getPlayerData(UUID.fromString(x)), Relation.valueOf(config.getString(x)));
			});
		}
	}
}