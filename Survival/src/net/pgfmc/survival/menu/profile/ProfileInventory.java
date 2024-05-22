package net.pgfmc.survival.menu.profile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.menu.CommandMenuInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class ProfileInventory extends BaseInventory {

	public ProfileInventory(PlayerData playerdata) {
		super(InventoryType.CHEST, "Profile");
		
		setBack(0, new CommandMenuInventory(playerdata).getInventory());
		
		/* 
		 * Link
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("net.pgfmc.core.link"))
		{
			final String discordID = playerdata.getData("Discord");
			
			if (discordID != null)
			{
				setAction(11, (player, event) -> {
					player.performCommand("unlink");
					playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					player.openInventory(new ProfileInventory(playerdata).getInventory());
					
				});
				
				final String discordGlobalName = Discord.JDA.getUserById(discordID).getGlobalName();
				final PGFRole role = playerdata.getRole();
				
				setItem(11, Material.AMETHYST_SHARD).n(ChatColor.LIGHT_PURPLE + "Unlink Account")
													.l(role.getColor() + discordGlobalName);
			
			} else {
				
				setAction(11, (player, event) -> {
					player.closeInventory();
					player.performCommand("link");
					playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					
				});
				
				setItem(11, Material.QUARTZ).n(ChatColor.LIGHT_PURPLE + "Link Account")
				.l(ChatColor.GRAY + "Click to get a link code.");
				
			}
			
		}
		
		
		/* 
		 * Server Info
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(13, (player, event) -> {
			playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Website: " + ChatColor.BLUE + ChatColor.ITALIC + ChatColor.UNDERLINE + "https://www.pgfmc.net");
			playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Discord Server: " + ChatColor.BLUE + ChatColor.ITALIC + ChatColor.UNDERLINE + "https://discord.gg/zdxeREe");
			playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Talk to bk about becoming a donator! :)");
			playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			
		});
		
		setItem(13, Material.FLOWER_BANNER_PATTERN).n(ChatColor.LIGHT_PURPLE + "Server Info")
												.l(ChatColor.GRAY + "Click to see Discord, website, and donation info.");
		
		
		/* 
		 * Nickname
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("net.pgfmc.survival.nick"))
		{
			setAction(15, (player, event) -> {
				final String nickname = (String) Optional.ofNullable(playerdata.getData("nick")).orElse(player.getName());
				
				Builder builder = new AnvilGUI.Builder();
				
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
				playerdata.sendMessage(ChatColor.RED + "Only donators can use this command.");
				
			});
			
		}
		
		setItem(15, Material.NAME_TAG).n(ChatColor.LIGHT_PURPLE + "Change Nickname")
		.l(playerdata.getRankedName());
		
	}

}
