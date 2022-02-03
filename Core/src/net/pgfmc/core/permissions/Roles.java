package net.pgfmc.core.permissions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles {
	
	private static Permissions pManager = new Permissions();
	
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
		private Set<Permission> permissions;
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
		
		public void setPermissions(Set<Permission> permissions)
		{
			this.permissions = permissions;
		}
		
		public void setId(String id)
		{
			this.id = id;
		}
		
		public Set<Permission> getPermissions()
		{
			return permissions;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void recalculate(PlayerData pd)
	{
		Bukkit.getLogger().warning("Recalculating roles for player " + pd.getName());
		
		String id = (String) pd.loadFromFile("Discord");
		List<String> roles = Arrays.asList(Role.MEMBER.getName());
		
		if (!CoreMain.PGFPlugin.BOT.isEnabled())
		{
			roles = (List<String>) Optional.ofNullable(pd.loadFromFile("roles"))
					.orElse(Arrays.asList(Role.MEMBER.getName()));
		} else if (id != null && CoreMain.PGFPlugin.BOT.isEnabled())
		{
			List<String> discordRoles = Discord.getMemberRoles(id);
			if (discordRoles != null) {
				pd.setData("Discord", id);
				roles = asString(getRolesById(discordRoles));
			}
		}
		
		pd.setData("roles", roles).save();
		pManager.recalculate(pd.getOfflinePlayer());
	}
	
	public static void recalculate(OfflinePlayer p)
	{
		recalculate(PlayerData.getPlayerData(p));
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
		List<String> roles = PlayerData.getData(p, "roles");
		
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
	
	public static Set<Permission> getPermissions(Collection<Role> roles)
	{
		if (roles.size() == 0 || roles == null) return Role.MEMBER.getPermissions();
		
		Set<Permission> permissions = new HashSet<Permission>();
		
		for (Role r : roles)
		{
			Set<Permission> p = r.getPermissions();
			if (p == null) continue;
			
			permissions.addAll(p);
		}
		
		return permissions;
	}
	
	public static List<String> getPermissionsAsString(Collection<Role> roles)
	{
		return getPermissions(roles).stream()
				.map(p -> p.getName())
				.collect(Collectors.toList());
	}
	
}