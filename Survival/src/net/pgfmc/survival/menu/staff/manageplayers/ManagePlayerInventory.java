package net.pgfmc.survival.menu.staff.manageplayers;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.SoundEffect;
import net.pgfmc.survival.menu.staff.inventorybackups.InventoryBackupListInventory;
import net.pgfmc.survival.menu.staff.inventorysee.InventorySeeSelectInventory;

public class ManagePlayerInventory extends BaseInventory {

	public ManagePlayerInventory(final PlayerData playerdata, final PlayerData target) {
		super(InventoryType.CHEST, "Manage Player");
		
		setBack(0, new ManagePlayersListInventory(playerdata).getInventory());
		
		/* 
		 * Inventory Backups
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (target.isOnline())
		{
			setAction(11, (player, event) -> {
				player.openInventory(new InventoryBackupListInventory(playerdata, target).getInventory());
				
			});
			
		} else
		{
			setAction(11, (player, event) -> {
				player.sendMessage(ChatColor.RED + "Player is offline: cannot perform command.");
				SoundEffect.ERROR.play(playerdata);
				
			});
			
		}
		
		setItem(11, Material.CHEST_MINECART).n(ChatColor.YELLOW + "Inventory Backups").l(ChatColor.GRAY + "Restore a player's inventory.");
		
		
		
		/* 
		 * Inventory See
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] XX [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (target.isOnline())
		{
			setAction(12, (player, event) -> {
				player.openInventory(new InventorySeeSelectInventory(playerdata, target).getInventory());
				
			});
			
		} else
		{
			setAction(12, (player, event) -> {
				player.sendMessage(ChatColor.RED + "Player is offline: cannot perform command.");
				SoundEffect.ERROR.play(playerdata);
				
			});
			
		}
		
		setItem(12, Material.PLAYER_HEAD).n(ChatColor.YELLOW + "Inventory See");
		
		
		
		/* 
		 * Teleport
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (target.isOnline())
		{
			setAction(13, (player, event) -> {
				new TimedTeleport(player, target.getPlayer().getLocation(), 0, 0, true).setAct(VOID -> {
					player.sendMessage(ChatColor.GREEN + "Poof!");
					SoundEffect.TELEPORT.play(playerdata);
					
				});
				
				player.closeInventory();
				
			});
			
		} else
		{
			setAction(13, (player, event) -> {
				player.sendMessage(ChatColor.RED + "Player is offline: cannot perform command.");
				SoundEffect.ERROR.play(playerdata);
				
			});
			
		}
		
		setItem(13, Material.ENDER_PEARL).n(ChatColor.YELLOW + "Teleport").l(Arrays.asList(ChatColor.GRAY + "Teleport to a player.", "Consider turning vanish on!"));
		
		
		
		/* 
		 * Reset Nickname
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] XX [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(14, (player, event) -> {
			target.setData("nickname", null).queue();
			
			if (target.isOnline())
			{
				target.sendMessage(ChatColor.GOLD + "Your nickname has been reset by an admin to " + target.getRankedName() + ChatColor.GOLD + ".");
				CoreMain.updatePlayerNameplate(target);
				
				SoundEffect.PING.play(target);
				
			}
			
			player.sendMessage(ChatColor.GOLD + "Player's nickname successfully reset to " + target.getRankedName() + ChatColor.GOLD + ".");
			SoundEffect.PING.play(player);
			
		});
		
		setItem(14, Material.NAME_TAG).n(ChatColor.YELLOW + "Reset Nickname").l(ChatColor.GRAY + "Reset a player's nickname.");
		
		
		
		/* 
		 * Heal
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (target.isOnline())
		{
			setAction(15, (player, event) -> {
				player.performCommand("heal " + target.getName());
				player.sendMessage(ChatColor.GREEN + target.getRankedName() + ChatColor.GREEN + " was healed!");
				SoundEffect.PING.play(playerdata);
				
			});
			
		} else
		{
			setAction(15, (player, event) -> {
				player.sendMessage(ChatColor.RED + "Player is offline: cannot perform command.");
				SoundEffect.ERROR.play(playerdata);
				
			});
			
		}
		
		setItem(15, Material.CAKE).n(ChatColor.YELLOW + "Heal").l("Heal the player completely.");
		
	}

}
