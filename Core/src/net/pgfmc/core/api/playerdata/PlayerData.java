package net.pgfmc.core.api.playerdata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import com.moandjiezana.toml.TomlWriter;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.PGFRole;
import net.pgfmc.core.util.files.Mixins;
import net.pgfmc.core.util.proxy.PluginMessageType;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 * @since 2.0.0
 * @version 4.0.2
 */
public final class PlayerData extends PlayerDataExtra {
	
	// fields
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private static final Set<PlayerData> instances = new HashSet<PlayerData>();
	private static final Set<PlayerData> debug = new HashSet<PlayerData>();
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	protected Set<String> tags = new HashSet<>();
	protected List<String> queue = new LinkedList<String>();
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	public PlayerData(OfflinePlayer p) {
		super(p);
		
		if (from(p) != null) return;
		
		getPlayerDataFile().getStringList("tags").stream().forEach(tag -> addTag(tag));
		PlayerDataManager.pdInit.stream().forEach(consoomer -> consoomer.accept(this));
		
		instances.add(this);
		
		Bukkit.getLogger().warning("PlayerData loaded for " + p.getName() + "!");
		
	}
	
	// find PlayerData functions ------------------------------------------
	
	/**
	 * Gets a player's associated PlayerData.
	 * @param p The player.
	 * @return The Player's PlayerData class.
	 */
	public static PlayerData from(OfflinePlayer p) { // gets a player's playerdata.
		if (p == null) return null;
		
		return from(p.getUniqueId());
	}
	
	/**
	 * Gets a player's PlayerData class.
	 * @param id The player's UUID.
	 * @return The player's PlayerData.
	 */
	public static PlayerData from(UUID id) {
		Objects.requireNonNull(id);
		for (PlayerData uid : instances) {
			if (id.toString().equals(uid.getUniqueId().toString())) {
				return uid;
			}
		}
		return null;
	}
	
	// getters and setters
	
	// This is the pvp swords icon for when a player has pvp enabled
	// It will show up in their name (similar to the staff diamond icon)
	// Used in getRankedName()
	private final String pvpSwordsIcon = new String(Character.toChars(0x2694));
	
	public String getRankedName() {
		if (!hasPermission("net.pgfmc.core.nick")) return getRole().getColor() + getName();
		
		// This will use the player's nickname, or, if they don't have a nickname, their regular Minecraft user name
		String name = getDisplayName();
		
		// If the player's role is STAFF or higher
		if (getRole().compareTo(PGFRole.STAFF) <= 0)
		{
			// Add the staff diamond icon to the beginning of the name
			name = getRole().getColor() + PGFRole.STAFF_DIAMOND + name;
		} else
		{
			name = getRole().getColor() + name;
		}
		
		// If the player has pvp enabled
		if (hasTag("pvp"))
		{
			// Add the pvp swords icon to the ending of the name
			name = name + ChatColor.GRAY + " " + pvpSwordsIcon;
		}
		
		return name + ChatColor.RESET;
	}
	
	public String getDisplayName()
	{
		// Returns their regular Minecraft user name if the player isn't a donator
		if (!hasPermission("net.pgfmc.core.nick")) return getName();
		
		// Returns their nickname, but with no color codes or symbols (or their regular user name if no nickname)
		return ChatColor.stripColor((String) Optional.ofNullable(getData("nickname"))
													.orElse(getName()));
		
	}
	
	public PGFRole getRole() {
		// Returns the player's role, or MEMBER if no role
		final String roleName = getData("role");
		
		if (roleName == null) return PGFRole.MEMBER;
		
		final PGFRole role = PGFRole.get(roleName);
		
		if (role == null) return PGFRole.MEMBER;
		
		return role;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OfflinePlayer) {
			return (((OfflinePlayer) o).getUniqueId().toString().equals(getUniqueId().toString()));
		} else if (o instanceof PlayerData) {
			return (((PlayerData) o).getUniqueId().toString().equals(getUniqueId().toString()));
		}
		return false;
	}
	
	public boolean equals(PlayerData o) {
		return o.getUniqueId().toString().equals(getUniqueId().toString());
	}
	
	public void setDebug(boolean d) {
		if (d) {
			debug.add(this);
		} else {
			debug.remove(this);
		}
	}
	
	public boolean isDebug() {
		return (debug.contains(this));
	}
	
	public static void sendDebug(String message) {
		for (PlayerData pd : debug) {
			pd.sendMessage(message);
		}
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * Returns a Queueable, which can be <.queue>ed to save to file.
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 */
	public Queueable setData(String n, Object d) { // sets a playerData point
		data.put(n, d);
		return new Queueable(n);
	}
	
	public static Queueable setData(OfflinePlayer player, String n, Object d) {
		return from(player).setData(n, d);
	}
	
	/**
	 * Queueable class.
	 * Create a new instance to create a new Queueable instance, then use the {@code .queue()} method to add to the queue.
	 * 
	 * @author CrimsonDart
	 *
	 */
	public class Queueable {
		
		private final String key;
		
		Queueable(final String key) {
			this.key = key;
		}
		

		/**
		 * Queue this mapping to be saved in the playerdata file.
		 * 
		 * @return This Queueable
		 */
		public final Queueable queue() {
			queue.add(key);
			return this;
		}
		
		/**
		 * Send this mapping to the proxy.
		 * 
		 * @return This Queueable
		 */
		public final Queueable send() {
			final TomlWriter writer = new TomlWriter();
			Object value = getData(key);
			
			if (value == null)
			{
				value = "";
			}
			
			final String data = writer.write(Map.of(key, value));
			
			PluginMessageType.PLAYER_DATA_SEND.send(CoreMain.plugin.getServer(), getUniqueId().toString(), data);
			
			return this;
		}
		
	}
	
	/**
	 * Gets a player's data. must be Cast to the Correct Class to be used, however.
	 * @param n Name of the data.
	 * @return The data called by "n". Must be Cast to be used.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String n) { // gets a playerData point
		return (T) data.get(n);
	}
	
	/**
	 * Static method to get a player's data.
	 * @param <T> The type of the data being returned.
	 * @param player An OfflinePlayer whose data is wanted.
	 * @param data The name of the data wanted.
	 * @return The data associated with input "data".
	 */
	public static <T> T getData(OfflinePlayer player, String data) {
		return from(player).getData(data);
	}
	
	public HashMap<String, Object> getAllData() {
		return data;
	}
	
	public boolean addTag(String tag) {
		
		if (tags.add(tag))
		{
			saveToFile("tags", new ArrayList<String>(tags));
			
			return true;
		}
		
		return false;
	}
	
	public boolean removeTag(String tag) {
		
		if (tags.remove(tag))
		{
			saveToFile("tags", new ArrayList<String>(tags));
			
			return true;
		}
		
		return false;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	/**
	 * @author bk
	 * @return a player's associated FileConfiguration
	 */
	public FileConfiguration getPlayerDataFile() {
		return Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml");
	}
	
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 * @return 
	 */
	public <T> void saveToFile(String path, T payload) {

		FileConfiguration database = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml");
		database.set(path, payload);
		
		Mixins.saveDatabase(database, CoreMain.plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Returns a set containing all PlayerDatas.
	 * @return A set containing all PlayerDatas.
	 */
	public static Set<PlayerData> getPlayerDataSet() {
		return getPlayerDataSet(x -> true);
	}
	
	/**
	 * Returns a set of all PlayerDatas that match the given Predicate.
	 * @param predicate The condition that a PlayerData must match to be returned.
	 * @return The set of all PlayerDatas that the predicate returns true for.
	 */
	public static Set<PlayerData> getPlayerDataSet(Predicate<PlayerData> predicate) {
		
		Set<PlayerData> set = new HashSet<>();
		
		for (PlayerData pd : instances) {
			if (predicate.test(pd)) {
				set.add(pd);
			}
		}
		return set;
	}
	
}