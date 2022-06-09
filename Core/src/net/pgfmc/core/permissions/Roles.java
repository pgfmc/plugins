package net.pgfmc.core.permissions;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.pgfmc.bot.Discord;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles extends Configify implements Listener {
	
	public Roles() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "roles.yml"));
		
		for (PGFRole r : PGFRole.values())
		{
			setDefaultValue("config-version", "1");
			setDefaultValue(r.getName() + ".id", "000000000000000000");
			setDefaultValue(r.getName() + ".color", "r");
		}
	}
	
	public enum PGFRole {
		FOUNDER,
		ADMIN,
		DEVELOPER,
		MODERATOR,
		TRAINEE,
		STAFF,
		DOOKIE,
		DONATOR,
		VETERAN,
		DEFAULT;
		
		private String color;
		private String id;
		
		public String getName()
		{
			return name().toLowerCase();
		}
		
		public String getColor() {
			return ChatColor.COLOR_CHAR + color;
		}
		
		public String getId() {
			return id;
		}
		
		public static PGFRole get(String role)
		{
			for (PGFRole r : PGFRole.values())
			{
				if (r.getName().equals(role)) return r;
			}
			
			return PGFRole.DEFAULT;
		}
		
		public void setColor(String color)
		{
			this.color = color;//color.replaceAll("[^A-Za-z0-9âœ¦]", "").toLowerCase();
		}
		
		public void setId(String id)
		{
			this.id = id;
		}
		
	}
	
	public static void recalculate(PlayerData pd, Collection<PGFRole> roles)
	{
		Bukkit.getLogger().warning("Recalculating roles for player " + pd.getName());
		
		if (roles.isEmpty())
		{
			roles.add(PGFRole.DEFAULT);
		}
		
		LuckPerms lp = LuckPermsProvider.get();
		Set<Group> lpGroups = lp.getGroupManager().getLoadedGroups();
		UserManager userManager = lp.getUserManager();
		
		userManager.modifyUser(pd.getUniqueId(), user -> {
			// Remove all other inherited groups the user had before.
	        user.data().clear(NodeType.INHERITANCE::matches);
		});
		
		PGFRole role = getTop(roles);
		Group group = lpGroups.stream().filter(g -> g.getName().toLowerCase().equals(role.getName())).collect(Collectors.toList()).get(0);
		
		userManager.modifyUser(pd.getUniqueId(), user -> {
			
            // Create a node to add to the player.
            Node node = InheritanceNode.builder(group).build();

            // Add the node to the user.
            user.data().add(node);
		});
		
	}
	
	public static void recalculate(OfflinePlayer p)
	{
		recalculate(PlayerData.from(p), getRolesByPlayer(p));
	}
	
	public static PGFRole getRoleById(String id)
	{
		for (PGFRole r : PGFRole.values())
		{
			if (id.equals(r.getId())) return r;
		}
		
		return PGFRole.DEFAULT;
	}
	
	public static Set<PGFRole> getRolesByPlayer(OfflinePlayer p)
	{
		PlayerData pd = PlayerData.from(p);
		
		if (CoreMain.PGFPlugin.BOT.isEnabled() && pd.getData("Discord") != null)
		{
			return getRolesById(Discord.getMemberRoles(pd.getData("Discord")));
		}
		
			return new HashSet<PGFRole>(Set.of(PGFRole.DEFAULT));
	}
	
	public static Set<PGFRole> getRolesById(Collection<String> ids)
	{
		if (ids.size() == 0 || ids == null) return Set.of(PGFRole.DEFAULT);
		
		return ids.stream()
				.map(id -> getRoleById(id))
				.collect(Collectors.toSet());
	}
	
	public static Set<PGFRole> getRolesByString(Collection<String> roles)
	{
		if (roles.size() == 0 || roles == null) return Set.of(PGFRole.DEFAULT);
		
		return roles.stream()
				.map(r -> PGFRole.get(r))
				.filter(r -> (r != null))
				.collect(Collectors.toSet());
	}
	
	public static List<String> asString(Collection<PGFRole> roles)
	{
		if (roles.size() == 0 || roles == null) return Arrays.asList(PGFRole.DEFAULT.getName());
		
		return roles.stream().map(r -> r.getName()).collect(Collectors.toList());
	}
	
	public static PGFRole getTop(Collection<PGFRole> roles)
	{
		if (roles.size() == 0 || roles == null) return PGFRole.DEFAULT;
		if (roles.size() == 1) return roles.stream().collect(Collectors.toList()).get(0);
		
		return roles.stream()
				.sorted((r1, r2) -> r1.compareTo(r2))
				.collect(Collectors.toList())
				.get(0);
	}
	
	public static PGFRole getTop(OfflinePlayer p)
	{
		return getTop(getRolesByPlayer(p));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Roles.recalculate(e.getPlayer());
	}

	@Override
	public void reload()
	{
		FileConfiguration db = getConfig();
		
		Bukkit.getLogger().warning("Reloading roles.yml");
		
		for (PGFRole r : PGFRole.values())
		{
			r.setId(db.getString(r.getName() + ".id"));
			r.setColor(db.getString(r.getName() + ".color"));
			
		}
		
		PlayerData.getPlayerDataSet().forEach(pd -> recalculate(pd, getRolesByPlayer(pd.getOfflinePlayer())));
		
	}

	@Override
	public void enable() {
		reload();
	}

	@Override
	public void disable() {}
	
}