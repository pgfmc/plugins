package net.pgfmc.masterbook.masterbook;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class BookClick implements Listener {
	
	@EventHandler
	public void bookClick(PlayerInteractEvent e) {
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Bukkit.getLogger().info("0");
		if (!e.hasItem()) return;
		Bukkit.getLogger().info("1");
		Player p = e.getPlayer();
		Bukkit.getLogger().info("2");
		if (p.isSneaking()) return;
		Bukkit.getLogger().info("3");
		if (p.getInventory().getItemInMainHand().getType() != Material.BOOK) return;
		Bukkit.getLogger().info("4");
		
		p.closeInventory();
		p.openInventory(new CommandsMenu(PlayerData.from(p)).getInventory());
	}
}
