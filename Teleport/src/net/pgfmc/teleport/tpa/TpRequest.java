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
	public ItemStack toItem(Request r) {
		return new ItemWrapper(Material.ENDER_PEARL).n("§dTp request from " + r.asker.getRankedName()).gi();
	}

	@Override
	protected boolean sendRequest(Request r) {
		r.asker.sendMessage("§6Teleport request sent to " + r.target.getRankedName() + "§6!");
		r.target.sendMessage("§6Incoming Tp request from " + r.asker.getRankedName() + "§6.");
		r.target.sendMessage("§6Use §b/tpaccept §6to accept!");
		return true;
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
			break;
			
		case DENIED:
			r.asker.sendMessage("§cTpa request denied!");
			r.target.sendMessage("§cTpa request denied!");
			break;
		case FORCEEND:
			break;
		case QUIT:
			r.asker.sendMessage("§cTpa request cancelled since " + r.target.getRankedName() + " §cquit!");
			r.target.sendMessage("§cTpa request cancelled since " + r.asker.getRankedName() + " §cquit!");
			break;
		case TIMEOUT:
			r.asker.sendMessage("§cTpa request timed out!");
			r.target.sendMessage("§cTpa request timed out!");
			break;
		case REFRESH:
			r.asker.sendMessage("§6Time refreshed!");
			break;
		}
	}
}
