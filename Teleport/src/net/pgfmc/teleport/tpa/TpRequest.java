package net.pgfmc.teleport.tpa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.teleportAPI.TimedTeleport;
import net.pgfmc.survival.cmd.Afk;

public class TpRequest extends Request {
	
	protected TpRequest(PlayerData asker, PlayerData target) {
		super(asker, target, 120);
	}

	@Override
	protected void endRequest(EndBehavior eB) {
		switch(eB) {
		case ACCEPT:
			
			asker.sendMessage("§6Teleporting to " + target.getRankedName() + " §r§6in 5 seconds");
			target.sendMessage("§6Teleporting "+ asker.getRankedName() +" §r§6here in 5 seconds");
			
			new TimedTeleport(asker.getPlayer(), target.getPlayer(), 5, 40, true).setAct(v -> {
				asker.sendMessage("§aPoof!");
				asker.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
				if (Afk.isAfk(asker.getPlayer())) { Afk.toggleAfk(asker.getPlayer()); }
			});
			
		case DENIED:
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case TIMEOUT:
			break;
		}
	}

	@Override
	public ItemStack toItem() {
		return new ItemWrapper(Material.ENDER_PEARL).gi();
	}
}
