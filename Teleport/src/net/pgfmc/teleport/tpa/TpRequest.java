package net.pgfmc.teleport.tpa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;
import net.pgfmc.core.teleportAPI.TimedTeleport;
import net.pgfmc.survival.cmd.Afk;

public class TpRequest extends RequestType {
	
	public static final TpRequest TR = new TpRequest();
	
	private TpRequest() {
		super(20 * 60 * 2, "Teleport");
		
	}
	
	public static final void registerAll() {
		TR.registerDeny("tpdeny");
		TR.registerAccept("tpaccept");
		TR.registerSend("tpa");
	}
	

	@Override
	public ItemStack toItem() {
		return new ItemWrapper(Material.ENDER_PEARL).gi();
	}

	@Override
	protected void requestMessage(Request r, boolean refreshed) {
		if (refreshed) {
			r.asker.sendMessage("Time limit refreshed!");
			r.target.sendMessage("Time limit refreshed!");
			return;
		} 
		r.asker.sendMessage("Teleport request sent to " + r.target.getRankedName() + "!");
		r.target.sendMessage("Incoming Tp request from " + r.asker.getRankedName() + ".");
		r.target.sendMessage("Use /tpaccept to accept!");
		
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		switch(eB) {
		case ACCEPT:
			
			r.asker.sendMessage("§6Teleporting to " + r.target.getRankedName() + " §r§6in 5 seconds");
			r.target.sendMessage("§6Teleporting "+ r.asker.getRankedName() +" §r§6here in 5 seconds");
			
			new TimedTeleport(r.asker.getPlayer(), r.target.getPlayer(), 5, 40, true).setAct(v -> {
				r.asker.sendMessage("§aPoof!");
				r.asker.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
				if (Afk.isAfk(r.asker.getPlayer())) { Afk.toggleAfk(r.asker.getPlayer()); }
			});
			
		case DENIED:
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case TIMEOUT:
			break;
		case REFRESH:
			break;
		}
	}
}
