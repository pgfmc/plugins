package net.pgfmc.survival.inventories.masterbook;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class BookClick implements Listener {
	
	@EventHandler
	public void bookClick(PlayerInteractEvent e)
	{
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if (!e.hasItem()) return;
		
		Player p = e.getPlayer();
		
		if (p.isSneaking()) return;
		
		if (p.getInventory().getItemInMainHand().getType() != Material.BOOK) return;
		
		p.closeInventory();
		p.openInventory(new CommandsMenu(PlayerData.from(p)).getInventory());
		
	}
	
}
