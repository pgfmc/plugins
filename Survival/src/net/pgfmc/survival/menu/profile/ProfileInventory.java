package net.pgfmc.survival.menu.profile;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.menu.CommandMenuInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class ProfileInventory extends BaseInventory {

	public ProfileInventory(PlayerData playerdata) {
		super(InventoryType.CHEST, Component.text("Profile"));
		
		setBack(0, new CommandMenuInventory(playerdata).getInventory());
		
		/* 
		 * Link
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("net.pgfmc.core.link"))
		{
			final String discordUserId = playerdata.getData("discord");
			
			if (discordUserId != null)
			{
				setAction(11, (player, event) -> {
					player.performCommand("unlink");
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					player.openInventory(new ProfileInventory(playerdata).getInventory());
					
				});
				
				setItem(11, Material.AMETHYST_SHARD)
                    .name(Component.text("Unlink Discord Account", NamedTextColor.LIGHT_PURPLE));
			
			} else {
				
				setAction(11, (player, event) -> {
					player.closeInventory();
					player.performCommand("link");
					playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					
				});
				
				setItem(11, Material.QUARTZ)
                    .name(Component.text("Link Account", NamedTextColor.LIGHT_PURPLE))
				.lore(Component.text("Click to get a link code.", NamedTextColor.GRAY));
				
			}
			
		}
		
		
		/* 
		 * Server Info
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(13, (player, event) -> {
			playerdata.sendMessage(Component.text()
                    .append(Component.text("Website: ", NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("https://www.pgfmc.net", NamedTextColor.BLUE, TextDecoration.ITALIC, TextDecoration.UNDERLINED)).build());
			playerdata.sendMessage(Component.text()
                    .append(Component.text("Discord Server: ", NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text("https://discord.gg/zdxeREe", NamedTextColor.BLUE, TextDecoration.ITALIC, TextDecoration.UNDERLINED)).build());
			playerdata.sendMessage(NamedTextColor.LIGHT_PURPLE + "Talk to bk about becoming a donator! :)");
			playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			
		});
		
		setItem(13, Material.FLOWER_BANNER_PATTERN)
            .name(Component.text("Server Info", NamedTextColor.LIGHT_PURPLE))
			.lore(Component.text("Click to see Discord, website, and donation info.", NamedTextColor.GRAY));
		
		
		/* 
		 * Nickname
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("net.pgfmc.survival.nick"))
		{
			setAction(15, (player, event) -> {
				final String nickname = playerdata.getDisplayName();
				
				Builder builder = new AnvilGUI.Builder().plugin(Main.plugin);
				
				builder.onClose(stateSnapshot -> {});
				
				builder.onClick((slot, stateSnapshot) -> {
			        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
			        
			        final String newNickname = stateSnapshot.getText();
			        stateSnapshot.getPlayer().performCommand("nick " + newNickname);
			        
			        playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			        
			        return Arrays.asList(AnvilGUI.ResponseAction.run(new Runnable() {

						@Override
						public void run() {
							player.openInventory(new ProfileInventory(playerdata).getInventory());
							
						}}));
			    });
				
				builder.text(nickname).title("Enter a nickname").plugin(Main.plugin);
				builder.open(player);
				
			});
			
			
		} else
		{
			setAction(15, (player, event) -> {
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				playerdata.sendMessage(NamedTextColor.RED + "Only donators can use this command.");
				
			});
			
		}
		
		setItem(15, Material.NAME_TAG)
            .name(Component.text("Change Nickname", NamedTextColor.LIGHT_PURPLE))
		    .lore(playerdata.getRankedName());
	}
}
