package net.pgfmc.survival.cmd.tpa;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.request.EndBehavior;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.SoundEffect;

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
		return new ItemWrapper(Material.ENDER_PEARL).n(ChatColor.LIGHT_PURPLE + "Teleport request from " + r.asker.getRankedName()).gi();
	}

	@Override
	protected boolean sendRequest(Request r) {
		r.asker.sendMessage(ChatColor.GOLD + "Teleport request sent to " + r.target.getRankedName() + ChatColor.GOLD + "!");
		r.target.sendMessage(ChatColor.GOLD + "Incoming Teleport request from " + r.asker.getRankedName() + ChatColor.GOLD + ".");
		r.target.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/tpaccept " + ChatColor.GOLD + "to accept!");
		SoundEffect.PING.play(r.target);
		
		return true;
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		switch(eB) {
		case ACCEPT:
			
			r.asker.sendMessage(ChatColor.GOLD + "Teleporting to " + r.target.getRankedName() + ChatColor.GOLD + " in 5 seconds");
			SoundEffect.PING.play(r.asker);
			r.target.sendMessage(ChatColor.GOLD + "Teleporting "+ r.asker.getRankedName() + ChatColor.GOLD + " here in 5 seconds");
			
			new TimedTeleport(r.asker.getPlayer(), r.target.getPlayer(), 5, 40, true).setAct(v -> {
				r.asker.sendMessage(ChatColor.GREEN + "Poof!");
				SoundEffect.TELEPORT.play(r.asker);
				if (r.target.hasTag("afk")) r.target.removeTag("afk");
			});
			break;
			
		case DENIED:
			r.asker.sendMessage(ChatColor.RED + "Teleport request denied!");
			r.target.sendMessage(ChatColor.RED + "Teleport request denied!");
			break;
		case FORCEEND:
			break;
		case QUIT:
			r.asker.sendMessage(ChatColor.RED + "Teleport request cancelled since " + r.target.getRankedName() + ChatColor.RED + " quit!");
			r.target.sendMessage(ChatColor.RED + "Teleport request cancelled since " + r.asker.getRankedName() + ChatColor.RED + " quit!");
			break;
		case TIMEOUT:
			r.asker.sendMessage(ChatColor.RED + "Teleport request timed out!");
			r.target.sendMessage(ChatColor.RED + "Teleport request timed out!");
			break;
		case REFRESH:
			r.asker.sendMessage(ChatColor.GOLD + "Time refreshed!");
			break;
		}
	}
}
