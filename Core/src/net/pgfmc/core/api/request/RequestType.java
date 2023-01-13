package net.pgfmc.core.api.request;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.cmd.RequestAcceptCommand;
import net.pgfmc.core.api.request.cmd.RequestDenyCommand;
import net.pgfmc.core.api.request.cmd.RequestSendCommand;
import net.pgfmc.core.util.files.Configi;
import net.pgfmc.core.util.files.Mixins;

/**
 * Represents a type of Request.
 * @author CrimsonDart
 *
 */
public abstract class RequestType extends Configi {
	
	// FIELDS
	
	/**
	 * Holds all requests in a type of request.
	 */
	Set<Request> requests = new HashSet<>();
	/**
	 * If this value is true, then the request will be ended when a member of the request logs off the server.
	 */
	protected boolean endsOnQuit = true;
	/**
	 * If true, then requests of this type will persist across restarts. If {@code endsOnQuit} is true, or if there is a timer on the request, this value wont matter.
	 */
	protected boolean isPersistent = false;
	/**
	 * This field controls how long Requests will last, in ticks.
	 */
	public final int time;
	/**
	 * The name of the Type of request. Used in built inventories, and Feedback for commands.
	 */
	public final String name;
	
	private String joinMessage;
	
	private static Set<RequestType> instances = new HashSet<>();
	
	/**
	 * Constructor for RequestTypes. Defines all required fields for requests.
	 * @param time Controls how long a request will last in ticks until it expires. If below 1, The request will never expire.
	 * @param typeName The name of the type of request. Used in built inventories and Command Feedback.
	 */
	public RequestType(int time, String typeName) {
		this.time = time;
		this.name = typeName;
		instances.add(this);
	}
	
	/**
	 * Used to create Requests.
	 * @param asker The player sending the request.
	 * @param target The player receiving the request.
	 * @return The request generated from the interaction.
	 */
	public Request createRequest(PlayerData asker, PlayerData target) {
		
		Request sub = findRequest(asker, target);
		if (sub != null && !isPersistent) {
			sub.end(EndBehavior.REFRESH);
		}
		
		Request r = new Request(asker, target, this);
		
		if (time > 0) {
			Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
				if (!r.isEnded) {
					r.end(EndBehavior.TIMEOUT);
				}
			}, time);
		}
		
		if (sub == null) {
			if (!sendRequest(r)) {
				r.end(EndBehavior.FORCEEND);
			}
		}
		
		return r;
	}
	
	/**
	 * Gets the request that fits the given parameters.
	 * @param asker The player that sent the request.
	 * @param target The player that received the request.
	 * @return The request that fits the parameters; if no request was found, returns null.
	 */
	public Request findRequest(PlayerData asker, PlayerData target) {
		
		for (Request r : requests) {
			if (r.asker == asker && r.target == target) {
				return r;
			}
		}
		return null;
	}
	
	public Set<Request> findRequests(PlayerData target) {
		Set<Request> set = new HashSet<>();
		
		for (Request r : requests) {
			if (r.target == target) {
				set.add(r);
			}
		}
		return set;
	}
	
	public static Set<Request> getAllRequests() {
		Set<Request> set = new HashSet<>();
		
		for (RequestType sr : instances) {
			for (Request r : sr.requests) {
				set.add(r);
			}
		}
		return set;
	}
	
	public static Set<Request> getInAllRequests(Predicate<? super Request> predicate) {
		Set<Request> set = new HashSet<>();
		
		int i = 0;
		for (RequestType sr : instances) {
			
			for (Request r : sr.requests) {
				if (predicate.test(r)) {
					set.add(r);
					System.out.println("added");
				}
				i++;
				System.out.print(" r" + i);
			}
		}
		return set;
	}
	
	public boolean endsOnQuit() {
		return endsOnQuit;
	}
	
	public void registerDeny(String label) {
		new RequestDenyCommand(label, this);
	}
	
	public void registerSend(String label) {
		new RequestSendCommand(label, this);
	}
	
	public void registerAccept(String label) {
		new RequestAcceptCommand(label, this);
	}
	
	@Override
	public void enable() {
		
		FileConfiguration cs = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "requests.yml");
		
		ConfigurationSection configsec = cs.getConfigurationSection(name);
		
		for (String key : configsec.getKeys(false)) {
			PlayerData aska = PlayerData.from(UUID.fromString(key));
			PlayerData targe = PlayerData.from(UUID.fromString(configsec.getString(key)));
			
			new Request(aska, targe, this);
		}
	}
	
	@Override
	public void disable() {
		FileConfiguration cs = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "requests.yml");
		
		ConfigurationSection configsec = cs.createSection(name);
		
		for (Request r : requests) {
			
			if (!isPersistent) continue;
			
			configsec.set(r.asker.getUniqueId().toString(), r.target.getUniqueId().toString());
		}
		
		cs.set(name, configsec);
		Mixins.saveDatabase(cs, CoreMain.plugin.getDataFolder() + File.separator + "requests.yml");
		
		requests.clear();
	}
	
	@Override
	public void reload() {
		disable();
		enable();
	}
	
	protected void setJoinMessage(String messa) {
		
		if (joinMessage == null) {
			joinMessage = messa;
		}
	}
	
	public String getJoinMessage() {
		return joinMessage;
	}
	
	/**
	 * Method ran when a new request is created.
	 * Use to send messages to members of a request when one is created.
	 * @param r The request just created.
	 * @return Returns true if the request isn't cancelled, false if it is cancelled.
	 */
	protected abstract boolean sendRequest(Request r);
	
	/**
	 * Only implement this method, NEVER RUN IT YOURSELF, instead use {@code request.end(EndBehavior)} to end a Request.
	 * @param r
	 * @param eB
	 */
	protected abstract void endRequest(Request r, EndBehavior eB);
	/**
	 * Used to represent the request in an inventory. Try using ItemWrapper for more easy construction of ItemStacks :)
	 * @return The ItemStack used to represent the Request in an inventory. 
	 */
	protected abstract ItemStack toItem(Request request);
	
}
