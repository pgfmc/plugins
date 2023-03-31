package net.pgfmc.core.api.playerdata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.files.Mixins;
import net.pgfmc.core.util.roles.PGFRole;

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
	private static final HashMap<String, PlayerData> instances = new HashMap<String, PlayerData>();

	private HashMap<String, Object> data = new HashMap<String, Object>();
	private Set<String> tags = new HashSet<>();
	protected List<String> queue = new LinkedList<String>();
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	public PlayerData(OfflinePlayer p) {
		super(p);
		
		if (from(p) != null) return;
		
		PlayerDataManager.pdInit.stream().forEach(consoomer -> consoomer.accept(this));
		
		instances.put(p.getUniqueId().toString(), this);
		
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
		for (String uid : instances.keySet()) {
			if (id.toString().equals(uid)) {
				return instances.get(uid);
			}
		}
		return null;
	}
	
	/**
	 * Deprecated; Use UUID
	 * Gets a PlayerData from a player's real name.s
	 * @param name The player's real name
	 * @return PlayerData, null if no matches
	 */
	@Deprecated
	public static PlayerData from(String name)
	{
		Set<PlayerData> nameMatches = PlayerData.getPlayerDataSet(pd -> pd.getName().toLowerCase().equals(name.toLowerCase()));
		
		if (nameMatches == null || nameMatches.isEmpty() || (PlayerData) nameMatches.toArray()[0] == null) return null;
		
		return (PlayerData) nameMatches.toArray()[0];
	}
	
	public static PlayerData fromDiscordId(String discordUserId)
	{
		for (PlayerData pd : getPlayerDataSet()) {
			if (discordUserId.equals(pd.getData("Discord"))) {
				return pd;
			}
		}
		return null;
		
	}
	
	// getters and setters
	
	public String getRankedName() {
		return  getRole().getColor()
				+ ((getRole().compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "")
				+ Optional.ofNullable(getData("nick")).orElse(getName());
	}
	
	public String getDisplayName()
	{
		if (!hasPermission("pgf.cmd.donator.nick")) return getName();
		
		return ChatColor.stripColor((String) Optional.ofNullable(getData("nick"))
													.orElse(getName()));
		
	}
	
	public PGFRole getRole() {
		return (PGFRole) Optional.ofNullable(getData("role")).orElse(PGFRole.MEMBER);
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
			addTag("debug");
		} else {
			removeTag("debug");
		}
	}
	
	public static void sendDebug(String message) {
		for (PlayerData pd : getPlayerDataSet((pd -> {
						return pd.hasTag("debug");
					}))) {
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
		
		private String data;
		
		Queueable(String n) {
			data = n;
		}
		
		/**
		 * adds this data to the queue
		 */
		public void queue() {
			queue.add(data);
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
	 * @return a player's associated FileConfiguration from database.yml.
	 */
	public Object loadFromFile(String path) {
		return Mixins.getDatabase(CoreMain.plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml").get(path);
	}
	
	public FileConfiguration loadFile() {
		return Mixins.getDatabase(CoreMain.plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 * @return 
	 */
	public <T> void saveToFile(String path, T payload) {

		FileConfiguration database = Mixins.getDatabase(CoreMain.plugin.getDataFolder().getAbsolutePath() + File.separator + "playerdata" + File.separator + getUniqueId().toString() + ".yml");
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
		
		for (String s : instances.keySet()) {
			PlayerData pd = instances.get(s);
			if (predicate.test(pd)) {
				set.add(pd);
			}
		}
		return set;
	}
}
