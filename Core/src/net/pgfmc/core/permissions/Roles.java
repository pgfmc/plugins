package net.pgfmc.core.permissions;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import net.pgfmc.bot.Discord;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles extends Configify implements Listener {
	
	public Roles() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "roles.yml"));
		
		for (Role r : Role.values())
		{
			setDefaultValue("config-version", "1");
			setDefaultValue(r.getName() + ".id", "000000000000000000");
			setDefaultValue(r.getName() + ".color", "r");
		}
	}
	
	public enum Role {
		FOUNDER,
		ADMIN,
		DEVELOPER,
		MODERATOR,
		TRAINEE,
		STAFF,
		DOOKIE,
		DONATOR,
		VETERAN,
		MEMBER;
		
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
		
		public static Role get(String role)
		{
			for (Role r : Role.values())
			{
				if (r.getName().equals(role)) return r;
			}
			
			return Role.MEMBER;
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
	
	@SuppressWarnings("unchecked")
	public static void recalculate(PlayerData pd)
	{
		Bukkit.getLogger().warning("Recalculating roles for player " + pd.getName());
		
		String id = (String) Optional.ofNullable(pd.getData("Discord")).orElse(pd.loadFromFile("Discord"));
		List<String> roles = Arrays.asList(Role.MEMBER.getName());
		
		if (!CoreMain.PGFPlugin.BOT.isEnabled() && pd.loadFromFile("Discord") != null)
		{
			roles = (List<String>) Optional.ofNullable(pd.loadFromFile("roles"))
					.orElse(Arrays.asList(Role.MEMBER.getName()));
			
		} else if (id != null)
		{
			List<String> discordRoles = Discord.getMemberRoles(id);
			if (discordRoles != null) {
				pd.setData("Discord", id).queue();
				roles = asString(getRolesById(discordRoles));
			}
		}
		
		pd.setData("roles", roles).queue();
		
		LuckPerms lp = LuckPermsProvider.get();
		Set<Group> lpGroups = lp.getGroupManager().getLoadedGroups();
		
		for (Group group : lpGroups)
		{
			String groupName = group.getName();
			UserManager userManager = lp.getUserManager();
			
			// Add roles
			if (roles.contains(groupName))
			{
				userManager.modifyUser(pd.getUniqueId(), user -> {
					user.data().add(Node.builder("group." + groupName).build());
				});
			} else // Remove roles
			{
				userManager.modifyUser(pd.getUniqueId(), user -> {
					user.data().remove(Node.builder("group." + groupName).build());
				});
			}
		}
	}
	
	public static void recalculate(OfflinePlayer p)
	{
		recalculate(PlayerData.from(p));
	}
	
	public static Role getRoleById(String id)
	{
		for (Role r : Role.values())
		{
			if (id.equals(r.getId())) return r;
		}
		
		return Role.MEMBER;
	}
	
	public static Set<Role> getRolesByPlayer(OfflinePlayer p)
	{
		PlayerData pd = PlayerData.from(p);
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) Optional.ofNullable(pd.getData("roles")).orElse(pd.loadFromFile("roles"));
		
		if (roles == null) return Set.of(Role.MEMBER);
		
		return getRolesByString(roles);
	}
	
	public static Set<Role> getRolesById(Collection<String> ids)
	{
		if (ids.size() == 0 || ids == null) return Set.of(Role.MEMBER);
		
		return ids.stream()
				.map(id -> getRoleById(id))
				.collect(Collectors.toSet());
	}
	
	public static Set<Role> getRolesByString(Collection<String> roles)
	{
		if (roles.size() == 0 || roles == null) return Set.of(Role.MEMBER);
		
		return roles.stream()
				.map(r -> Role.get(r))
				.filter(r -> (r != null))
				.collect(Collectors.toSet());
	}
	
	public static List<String> asString(Collection<Role> roles)
	{
		if (roles.size() == 0 || roles == null) return Arrays.asList(Role.MEMBER.getName());
		
		return roles.stream().map(r -> r.getName()).collect(Collectors.toList());
	}
	
	public static Role getTop(Collection<Role> roles)
	{
		if (roles.size() == 0 || roles == null) return Role.MEMBER;
		if (roles.size() == 1) return roles.stream().collect(Collectors.toList()).get(0);
		
		return roles.stream()
				.sorted((r1, r2) -> r1.compareTo(r2))
				.collect(Collectors.toList())
				.get(0);
	}
	
	public static Role getTop(OfflinePlayer p)
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
		
		for (Role r : Role.values())
		{
			r.setId(db.getString(r.getName() + ".id"));
			r.setColor(db.getString(r.getName() + ".color"));
			
		}
		
		PlayerData.getPlayerDataSet(x -> x.isOnline())
		.forEach(pd -> Roles.recalculate(pd));
		
	}

	@Override
	public void enable() {
		reload();
	}

	@Override
	public void disable() {}
	
}