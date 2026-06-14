package net.pgfmc.survival.menu.staff.manageplayers;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.survival.menu.staff.inventorybackups.InventoryBackupListInventory;
import net.pgfmc.survival.menu.staff.inventorysee.InventorySeeSelectInventory;

public class ManagePlayerInventory extends BaseInventory {

	public ManagePlayerInventory(final PlayerData playerdata, final PlayerData target) {
		super(InventoryType.CHEST, Component.text("Manage Player"));
		
		setBack(0, new ManagePlayersListInventory(playerdata, true).getInventory());
		
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
				player.sendMessage(Component.text("Player is offline: cannot perform command.", NamedTextColor.RED));
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			});
			
		}
		
		setItem(11, Material.CHEST_MINECART)
            .name(Component.text("Inventory Backups", NamedTextColor.YELLOW))
            .lore(Component.text("Restore a player's inventory.", NamedTextColor.GRAY));
		
		
		
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
				player.sendMessage(NamedTextColor.RED + "Player is offline: cannot perform command.");
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			});
			
		}
		
		setItem(12, Material.PLAYER_HEAD)
            .name(Component.text("Inventory See", NamedTextColor.YELLOW));
		
		
		
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
					player.sendMessage(NamedTextColor.GREEN + "Poof!");
					playerdata.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
					
				});
				
				player.closeInventory();
				
			});
			
		} else
		{
			setAction(13, (player, event) -> {
				player.sendMessage(NamedTextColor.RED + "Player is offline: cannot perform command.");
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			});
			
		}
		
		setItem(13, Material.ENDER_PEARL)
            .name(Component.text("Teleport", NamedTextColor.YELLOW))
            .lore(Component.text("Teleport to a player.", NamedTextColor.GRAY),
                  Component.text("Consider turning vanish on!", NamedTextColor.GRAY));
		
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
				target.sendMessage(NamedTextColor.GOLD + "Your nickname has been reset by an admin to " + target.getRankedName() + NamedTextColor.GOLD + ".");
				CoreMain.updatePlayerNameplate(target);
				
				target.playSound(target.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
				
			}
			
			player.sendMessage(NamedTextColor.GOLD + "Player's nickname successfully reset to " + target.getRankedName() + NamedTextColor.GOLD + ".");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			
		});
		
		setItem(14, Material.NAME_TAG)
            .name(Component.text("Reset Nickname", NamedTextColor.YELLOW))
            .lore(Component.text("Reset a player's nickname.", NamedTextColor.GRAY));
		
		
		
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
				player.sendMessage(Component.text()
                        .append(target.getRankedName())
                        .append(Component.text(" was healed!", NamedTextColor.GREEN)).build());
				playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
				
			});
			
		} else
		{
			setAction(15, (player, event) -> {
				player.sendMessage(NamedTextColor.RED + "Player is offline: cannot perform command.");
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			});
			
		}
		
		setItem(15, Material.CAKE)
            .name(Component.text("Heal", NamedTextColor.YELLOW))
            .lore(Component.text("Heal the player completely."));
		
	}

}
