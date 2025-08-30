package net.pgfmc.survival.menu.profile;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.SoundEffect;
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
			final String discordUserId = playerdata.getData("discord");
			
			if (discordUserId != null)
			{
				setAction(11, (player, event) -> {
					player.performCommand("unlink");
					SoundEffect.ERROR.play(playerdata);
					player.openInventory(new ProfileInventory(playerdata).getInventory());
					
				});
				
				setItem(11, Material.AMETHYST_SHARD).n(ChatColor.LIGHT_PURPLE + "Unlink Discord Account");
			
			} else {
				
				setAction(11, (player, event) -> {
					player.closeInventory();
					player.performCommand("link");
					SoundEffect.PING.play(playerdata);
					
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
			SoundEffect.PING.play(playerdata);
			
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
				final String nickname = playerdata.getDisplayName();
				
				Builder builder = new AnvilGUI.Builder().plugin(Main.plugin);
				
				builder.onClose(stateSnapshot -> {});
				
				builder.onClick((slot, stateSnapshot) -> {
			        if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList(); // Do nothing
			        
			        final String newNickname = stateSnapshot.getText();
			        stateSnapshot.getPlayer().performCommand("nick " + newNickname);
			        
			        SoundEffect.PING.play(playerdata);
			        
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
				SoundEffect.ERROR.play(playerdata);
				playerdata.sendMessage(ChatColor.RED + "Only donators can use this command.");
				
			});
			
		}
		
		setItem(15, Material.NAME_TAG).n(ChatColor.LIGHT_PURPLE + "Change Nickname")
		.l(playerdata.getRankedName());
		
	}

}
