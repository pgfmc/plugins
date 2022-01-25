package net.pgfmc.core.requests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class Request implements Buttonable {
	
	public final PlayerData asker;
	public final PlayerData target;
	
	public final int time;
	private transient boolean isEnded;
	
	static final HashMap<Class<? extends Request>, Set<Request>> requests = new HashMap<>();
	
	protected Request(PlayerData asker, PlayerData target, int time, boolean quittable) {
		this.asker = asker;
		this.target = target;
		this.time = time;
		
		Set<Request> list = requests.get(this.getClass());
		if (list == null) {
			list = new HashSet<Request>();
			list.add(this);
			requests.put(this.getClass(), list);
			
		} else {
			list.add(this);
		}
		
		if (time < 1) return;
		Bukkit.getScheduler().runTaskLater(CoreMain.plugin, x -> {
			if (!isEnded) {
				timeout();
			}
		}, time);
	}
	
	public static Request findRequest(PlayerData asker, PlayerData target, Class<? extends Request> clazz) {
		for (Request r : requests.get(clazz)) {
			if (r.asker == asker && r.target == target) {
				return r;
			}
		}
		return null;
	}
	
	public static Set<Request> findRequests(PlayerData asker, PlayerData target) {
		Set<Request> set = new HashSet<>();
		
		for (Entry<Class<? extends Request>, Set<Request>> entry : requests.entrySet()) {
			for (Request r : entry.getValue()) {
				if (r.asker == asker && r.target == target) {
					set.add(r);
					break;
				}
			}
		}
		if (set.size() == 0) {
			return null;
		} else {
			return set;
		}
	}
	
	public static Set<Request> requestSet() {
		Set<Request> set = new HashSet<>();
		
		for (Entry<Class<? extends Request>, Set<Request>> entry : requests.entrySet()) {
			for (Request r : entry.getValue()) {
				set.add(r);
			}
		}
		return set;
	}
	
	public void accept() {
		requests.get(this.getClass()).remove(this);
		endRequest(EndBehavior.ACCEPT);
	}
	
	public void deny() {
		requests.get(this.getClass()).remove(this);
		endRequest(EndBehavior.DENIED);
	}
	
	public void forceEnd() {
		requests.get(this.getClass()).remove(this);
		endRequest(EndBehavior.FORCEEND);
	}
	
	protected void quit() {
		requests.get(this.getClass()).remove(this);
		endRequest(EndBehavior.QUIT);
	}
	
	protected void timeout() {
		requests.get(this.getClass()).remove(this);
		endRequest(EndBehavior.TIMEOUT);
	}
	
	public Butto toAction() {
		return (p, e) -> {
			
		};
	}
	
	
	protected abstract void endRequest(EndBehavior eB);
	
	
}
