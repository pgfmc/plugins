package net.pgfmc.survival.menu.staff;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ServerMessage;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.menu.CommandMenuInventory;
import net.pgfmc.survival.menu.staff.giverewards.GiveRewardsListInventory;
import net.pgfmc.survival.menu.staff.manageplayers.ManagePlayersListInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class StaffInventory extends BaseInventory {

	public StaffInventory (final PlayerData playerdata) {
		super(InventoryType.CHEST, "Staff Commands");
		
		setBack(0, new CommandMenuInventory(playerdata).getInventory());
		
		/* 
		 * Vanish
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(11, (player, event) -> {
			player.performCommand("vanish");
		});
		
		final ItemStack potion = new ItemStack(Material.POTION);
		final PotionMeta meta = (PotionMeta) potion.getItemMeta(); // Cast ItemMeta to PotionMeta
		meta.setBasePotionType(PotionType.INVISIBILITY);
		potion.setItemMeta(meta);
		
		setItem(11, potion).n(ChatColor.YELLOW + "Vanish").l(ChatColor.GRAY + "Enable/disable vanish mode.");
		
		
		
		/* 
		 * Fly
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] XX [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(12, (player, event) -> {
			player.performCommand("fly");
		});
		
		setItem(12, Material.ELYTRA).n(ChatColor.YELLOW + "Fly").l(ChatColor.GRAY + "Enable/disable fly mode.");
		
		
		
		/* 
		 * Manage Players
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(13, (player, event) -> {
			player.openInventory(new ManagePlayersListInventory(playerdata, true).getInventory());
		});
		
		setItem(13, Material.PLAYER_HEAD).n(ChatColor.YELLOW + "Online Players").l(ChatColor.GRAY + "Commands for managing individual players.");
		
		
		
		/* 
		 * Broadcast
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] XX [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(14, (player, event) -> {
			Builder builder = new AnvilGUI.Builder().plugin(Main.plugin);
			
			builder.onClose(stateSnapshot -> {});
			
			builder.onClick((slot, stateSnapshot) -> {
		        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
		        
		        final String announcement_text = stateSnapshot.getText();
		        ServerMessage.sendServerMessage(announcement_text);
		        
		        return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {
					@Override
					public void run() {
						player.openInventory(new StaffInventory(playerdata).getInventory());
						
					}}));
		        
			});
			
			builder.text("message").title("Broadcast").plugin(Main.plugin);
			builder.open(player);
			
		});
		
		setItem(14, Material.GOAT_HORN).n(ChatColor.YELLOW + "Broadcast").l(Arrays.asList(ChatColor.GRAY + "Send a server message."
																											, "50 characters max. For a longer message,"
																											, "use /broadcast <message>"));
		
		
		
		
		/* 
		 * Give Rewards
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(15, (player, event) -> {
			player.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
		});
		
		setItem(15, Material.BOOKSHELF).n(ChatColor.YELLOW + "Give Rewards").l(ChatColor.GRAY + "Add a reward to all players.");
		
	}

}
