package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.kyori.adventure.text.Component;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;
import net.pgfmc.proxycore.util.roles.PGFRole;
import net.pgfmc.proxycore.util.roles.RoleManager;

public class GlobalPlayerData {
    
    private static final Path dataPath = Path.of(Main.plugin.dataDirectory + File.separator + "playerdata");
    
    private static final Toml getToml(final UUID uuid)
    {
    	final Path path = Path.of(dataPath + File.separator + uuid.toString() + ".toml");
    	final File file = Mixins.getFile(path);
    	
    	if (file == null)
    	{
    		Logger.error("GlobalPlayerData getToml encountered a null File.");
    		return null;
    	}
    	
    	final Toml toml = new Toml().read(file);
    	
    	return toml;
    }
    
    @SuppressWarnings("unchecked")
	public static final <T> T getData(final UUID uuid, final String key)
    {
    	final Toml toml = getToml(uuid);
    	
    	if (!toml.contains(key)) return null;
    	
    	return (T) toml.toMap().get(key);
    }
    
    public static final void setData(final UUID uuid, final String key, final Object value)
    {
    	if (uuid == null)
    	{
    		Logger.warn("GlobalPlayerData setData got null uuid");
    		return;
    	}
    	
    	if (key == null)
    	{
    		Logger.warn("GlobalPlayerData setData got null key");
    		return;
    	}
    	
    	final TomlWriter writer = new TomlWriter();
    	String data = "";
    	
    	if (Objects.equals(value, null))
    	{
    		data = writer.write(Map.of(key, ""));
    	} else
    	{
    		data = writer.write(Map.of(key, value));
    	}
    	
    	final Toml toml = new Toml().read(data);
    	
    	setData(uuid, toml);
    	
    }
    
    public static final void setData(final UUID uuid, final Toml newToml)
    {
    	if (uuid == null)
    	{
    		Logger.warn("GlobalPlayerData setData got null uuid");
    		return;
    	}
    	
    	if (newToml == null)
    	{
    		Logger.warn("GlobalPlayerData setData got null TOML");
    		return;
    	}
    	
    	final Toml toml = getToml(uuid);
    	final TomlWriter writer = new TomlWriter();
    	final Map<String, Object> data = new HashMap<>();
    	
    	if (!toml.isEmpty())
    	{
    		data.putAll(toml.toMap());
    	}
    	
    	data.putAll(newToml.toMap());
    	
    	try {
    		final Path path = Path.of(dataPath + File.separator + uuid.toString() + ".toml");
        	final File file = Mixins.getFile(path);
        	
			writer.write(data, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	propogateGlobalPlayerData(uuid);
    	
    }
    
    private static final String getUsername(UUID uuid)
    {
    	final String username = getData(uuid, "username");
    	
    	if (username == null || username.isBlank())
    	{
    		Optional<Player> player = Main.plugin.proxy.getPlayer(uuid);
    		
    		if (!player.isPresent()) return new String();
    		
    		setData(uuid, "username", player.get().getUsername());
    		
    		return player.get().getUsername();
    	}
    	
    	return username;
    }
    
    private static final String getNickname(UUID uuid)
    {
    	final String discordUserId = getData(uuid, "discord");
    	
    	if (discordUserId == null || discordUserId.isBlank()) return getUsername(uuid);
    	
    	final String nickname = getData(uuid, "nickname");
    	
    	if (nickname == null || nickname.isBlank()) return getUsername(uuid);
    	
    	return nickname;
    }
    
    public static final Component getRankedName(UUID uuid)
    {
    	final PGFRole role = RoleManager.getRoleFromPlayerUuid(uuid);
    	
    	return Component.text(((role.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + getNickname(uuid)).color(role.getColor());
    }
    
    public static final void propogateGlobalPlayerData(final UUID uuid)
    {
    	if (uuid == null)
    	{
    		
    		return;
    	}
    	
    	final Toml toml = getToml(uuid);
    	final TomlWriter writer = new TomlWriter();
    	final String data = writer.write(toml.toMap());
    	
    	for (final RegisteredServer server : Main.plugin.proxy.getAllServers())
    	{
    		PluginMessageType.PLAYER_DATA.send(server, uuid.toString(), data);
    	}
    	
    }
    
}
