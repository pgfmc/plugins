package net.pgfmc.core.api.request;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.inventory.extra.Buttonable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.event.RequestEndEvent;
import net.pgfmc.core.api.request.event.RequestSendEvent;
import net.pgfmc.core.api.request.inv.RequestListInventory;

/**
 * Abstract class to be extended by other classes :)
 * 
 * Implementor must define 
 * 
 * @author CrimsonDart
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
		new RequestSendEvent(this);
		parent.requests.add(this);
	}
	
	public void end(EndBehavior eB) {
		parent.requests.remove(this);
		parent.endRequest(this, eB);
		new RequestEndEvent(this, eB);
	}
	
	public Butto toAction() {
		Request r = this;
		return (p, e) -> {
			
			ConfirmInventory conf = new ConfirmInventory("Accept " + parent.name + " Request from " + r.asker.getDisplayName() + "?", "§r§aAccept", "§r§cReject") {
				
				@Override
				protected void confirmAction(Player p, InventoryClickEvent e) {
					r.end(EndBehavior.ACCEPT);
					p.closeInventory();
				}
				
				@Override
				protected void cancelAction(Player p, InventoryClickEvent e) {
					r.end(EndBehavior.DENIED);
					p.closeInventory();
				}
			};
			conf.setItem(0, Material.FEATHER).n("§r§cBack");
			conf.setAction(0, (pl, ev) -> {
				RequestListInventory inv = new RequestListInventory(PlayerData.from(p));
				
				p.openInventory(inv.getInventory());
			});
			
			p.openInventory(conf.getInventory());
		};
	}
	
	public String getType() {
		return parent.name;
	}
	
	@Override
	public ItemStack toItem() {
		return parent.toItem(this);
	}
}
