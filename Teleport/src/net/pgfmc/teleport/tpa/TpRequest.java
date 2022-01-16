package net.pgfmc.teleport.tpa;

import org.bukkit.Sound;

import net.pgfmc.core.requestAPI.Requester;
import net.pgfmc.core.teleportAPI.TimedTeleport;
import net.pgfmc.survival.cmd.Afk;

public class TpRequest extends Requester {
	
	public static final TpRequest TPA = new TpRequest("Teleport");
	public static final TpRequest TPAHERE = new TpRequest("Teleport Here");
	
	private TpRequest(String name)
	{
		super(name, 120, (a, b) -> {
			if (name.equals("Teleport"))
			{
				if (!(a.isOnline() && b.isOnline())) { return false; }
				
				a.sendMessage("§6Teleporting to " + b.getRankedName() + " §r§6in 5 seconds");
				b.sendMessage("§6Teleporting "+ a.getRankedName() +" §r§6here in 5 seconds");
				
				new TimedTeleport(a.getPlayer(), b.getPlayer(), 5, 40, true).setAct(v -> {
					a.sendMessage("§aPoof!");
					a.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
					if (Afk.isAfk(a.getPlayer())) { Afk.toggleAfk(a.getPlayer()); }
				});
				
				return true;
			}
			
			if (name.equals("Teleport Here"))
			{
				if (!(a.isOnline() && b.isOnline())) { return false; }
				
				b.sendMessage("§6Teleporting to " + a.getRankedName() + " §r§ain 5 seconds");
				a.sendMessage("§6Teleporting "+ b.getRankedName() +" §r§6here in 5 seconds");
				
				new TimedTeleport(b.getPlayer(), a.getPlayer(), 5, 40, true).setAct(v -> {
					b.sendMessage("§aPoof!");
					b.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
					if (Afk.isAfk(b.getPlayer())) { Afk.toggleAfk(b.getPlayer()); }
				});
				
				return true;
			}
			
			
			return false;
		});
	}

}
