package net.pgfmc.survival.masterbook.inv.home.inv;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.masterbook.MasterbookInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

/*
 * https://github.com/WesJD/AnvilGUI
 */
public class HomeHomepage extends BaseInventory {
	
	public HomeHomepage(PlayerData playerdata)
	{
		super(27, ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Home Menu");
		
		setBack(0, new MasterbookInventory(playerdata).getInventory());
		
		setAction(13, (player, event) -> {
			player.openInventory(new HomeSelect(playerdata).getInventory());
		});
		
		setItem(13, Material.ENDER_PEARL).n(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Go to Home");
		
		setAction(11, (player, event) -> {
			Builder builder = new AnvilGUI.Builder();
			
			builder.onClose(stateSnapshot -> {});
			
			builder.onClick((slot, stateSnapshot) -> {
		        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
		        
		        final String homeName = stateSnapshot.getText();
		        stateSnapshot.getPlayer().performCommand("sethome " + homeName);
		        
		        return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {
					@Override
					public void run() {
						player.openInventory(new HomeHomepage(playerdata).getInventory());
						
					}}));
		    });
			
			builder.text("Enter name").title("Set Home").plugin(Main.plugin);
			builder.open(player);
			
		});
		
		setItem(11, Material.OAK_SAPLING).n(ChatColor.RESET + "" + ChatColor.GREEN + "Set Home");
		
		setAction(15, (player, event) -> {
			player.openInventory(new HomeDelete(playerdata).getInventory());
		});
		
		setItem(15, Material.FLINT_AND_STEEL).n(ChatColor.RESET + "" + ChatColor.RED + "Delete Home");
		
	}
	
}
