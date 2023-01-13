package net.pgfmc.survival.cmd.tpa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.request.EndBehavior;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.ItemWrapper;


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
	public ItemStack toItem(Request r) {
		return new ItemWrapper(Material.ENDER_EYE).n("§dTp here request from " + r.asker.getRankedName()).gi();
	}

	@Override
	protected boolean sendRequest(Request r) {
		r.asker.sendMessage("§6Teleport here request sent to " + r.target.getRankedName() + "§6!");
		r.target.sendMessage("§6Incoming Tph request from " + r.asker.getRankedName() + "§6.");
		r.target.sendMessage("§6Use §b/tpha §6to accept!");
		return true;
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
				if (r.target.hasTag("afk")) r.target.removeTag("afk");
			});
			break;
			
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
			r.asker.sendMessage("§6Time refreshed!");
			break;
		}
	}
	
}
