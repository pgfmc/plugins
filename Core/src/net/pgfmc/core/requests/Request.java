package net.pgfmc.core.requests;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.ConfirmInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Abstract class to be extended by other classes :)
 * 
 * Implementor must define 
 * 
 * 
 * @author CrimsonDart
 *
 */
public final class Request implements Buttonable {
	
	// FIELDS
	
	public final PlayerData asker;
	public final PlayerData target;
	
	transient boolean isEnded = false;
	public final RequestType parent;
	
	/**
	 * Base Constructor for Requests.
	 * Automatically adds the constructed request to an internal static HashMap.
	 * @param asker The Player sending the request.
	 * @param target The Player receiving the request.
	 * @param time The amount of time (in ticks) until the request expires.
	 */
	protected Request(PlayerData asker, PlayerData target, RequestType parent) {
		this.asker = asker;
		this.target = target;
		this.parent = parent;
	}
	
	public void end(EndBehavior eB) {
		parent.requests.remove(this);
		parent.endRequest(this, eB);
	}
	
	public Butto toAction() {
		Request r = this;
		return (p, e) -> {
			
			ConfirmInventory conf = new ConfirmInventory("Accept " + parent.name + " Request from " + r.asker.getDisplayName() + "?", "Accept", "Reject") {

				@Override
				protected void confirmAction(Player p, InventoryClickEvent e) {
					r.end(EndBehavior.ACCEPT);
				}
				
				@Override
				protected void cancelAction(Player p, InventoryClickEvent e) {
					r.end(EndBehavior.DENIED);
				}
			};
			
			p.openInventory(conf.getInventory());
		};
	}
	
	public String getType() {
		return parent.name;
	}
	
	@Override
	public ItemStack toItem() {
		return parent.toItem();
	}
	
	protected ConfigurationSection toConfigSec() {
		
		MemoryConfiguration mem = new MemoryConfiguration();
		
		mem.set("name", parent.name);
		mem.set("asker", asker.getUniqueId().toString());
		mem.set("target", target.getUniqueId().toString());
		
		return mem;
	}
}
