package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.kyori.adventure.text.Component;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;
import net.pgfmc.proxycore.util.roles.PGFRole;

public class GlobalPlayerData {
    
    private static final Path dataPath = Path.of(Main.plugin.dataDirectory + File.separator + "playerdata");
    private static Map<UUID, GlobalPlayerData> playerDatas = new HashMap<UUID, GlobalPlayerData>();

    public final UUID uuid;

    public String nickname = "";
    public String username = "";
    public String discord = "";
    public PGFRole role = PGFRole.MEMBER;

    private GlobalPlayerData(UUID uuid) {
        this.uuid = uuid;

        Toml toml = getToml(uuid);

        this.username = toml.getString("username");
        this.nickname = toml.getString("nickname");
        this.discord = toml.getString("discord");
        String roleStep1 = toml.getString("role");
        this.role = PGFRole.get(roleStep1);
    }

    public static GlobalPlayerData fromUuid(UUID uuid) {
        GlobalPlayerData buffer = playerDatas.get(uuid); 

        if (buffer != null) {return buffer;}
        
        return new GlobalPlayerData(uuid);
    }

    public void save() {


        String out = "";

        if (username != null) {
            out = out + "username = \"" + username + "\"\n";
        }

        if (nickname != null) {
            out = out + "nickname = \"" + nickname + "\"\n";
        }

        if (discord != null) {
            out = out + "discord = \"" + discord + "\"\n";
        }

        if (role != null) {
            out = out + "role = \"" + role.toString() + "\"\n";
        }

        Logger.log(out);

    	final Path path = Path.of(dataPath + File.separator + uuid.toString() + ".toml");
    	final File file = Mixins.getFile(path);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(out);
            writer.close();
        }
        catch (IOException e) {}

        propogateGlobalPlayerData();
    }
    
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

//    public static final void setData(final UUID uuid, final String key, final Object value)
//    {
//    	if (uuid == null)
//    	{
//    		Logger.warn("GlobalPlayerData setData got null uuid");
//    		return;
//    	}
//    	
//    	if (key == null)
//    	{
//    		Logger.warn("GlobalPlayerData setData got null key");
//    		return;
//    	}
//    	
//    	final TomlWriter writer = new TomlWriter();
//    	String data = "";
//    	
//    	if (Objects.equals(value, null))
//    	{
//    		data = writer.write(Map.of(key, ""));
//    	} else
//    	{
//    		data = writer.write(Map.of(key, value));
//    	}
//    	
//    	final Toml toml = new Toml().read(data);
//    	
//    	setData(uuid, toml);
//    	
//    }
//    
//    public static final void setData(final UUID uuid, final Toml newToml)
//    {
//    	if (uuid == null)
//    	{
//    		Logger.warn("GlobalPlayerData setData got null uuid");
//    		return;
//    	}
//    	
//    	if (newToml == null)
//    	{
//    		Logger.warn("GlobalPlayerData setData got null TOML");
//    		return;
//    	}
//    	
//    	final Toml toml = getToml(uuid);
//    	final TomlWriter writer = new TomlWriter();
//    	final Map<String, Object> data = new HashMap<>();
//
//        
//    	
//    	if (!toml.isEmpty())
//    	{
//    		data.putAll(toml.toMap());
//    	}
//    	
//    	data.putAll(newToml.toMap());
//    	
//    	try {
//    		final Path path = Path.of(dataPath + File.separator + uuid.toString() + ".toml");
//        	final File file = Mixins.getFile(path);
//            	
//			writer.write(data, file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	propogateGlobalPlayerData(uuid);
//    	
//    }
//    
//    private static final String getUsername(UUID uuid)
//    {
//    	final String username = getData(uuid, "username");
//    	
//    	if (username == null || username.isBlank())
//    	{
//    		Optional<Player> player = Main.plugin.proxy.getPlayer(uuid);
//    		
//    		if (!player.isPresent()) return new String();
//    		
//    		setData(uuid, "username", player.get().getUsername());
//    		
//    		return player.get().getUsername();
//    	}
//    	
//    	return username;
//    }
//    
//    private static final String getNickname(UUID uuid)
//    {
//    	final String discordUserId = getData(uuid, "discord");
//    	
//    	if (discordUserId == null || discordUserId.isBlank()) return getUsername(uuid);
//    	
//    	final String nickname = getData(uuid, "nickname");
//    	
//    	if (nickname == null || nickname.isBlank()) return getUsername(uuid);
//    	
//    	return nickname;
//    }
    
//    public Component getRankedName() {
//        Player p = Main.plugin.proxy.getPlayer(uuid).get();
//        return getName(p.hasPermission("net.pgfmc.core.nick"));
//    }

    public final Component getRankedName(Player player) {
    	//final PGFRole role = RoleManager.getRoleFromPlayerUuid(uuid);
		
        if (!player.hasPermission("net.pgfmc.core.nick")) {
            return Component.text(username).color(role.getColor());
        }

		// This will use the player's nickname, or, if they don't have a nickname, their regular Minecraft user name
        String name = username;

        if (nickname != null && nickname != "") {
            name = nickname;
        }
		
		// If the player's role is STAFF or higher
		if (role.compareTo(PGFRole.STAFF) <= 0)
		{
			// Add the staff diamond icon to the beginning of the name
			name = PGFRole.STAFF_DIAMOND + name;
		}
		
		return Component.text(name).color(role.getColor());
    }
    
    public final void propogateGlobalPlayerData() {
    	
    	final Toml toml = getToml(uuid);
    	final TomlWriter writer = new TomlWriter();
    	final String data = writer.write(toml.toMap());
    	
    	for (final RegisteredServer server : Main.plugin.proxy.getAllServers())
    	{
    		PluginMessageType.PLAYER_DATA.send(server, uuid.toString(), data);
    	}
    }
}
