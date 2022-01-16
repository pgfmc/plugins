package net.pgfmc.core.requestAPI;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requestAPI.Requester.Reason;
import net.pgfmc.core.requestAPI.Requester.RequestAction;

/**
 * 
 * @author bk
 * 
 * This is the Request itself
 * Holds the initiate, target, expiry, and action
 * 
 * Automatically expires Requests at expiry
 *
 */
public class Request {
	/*
	 * The initiate is the one who sends/initiates the Request (to a target)
	 */
	protected PlayerData initiate;
	
	/*
	 * The target is the one who receives the Request (from an initiate)
	 */
	protected PlayerData target;
	
	protected boolean active = true;
	
	protected RequestAction action = RequestAction.nothing;
	
	protected RequestMessage message = RequestMessage.def;
	
	@FunctionalInterface
	public interface RequestMessage
	{
		public void send(PlayerData initiate, PlayerData target, Reason r, Request req);
		
		public RequestMessage def = (initiate, target, r, req) -> {
			switch (r) {
			case Accept:
				initiate.sendMessage("§aAccepted " + req.parent.getName() + " request -> §n" + target.getRankedName());
				target.sendMessage("§aAccepted " + req.parent.getName() + " request <- " + initiate.getRankedName());
				break;
			
			case Deny:
				initiate.sendMessage("§cDenied " + req.parent.getName() + " request -> §n" + target.getRankedName());
				target.sendMessage("§cDenied " + req.parent.getName() + " request <- §n" + initiate.getRankedName());
				break;
			
			case Duplicate:
			
				break;
			
			case Quit:
				try {
					initiate.sendMessage("§cQuit " + req.parent.getName() + " request -> §n" + target.getRankedName()); } finally {}
				try {
					target.sendMessage("§cQuit " + req.parent.getName() + " request <- §n" + initiate.getRankedName()); } finally {}
				break;
			
			case Timeout:
				try {
					initiate.sendMessage("§cTimeout " + req.parent.getName() + " request -> §n" + target.getRankedName()); } finally {}
				try {
					target.sendMessage("§cTimeout " + req.parent.getName() + " request <- §n" + initiate.getRankedName()); } finally {}
				break;
			
			case Force:
				try {
					initiate.sendMessage("§cStopped " + req.parent.getName() + " request -> §n" + target.getRankedName()); } finally {}
				try {
					target.sendMessage("§cStopped " + req.parent.getName() + " request <- §n" + initiate.getRankedName()); } finally {}
				break;
			}
		};
	}
	
	/**
	 * The parent requester object;
	 * the requester object dictates how the request will act.
	 */
	protected Requester parent;
	
	/**
	 * 
	 * This creates the Request
	 * 
	 * It checks for duplicated Requests
	 * It automatically times out at expiry
	 * 
	 * @param initiate sender
	 * @param target target
	 * @param action The RequestAction to run when a Request is accepted
	 * 
	 */
	protected Request(PlayerData initiate, PlayerData target, Requester parent) {
		this.initiate = initiate;
		this.target = target;
		this.parent = parent;
		action = parent.action;
		
		if (message.equals(RequestMessage.def))
		{
			initiate.sendMessage("§6Outgoing " + parent.getName() + " request -> §n" + target.getRankedName());
			target.sendMessage("§6Incoming " + parent.getName() + " request <- §n" + initiate.getRankedName());
		}
		
		Requester.ALLREQUESTS.add(this);
		parent.allRequests.add(this);
	}		
	
	/**
	 * Activates and finalizes the request.
	 * @return Whether or not the action was successful (idk actually)
	 */
	public boolean act() {
		return action.act(initiate, target);
	}
	
	public void setMessage(RequestMessage message)
	{
		this.message = message;
	}
	
	public PlayerData getInitiate()
	{
		return initiate;
	}
	
	public PlayerData getTarget()
	{
		return target;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public static boolean isActive(Request req)
	{
		return req.isActive();
	}
	
	/**
	 * All Requests will hit this method
	 * For a Request to expire does not mean it didn't execute successfully
	 * 
	 * Expiring a Request removes it from allRequests
	 * Expiring a Request will also determine what action to take with Reason (hah get it)
	 * 
	 * @param reason Reason to give an expired Request, Reason.Force most common (?)
	 * @return true if successfully expired, false if unsuccessful or already expired
	 */
	public boolean expireNow(Reason reason)
	{
		if (parent.allRequests.remove(this)) // If a change was made i.e. it removed the Request
		{
			Requester.ALLREQUESTS.remove(this);
			active = false;
			message.send(initiate, target, reason, this);
			
			return true;
		}
		active = false;
		Requester.ALLREQUESTS.remove(this);
		return false;
	}
	
	/**
	 * 
	 * Tests if this Request have the same initiates and targets
	 * Used for distinguishing duplicates
	 * 
	 * @param initiate A new initiate
	 * @param target A new target
	 * @return true if same
	 * 
	 */
	public boolean isSame(Player initiate, Player target) {
		return (this.initiate.getUniqueId().equals(initiate.getUniqueId()) && this.target.getUniqueId().equals(target.getUniqueId()));
	}
	
	public boolean isSame(UUID initiate, UUID target) {
		return (this.initiate.getUniqueId().equals(initiate) && this.target.getUniqueId().equals(target));
	}
	
	public Requester getParent() {
		return parent;
	}
	
}
