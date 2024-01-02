package net.pgfmc.survival.cmd.tpa;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.PGFAdvancement;
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
		return new ItemWrapper(Material.ENDER_EYE).n(ChatColor.LIGHT_PURPLE + "Tp here request from " + r.asker.getRankedName()).gi();
	}

	@Override
	protected boolean sendRequest(Request r) {
		r.asker.sendMessage(ChatColor.GOLD + "Teleport here request sent to " + r.target.getRankedName() + ChatColor.GOLD + "!");
		r.target.sendMessage(ChatColor.GOLD + "Incoming Tph request from " + r.asker.getRankedName() + ChatColor.GOLD + ".");
		r.target.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/tpha " + ChatColor.GOLD + "to accept!");
		
		// Grants advancement
		PGFAdvancement.TP_PLEASE.grantToPlayer(r.asker.getPlayer());
		
		return true;
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		switch(eB) {
		case ACCEPT:
			
			r.target.sendMessage(ChatColor.GOLD + "Teleporting to " + r.asker.getRankedName() + ChatColor.RESET + ChatColor.GOLD + " 5 seconds");
			r.asker.sendMessage(ChatColor.GOLD + "Teleporting "+ r.target.getRankedName() + ChatColor.RESET + ChatColor.GOLD + " here in 5 seconds");
			
			new TimedTeleport(r.target.getPlayer(), r.asker.getPlayer(), 5, 40, true).setAct(v -> {
				r.target.sendMessage(ChatColor.GREEN + "Poof!");
				r.target.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
				if (r.target.hasTag("afk")) r.target.removeTag("afk");
			});
			break;
			
		case DENIED:
			r.asker.sendMessage(ChatColor.RED + "Tph request denied!");
			r.target.sendMessage(ChatColor.RED + "Tph request denied!");
			break;
		case FORCEEND:
			break;
		case QUIT:
			r.asker.sendMessage(ChatColor.RED + "Tph request cancelled since " + r.target.getRankedName() + ChatColor.RED + " quit!");
			r.target.sendMessage(ChatColor.RED + "Tph request cancelled since " + r.asker.getRankedName() + ChatColor.RED + " quit!");
			break;
		case TIMEOUT:
			r.asker.sendMessage(ChatColor.RED + "Tph request timed out!");
			r.target.sendMessage(ChatColor.RED + "Tph request timed out!");
			break;
		case REFRESH:
			r.asker.sendMessage(ChatColor.GOLD + "Time refreshed!");
			break;
		}
	}
	
}
