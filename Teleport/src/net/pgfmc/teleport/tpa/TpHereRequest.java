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


public class TpHereRequest extends RequestType {
	
public static final TpHereRequest TH = new TpHereRequest();
	
	private TpHereRequest() {
		super(20 * 60 * 2, "Teleport here");
	}
	
	public static final void registerAll() {
		TH.registerDeny("tpheredeny");
		TH.registerAccept("tphereaccept");
		TH.registerSend("tphere");
	}
	

	@Override
	public ItemStack toItem() {
		return new ItemWrapper(Material.ENDER_EYE).gi();
	}

	@Override
	protected void requestMessage(Request r, boolean refreshed) {
		if (refreshed) {
			r.asker.sendMessage("§6Time limit refreshed!");
			r.target.sendMessage("§6Time limit refreshed!");
			return;
		} 
		r.asker.sendMessage("§6Teleport here request sent to " + r.target.getRankedName() + "§6!");
		r.target.sendMessage("§6Incoming Tph request from " + r.asker.getRankedName() + "§6.");
		r.target.sendMessage("§6Use §b/tpha §6to accept!");
		
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		switch(eB) {
		case ACCEPT:
			
			r.target.sendMessage("§6Teleporting to " + r.asker.getRankedName() + " §r§6in 5 seconds");
			r.asker.sendMessage("§6Teleporting "+ r.target.getRankedName() +" §r§6here in 5 seconds");
			
			new TimedTeleport(r.target.getPlayer(), r.asker.getPlayer(), 5, 40, true).setAct(v -> {
				r.target.sendMessage("§aPoof!");
				r.target.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
				if (Afk.isAfk(r.target.getPlayer())) { Afk.toggleAfk(r.target.getPlayer()); }
			});
			
		case DENIED:
			r.asker.sendMessage("§cTph request denied!");
			r.target.sendMessage("§cTph request denied!");
			break;
		case FORCEEND:
			break;
		case QUIT:
			r.asker.sendMessage("§cTph request cancelled since " + r.target.getRankedName() + " §cquit!");
			r.target.sendMessage("§cTph request cancelled since " + r.asker.getRankedName() + " §cquit!");
			break;
		case TIMEOUT:
			r.asker.sendMessage("§cTph request timed out!");
			r.target.sendMessage("§cTph request timed out!");
			break;
		case REFRESH:
			break;
		}
	}
	
}
