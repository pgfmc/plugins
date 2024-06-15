package net.pgfmc.core.api.playerdata;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.PGFRole;
import net.pgfmc.core.util.files.Mixins;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 * @since 2.0.0
 * @version 4.0.2
 */
public final class PlayerData extends PlayerDataExtra {
	
	// fields
    //


    private enum Send {
        DISCORD,
        ROLE,
        NICKNAME
    }

    private enum Save {
        HOMES,
        TAGS
    }
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private static final Set<PlayerData> instances = new HashSet<PlayerData>();

    private String nickname = "";
    private PGFRole role = PGFRole.MEMBER;
    private String discord = "";
    private HashMap<String, Location> homes = new HashMap<String, Location>();

    private EnumSet<Send> sendQueue = EnumSet.noneOf(Send.class);
    private EnumSet<Save> saveQueue = EnumSet.noneOf(Save.class);
	
	protected Set<String> tags = new HashSet<>();
	
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
        name = name + ChatColor.RESET;
		
		return name;
	}
	
	public String getDisplayName()
	{
		// Returns their regular Minecraft user name if the player isn't a donator
		if (!hasPermission("net.pgfmc.core.nick")) return getName();
		
		// Returns their nickname, but with no color codes or symbols (or their regular user name if no nickname)
		return ChatColor.stripColor((String) Optional.ofNullable(this.nickname)
													.orElse(getName()));
		
	}

    public void addHome(String name, Location location) {
        this.homes.put(name, location);
        this.saveQueue.add(Save.HOMES);
    }

    public Location getHome(String name) {
        for (String nameCheck : homes.keySet()) {
            if (name.equals(nameCheck)) {
                return homes.get(nameCheck);
            }
        }
        return null;
    }

    public void removeHome(String name) {
        for (String nameCheck : homes.keySet()) {
            if (name.equals(nameCheck)) {
                homes.remove(nameCheck);
                this.saveQueue.add(Save.HOMES);
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        nickname = name;
        this.sendQueue.add(Send.NICKNAME);
    }

    public String getDiscordId() {
        return discord;
    }

    public void setDiscordId(String id) {
        discord = id;
        sendQueue.add(Send.DISCORD);
    }

    public PGFRole getRole() {
        return role;
    }

    public void setRole(PGFRole r) {
        role = r;
        sendQueue.add(Send.ROLE);
    }
	
	public boolean equals(PlayerData o) {
		return o.getUniqueId().toString().equals(getUniqueId().toString());
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
