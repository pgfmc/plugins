package net.pgfmc.proxycore.roles;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.Mixins;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public final class RoleManager  {
	
	private static final LuckPerms lp = Main.lp;
	
	public static final PGFRole getRoleFromPlayerUuid(final UUID uuid)
	{
		if (uuid == null) return PGFRole.MEMBER;
		
		final String discordUserId = getDiscordUserIdFromPlayerUuid(uuid);
		
		if (discordUserId == null) return PGFRole.MEMBER;
		
		final PGFRole role = Discord.getTopRoleOfMember(
				Discord.getJda()
				.getGuildById(Discord.GUILD_ID_PGF)
				.getMemberById(discordUserId));
		
		return role;
	}
	
	public static final String getDiscordUserIdFromPlayerUuid(final UUID uuid)
	{
		final String discordUserId = GlobalPlayerData.getData(uuid, "discord");
		
		if (discordUserId == null || discordUserId.isBlank()) return null;
		
		return discordUserId;
	}
	
	public static final UUID getPlayerUuidFromDiscordUserId(final String discordUserId)
	{
		final File playerdataParentFile = Mixins.getFile(Path.of(Main.plugin.configDirectory + File.separator + "playerdata"));
		
		for (final File playerdataFile : playerdataParentFile.listFiles())
		{
			final Toml toml = new Toml()
					.read(playerdataFile);
			
			if (toml.contains("discord") && Objects.equals(toml.getString("discord"), discordUserId)) continue;
			
			return UUID.fromString(playerdataFile.getName().replace(".toml", ""));
			
		}
		
		return null;
	}
	
	public static final void linkPlayerDiscord(final UUID playerUuid, final String discordUserId)
	{
		GlobalPlayerData.setData(playerUuid, "discord", discordUserId);
		
		propogatePlayerRole(playerUuid, discordUserId);
		
	}
	
	public static final void unlinkPlayerDiscord(final UUID playerUuid)
	{
		GlobalPlayerData.setData(playerUuid, "discord", null);
		
		propogatePlayerRole(playerUuid, null);
		
	}

	public static final void propogatePlayerRole(final UUID playerUuid, final String discordUserId)
	{
		Main.plugin.updateTablist();
		
		PGFRole role = PGFRole.MEMBER;
		
		if (discordUserId != null) // may be null
		{
			role = Discord.getTopRoleOfMember(
					Discord.getJda()
					.getGuildById(Discord.GUILD_ID_PGF)
					.getMemberById(discordUserId));
		}
		
		final PGFRole finalRole = role;
		final UserManager userManager = lp.getUserManager();
		
		// Remove then add groups, save changes
		userManager.modifyUser(playerUuid, user -> {
	        user.data().clear(NodeType.INHERITANCE::matches);
	        
	        if (Objects.equals(finalRole.name().toLowerCase(), "member"))
			{
				final Node node = Node.builder("group.default").build();
				Logger.debug("Updated roles: " + user.data().add(node).toString());
				
			} else
			{
	            final Node node = Node.builder("group." + finalRole.name().toLowerCase()).build();
	            Logger.debug("Updated roles: " + user.data().add(node).toString());
	            
			}
	        
		});
		
		for (final RegisteredServer server : Main.plugin.proxy.getAllServers())
		{
			PluginMessageType.PLAYER_DATA.send(server, playerUuid.toString(), "discord", (String) Optional.ofNullable(discordUserId).orElse(""));
			PluginMessageType.PLAYER_DATA.send(server, playerUuid.toString(), "role", role.name());
			
		}
		
	}
	
}
