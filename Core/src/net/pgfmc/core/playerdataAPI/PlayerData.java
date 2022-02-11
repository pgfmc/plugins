package net.pgfmc.core.playerdataAPI;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.permissions.Roles;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 * @since 2.0.0
 * @version 4.0.2
 */
public final class PlayerData extends AbstractPlayerData {
	
	// fields
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private static final Set<PlayerData> instances = new HashSet<PlayerData>();
	private static final Set<PlayerData> debug = new HashSet<PlayerData>();
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private Set<String> tags = new HashSet<>();
	protected List<String> queue = new LinkedList<String>();
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	PlayerData(OfflinePlayer p) {
		super(p);
		
		PlayerData pd = from(p);
		if (pd == null) {
			
			
			for (Consumer<PlayerData> consoomer : PlayerDataManager.pdInit) {
				
				consoomer.accept(this);
			}
			
			instances.add(this);
			Bukkit.getLogger().warning(p.getName() + " has been loaded!");
		}
	}
	
	// find PlayerData functions ------------------------------------------
	
	/**
	 * Gets a player's associated PlayerData.
	 * @param p The player.
	 * @return The Player's PlayerData class.
	 */
	public static PlayerData from(OfflinePlayer p) { // gets a player's playerdata.
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
	
	/**
	 * Gets a player's PlayerData class.
	 * @param name The player's Minecraft Username, or Nickname.
	 * @return The player's PlayerData.
	 */
	public static PlayerData from(String name) {
		for (PlayerData uid : instances) {
			name = name.toLowerCase();
			if (uid.getName().toLowerCase().equals(name) || uid.getDisplayNameRaw().toLowerCase().equals(name)) {
				return uid;
			}
		}
		return null;
	}
	
	public static PlayerData fromDiscordID(String discordUserId)
	{
		for (PlayerData uid : instances)
		{
			if (discordUserId.equals(uid.getData("Discord"))) return uid;
		}
		return null;
		
	}
	
	// getters and setters
	
	// Has color, has staff diamond
	public String getRankedName()
	{
		Nick.removeImpostors(this);
		// Nick will be player's name if no permission
		return getRankColor() + Nick.getNick(player);
	}
	
	// No color, has staff diamond
	public String getDisplayName()
	{
		return Nick.removeCodes(getRankedName());
	}
	
	// No color, no staff diamond
	public String getDisplayNameRaw()
	{
		return Nick.removeCodes(getDisplayName()).replaceAll("[^A-Za-z0-9]", "");
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
	
	/**
	 * Gets the Player's Role prefix.
	 * @return The player's role prefix.
	 */
	public String getRankColor() {
		return Roles.getTop(player).getColor();
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
		
		public void save() {
			saveToFile(data, getData(data));
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
		return tags.add(tag);
	}
	
	public boolean removeTag(String tag) {
		return tags.remove(tag);
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
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml").get(path);
	}
	
	public FileConfiguration loadFile() {
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 */
	public void saveToFile(String path, Object payload) {

		FileConfiguration database = Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
		database.set(path, payload);
		//queue.forEach((s, o) -> database.set(s, o));
		//queue.clear();
		Bukkit.getLogger().warning("Queue saved to system!");
		
		Mixins.saveDatabase(database, CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
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