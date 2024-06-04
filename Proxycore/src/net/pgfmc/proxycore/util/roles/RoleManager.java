package net.pgfmc.proxycore.util.roles;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

import com.moandjiezana.toml.Toml;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.Mixins;

public final class RoleManager  {
	
	private static final LuckPerms lp = Main.lp;
	
	public static final PGFRole getRoleFromPlayerUuid(final UUID uuid)
	{
		if (uuid == null) return PGFRole.MEMBER;
		
		final String discordUserId = getDiscordUserIdFromPlayerUuid(uuid);
		
		if (discordUserId == null || discordUserId.isBlank()) return PGFRole.MEMBER;
		
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
		final File playerdataParentFile = Mixins.getFile(Path.of(Main.plugin.dataDirectory + File.separator + "playerdata"));
		
		for (final File playerdataFile : playerdataParentFile.listFiles())
		{
			final Toml toml = new Toml()
					.read(playerdataFile);
			
			if (toml.contains("discord") && Objects.equals(toml.getString("discord"), discordUserId)) continue;
			
			return UUID.fromString(playerdataFile.getName().replace(".toml", ""));
			
		}
		
		return null;
	}

	public static final void updatePlayerRole(final UUID playerUuid)
	{
		Main.plugin.updateTablist();
		
		final PGFRole role = getRoleFromPlayerUuid(playerUuid);
		final UserManager userManager = lp.getUserManager();
		
		// Remove then add groups, save changes
		userManager.modifyUser(playerUuid, user -> {
	        user.data().clear(NodeType.INHERITANCE::matches);
	        
	        if (Objects.equals(role.name().toLowerCase(), "member"))
			{
				final Node node = Node.builder("group.default").build();
				user.data().add(node);
				Logger.debug("Updated permissions for " + user.getFriendlyName());
				
			} else
			{
	            final Node node = Node.builder("group." + role.name().toLowerCase()).build();
	            user.data().add(node);
	            Logger.debug("Updated permissions for " + user.getFriendlyName());
	            
			}
	        
		});
		
		GlobalPlayerData.setData(playerUuid, "role", role.name());
		
	}
	
}
