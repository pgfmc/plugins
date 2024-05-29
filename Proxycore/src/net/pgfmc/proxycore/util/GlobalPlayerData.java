package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.kyori.adventure.text.Component;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.roles.PGFRole;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class GlobalPlayerData {
    
    public static final String dataPath = Main.plugin.configDirectory + File.separator + "playerdata" + File.separator;
    
    /* PluginMessageType isn't ready for non-strings yet
    @SuppressWarnings("unchecked")
	public static final <T> T getData(UUID uuid, String key)
    {
    	final Path path = Path.of(dataPath + uuid.toString() + ".toml");
    	final File file = Mixins.getFile(path);
    	final Toml toml = new Toml().read(file);
    	
    	if (!toml.contains(key)) return null;
    	
    	
    	return (T) toml.toMap().get(key);
    }
    */
    
	public static final String getData(UUID uuid, String key)
    {
    	final Path path = Path.of(dataPath + uuid.toString() + ".toml");
    	final File file = Mixins.getFile(path);
    	final Toml toml = new Toml().read(file);
    	
    	if (!toml.contains(key)) return null;
    	
    	
    	return toml.getString(key);
    }
    
    public static final void setData(UUID uuid, String key, String value)
    {
    	final Path path = Path.of(dataPath + uuid.toString() + ".toml");
    	final File file = Mixins.getFile(path);
    	final Toml toml = new Toml().read(file);
    	final TomlWriter writer = new TomlWriter();
    	final Map<String, Object> data = new HashMap<>();
    	
    	data.put(key, value);
    	
    	if (!toml.isEmpty())
    	{
    		data.putAll(toml.toMap());
    	}
    	
    	try {
    		
			writer.write(data, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	for (final RegisteredServer server : Main.plugin.proxy.getAllServers())
    	{
    		PluginMessageType.PLAYER_DATA.send(server, uuid.toString(), key, value);
    	}
    	
    }
    
    public static final String getUsername(UUID uuid)
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
    
    public static final String getNickname(UUID uuid)
    {
    	final String nickname = getData(uuid, "nickname");
    	
    	if (nickname == null || nickname.isBlank()) return getUsername(uuid);
    	
    	return nickname;
    }
    
    public static final Component getRankedName(UUID uuid)
    {
    	final PGFRole role = RoleManager.getRoleFromPlayerUuid(uuid);
    	
    	return Component.text(((role.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + getNickname(uuid)).color(role.getColor());
    }
    
    /*
    public static String getDiscordID(UUID uuid) {

        Path path = Path.of(dataPath + uuid.toString() + ".toml");

        try {
            Toml file = new Toml().read(Files.readString(path));
            return file.getString("discord");

        } catch (IOException ex) {
            return null;
        }
    }
    */
    
    /* sorry
    public static void setData(UUID uuid, String id, String nick) {

        String path = dataPath + uuid.toString() + ".toml";
        
        final Map<String, Object> content = new HashMap<>();

        if (id != null) {
        	content.put("discord", id);
        } else
        {
        	content.put("discord", "");
        }

        if (nick != null) {
        	content.put("nick", nick);
        } else
        {
        	content.put("nick", "");
        }

        File file = Mixins.getFile(Path.of(path));
        TomlWriter writer = new TomlWriter();

        try {
        	writer.write(content, file);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    */
}
