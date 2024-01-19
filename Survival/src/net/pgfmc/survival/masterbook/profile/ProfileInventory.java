package net.pgfmc.survival.masterbook.profile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

import net.pgfmc.core.api.inventory.BaseInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.util.roles.PGFRole;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.masterbook.MasterbookInventory;
import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.Builder;

public class ProfileInventory extends BaseInventory {

	public ProfileInventory(PlayerData playerdata) {
		super(27, ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Profile");
		
		setBack(0, new MasterbookInventory(playerdata).getInventory());
		
		/* 
		 * Link
		 * [] [] [] [] [] [] [] [] []
		 * [] [] XX [] [] [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("pgf.cmd.link"))
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
				
				setItem(11, Material.AMETHYST_SHARD).n(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Unlink Account")
													.l(ChatColor.RESET + "" + role.getColor() + discordGlobalName);
			
			} else {
				
				setAction(11, (player, event) -> {
					player.closeInventory();
					player.performCommand("link");
					playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
					
				});
				
				setItem(11, Material.QUARTZ).n(ChatColor.LIGHT_PURPLE + "Link Account")
				.l(ChatColor.RESET + "" + ChatColor.GRAY + "Click to get a link code.");
				
			}
			
		}
		
		
		/* 
		 * Server Info
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] XX [] [] [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		setAction(13, (player, event) -> {
			playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "PGF Discord Server: " + ChatColor.BLUE + ChatColor.ITALIC + ChatColor.UNDERLINE + "https://discord.gg/zdxeREe");
			playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Talk to bk about becoming a donator! :)");
			playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
			
		});
		
		setItem(13, Material.FLOWER_BANNER_PATTERN).n(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "PGF Season 12")
												.l("Click to see Discord and Donation info.");
		
		
		/* 
		 * Nickname
		 * [] [] [] [] [] [] [] [] []
		 * [] [] [] [] [] [] XX [] []
		 * [] [] [] [] [] [] [] [] []
		 */
		if (playerdata.hasPermission("pgf.cmd.donator.nick"))
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
		
		setItem(15, Material.NAME_TAG).n(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Change Nickname")
		.l(ChatColor.RESET + playerdata.getRankedName());
		
	}

}
