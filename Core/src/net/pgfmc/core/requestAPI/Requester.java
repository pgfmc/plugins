package net.pgfmc.core.requestAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * 
 * A helper class for creating Requests between two online players
 * 
 * This has the ability to:
 *   <> Execute a RequestAction apon an accepted Request
 *   <> Accept a Request
 *   <> Deny a Request
 *   <> Automatically deny all Requests related to a player who has left the server
 *   <> Can define the Request name/type that appears in response messages
 *   <> Can define expiry
 *   <> Can define the action apon an accepted Request
 *   <> Forcibly expire a Request *Use Reason.Force
 *   <> Holds all Requests tied to a player until a Request that they're related to
 *      is expired of Reason: Deny, Accept (Automatically denies all related Requests, some exceptions)
 * @author bk
 *
 */
public abstract class Requester implements Listener {
	
	/*
	 * Holds all active Requests
	 */
	public List<Request> allRequests = new ArrayList<Request>();
	
	public static List<Request> ALLREQUESTS = new LinkedList<>();
	
	/*
	 * Request name/type that appears in response messages
	 * Defaults to "" (blank)
	 * 
	 * Example if "TPA" -> The TPA request has been denied
	 * Example if blank -> The request has been denied
	 */
	protected String name = "";
	
	/*
	 * The expiry of the Request or when the Request should expire
	 * Measured in seconds
	 * Defaults to 120 seconds
	 */
	protected int expiry = 120;
	
	/*
	 * The action that should take place
	 * apon an accepted Request
	 * 
	 * Lambda function that has initiate and target
	 * See the interface RequestAction for details
	 */
	protected RequestAction action = RequestAction.nothing;
	
	/*
	 * Reason for an expire
	 * All can be self defined
	 *    Self defined means a class that extends this one
	 *    may use these Reasons to force a Request to instantaneously die
	 *    I made up the term self defined so don't take it too literal pls
	 *    
	 *    * = Is used in auto (user probably shouldn't use these)
	 * 
	 * 
	 * <> Duplicate*
	 *    If the initiate and target match another Request with the same initiate and target
	 * <> Quit*
	 *    If a player of a Request quit
	 * <> Deny
	 *    If a target denies a Request
	 * <> Accept
	 *    If a target accepts a Request
	 * <> Timeout*
	 *    If the Request met it's expiry
	 * <> Force
	 *    Usually used by a class extending this one
	 *    If the Request needs to expire forcibly
	 */
	public enum Reason {
		Duplicate,
		Quit,
		Deny,
		Accept,
		Timeout,
		Force
	};
	
	/**
	 * The interface that does something
	 *    idk, action that should take place
	 *    apon an accepted Request
	 *    
	 * Has two parameters
	 * <> Player a
	 *    initiate
	 * <> Player b
	 *    target
	 *    
	 * Use this to make this class useful lol
	 */
	@FunctionalInterface
	public interface RequestAction
	{
		/**
		 * 
		 * Action that should take place
		 * apon an accepted Request
		 * 
		 * @param a initiate
		 * @param b target
		 * @return true if successful
		 * 
		 */
		public boolean act(PlayerData a, PlayerData b);
		
		public RequestAction nothing = (a, b) -> { return true; };
	}
	
	/**
	 * 
	 * Super variablesss
	 * Give the Request a name/type, an expiry, and an action
	 * 
	 * @param name The name/type of Request that is used for player feedback messages
	 * @param expiry In seconds, when the Request should expire, -1 for expire on server restart
	 * @param customMessage true if you don't want Requester sending messages to the players, can leave blank
	 * @param action The RequestAction to execute apon an accepted Request, can be null or blank for nothing
	 * 
	 */
	public Requester(String name, int expiry, RequestAction action)
	{
		this.name = name;
		this.expiry = expiry;
		this.action = Optional.ofNullable(action).orElse(RequestAction.nothing);
		
	}
	public Requester(String name, int expiry)
	{
		this.name = name;
		this.expiry = expiry;
	}
	
	/**
	 * 
	 * Used for finding all Requests with matching initiate and target
	 * Technically there should only be max one match
	 * 
	 * Requests are in order from oldest to newest
	 * 
	 * @param initiate
	 * @param target
	 * @return A list of all matching Requests or null if none
	 * 
	 */
	public Request findRequest(Player initiate, Player target)
	{
		return findRequest(initiate.getUniqueId(), target.getUniqueId());
	}
	
	public Request findRequest(UUID init, UUID target) {
		List<Request> requests = allRequests
				.stream()
				.filter(request -> request.isSame(init, target))
				.collect(Collectors.toList());
				
		if (requests.isEmpty())
		{
			return null;
		}
		
		return requests.get(0);
	}
	/**
	 * 
	 * Used for finding all Requests with matching target
	 * Requests are in order from oldest to newest
	 * 
	 * @param target The player That will be 
	 * @return The Request that has the <target> as the <target>
	 * 
	 */
	public Request findRequest(Player target)
	{	
		List<Request> requests = allRequests
				.stream()
				.filter(request -> request.getTarget().getUniqueId().equals(target.getUniqueId()))
				.collect(Collectors.toList());
		
		if (requests.isEmpty())
		{
			return null;
		}
		
		return requests.get(requests.size() - 1); // latest Request
	}
	
	/**
	 * Expires all relations that contain the specified UUID <uuid>.
	 * @param uuid The Player's UUID that expires all requests.
	 * @param reason The Reason for expiration.
	 */
	public void expireRelations(UUID uuid, Reason reason) {
		
		allRequests.stream().forEach(x -> {
				if (x.getInitiate().getUniqueId().equals(uuid)){
						x.expireNow(reason);
						
				} else if (x.getTarget().getUniqueId().equals(uuid)){
					x.expireNow(reason);
				
				}
			}
		);
		
		System.out.println("All relations of player "  + Bukkit.getOfflinePlayer(uuid).getName() + " Expired due to " + reason.toString());
	}
	
	/**
	 * 
	 * Create a Request with two online players
	 * Uses user defined super expiry and action
	 * 
	 * @param initiate The sender
	 * @param target The target
	 * @return The created Request, null is both aren't online
	 * 
	 */
	public Request createRequest(Player initiate, Player target, RequestAction action)
	{
		// checks for duplicates
		// opt will contain duplicate Requests when done
		Optional<Request> opt = allRequests.stream().reduce((a, x) -> {
			if (!initiate.getUniqueId().equals(x.getInitiate().getUniqueId()) && 
					!target.getUniqueId().equals(x.getTarget().getUniqueId()) &&
					initiate.isOnline() && target.isOnline()) {
					// ---------
					
					return x;
				}
			return null;
		});
		opt.stream().forEach(request -> request.expireNow(Reason.Duplicate)); // probably only contains 1 Request
		
		
		
		Request r = new Request(PlayerData.getPlayerData(initiate), PlayerData.getPlayerData(target), this);
		
		if (expiry != -1)
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() { @Override public void run()
			{
				r.expireNow(Reason.Timeout);
			}}, 20 * expiry);
		}
		
		return r;
	}
	
	public Request createRequest(Player initiate, Player target) {
		return createRequest(initiate, target, action);
	}
	
	/**
	 * 
	 * Accept a Request with given Request
	 * 
	 * @param request The Request to accept
	 * @return true if successful
	 * 
	 */
	public boolean accept(Request request)
	{
		if (request == null)
		{
			return false;
		}
		
		if (!request.expireNow(Reason.Accept)) { return false; } // Old Request/ already expired
		
		return request.act();
		
	}
	
	public boolean accept(Player initiate, Player target)
	{
		return accept(findRequest(initiate, target));
	}
	
	public boolean accept(Player target) {
		return accept(findRequest(target));
	}
	
	
	
	/**
	 * 
	 * Denies a Request with given Request
	 * 
	 * @param request
	 * @return true if successful
	 * 
	 */
	public boolean deny(Request request) {
		if (request == null)
		{
			return false;
		}
		return request.expireNow(Reason.Deny);
	}
	
	public boolean deny(Player initiate, Player target) {
		return deny(findRequest(initiate, target));
	}
	
	public boolean deny(Player target) {
		return deny(findRequest(target));
	}
	
	/**
	 * Expires all Requests that relate to the quit player
	 * Expires with Reason.Quit
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		expireRelations(e.getPlayer().getUniqueId(), Reason.Quit);
	}
	
	/**
	 * Returns the expiry, the time in seconds that the request will last.
	 * @return the expiry, the time in seconds that the request will last.
	 */
	public int getExpiry() {
		return expiry;
	}
	
	/**
	 * Returns the type of Request.
	 * @return the type of Request.
	 */
	public String getName() {
		return name;
	}
	
}
