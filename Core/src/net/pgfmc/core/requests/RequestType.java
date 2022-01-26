package net.pgfmc.core.requests;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Represents a type of Request.
 * @author CrimsonDart
 *
 */
public abstract class RequestType {
	
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
	
	/**
	 * Constructor for RequestTypes. Defines all required fields for requests.
	 * @param time Controls how long a request will last in ticks until it expires. If below 1, The request will never expire.
	 * @param typeName The name of the type of request. Used in built inventories and Command Feedback.
	 */
	public RequestType(int time, String typeName) {
		this.time = time;
		this.name = typeName;
	}
	
	/**
	 * Used to create Requests.
	 * @param asker The player sending the request.
	 * @param target The player receiving the request.
	 * @return The request generated from the interaction.
	 */
	public Request createRequest(PlayerData asker, PlayerData target) {
		
		Request sub = findRequest(asker, target);
		if (sub != null) {
			sub.end(EndBehavior.REFRESH);
		}
		
		Request r = new Request(asker, target, this);
		
		requests.add(r);
		
		if (time > 0) {
			Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
				if (!r.isEnded) {
					r.end(EndBehavior.TIMEOUT);
				}
			}, time);
		}
		
		requestMessage(r, sub != null);
		
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
	
	public boolean endsOnQuit() {
		return endsOnQuit;
	}
	
	/**
	 * Method ran when a new request is created.
	 * Use to send messages to members of a request when one is created.
	 * @param r The request just created.
	 * @param refreshed Whether or not the request refreshed a past request.	
	 */
	protected abstract void requestMessage(Request r, boolean refreshed);
	
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
	protected abstract ItemStack toItem();
	
}
