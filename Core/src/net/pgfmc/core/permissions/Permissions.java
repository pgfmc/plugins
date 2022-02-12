package net.pgfmc.core.permissions;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.permissions.Roles.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Permissions extends Configify implements Listener {
	
	private static HashMap<String, PermissionAttachment> permatches = new HashMap<>();
	private static Set<Permission> allPermissions = new HashSet<Permission>();
	
	public Permissions() {
		super(Mixins.getFile(CoreMain.plugin.getDataFolder() + File.separator + "permissions.yml"));
		
		for (Role r : Role.values())
		{
			setDefaultValue("config-version", "1");
			setDefaultValue(r.getName() + ".id", "000000000000000000");
			setDefaultValue(r.getName() + ".color", "r");
			setDefaultValue(r.getName() +  ".permissions", Arrays.asList("test.permission.1", "test.permission.2"));
		}
	}
	
	public static boolean has(OfflinePlayer p, String permission)
	{
		// Gets the permissions the slowest pay, only runs if the player is offline
		if (!p.isOnline()) return Roles.getPermissionsAsString(Roles.getRolesByPlayer(p)).contains(permission);
		
		// If the player has a matching permission set to true
		return !p.getPlayer()
				.getEffectivePermissions().stream()
				.filter(perm -> perm.getPermission().equals(permission) && perm.getValue())
				.collect(Collectors.toList())
				.isEmpty();
		
	}
	
	@Deprecated
	public static boolean has(OfflinePlayer p, Permission permission)
	{
		return has(p, permission.getName());
	}
	
	/**
	 * Recalculates Player permissions
	 * This shouldn't ever be ran manually, instead see {@link Roles#recalculate(PlayerData)}
	 * @param pd
	 */
	void recalculate(OfflinePlayer op)
	{
		Bukkit.getLogger().warning("Recalculating permisisons for player " + op.getName());
		
		Set<Role> roles = Roles.getRolesByPlayer(op);
		Set<Permission> perms = Roles.getPermissions(roles);
		
		Player p = op.getPlayer();
		
		clear(op);
		
		if (p == null || !op.isOnline())
		{
			Bukkit.getLogger().warning("Updating perms failed, player was offline");
			return;
		}
		
		PermissionAttachment permatch = p.addAttachment(CoreMain.plugin);
		permatches.put(p.getUniqueId().toString(), permatch);
		
		// Bukkit.getPluginManager().getPermissions().forEach(pp -> permatch.setPermission(pp, false));
		
		for (Permission perm : perms)
		{
			//Bukkit.getLogger().warning(perm.getName());
			if (perm.getName().startsWith("-"))
			{
				permatch.setPermission(perm, false);
				continue;
			}
			
			permatch.setPermission(perm, true);
			
		}
		
		p.recalculatePermissions();
		p.updateCommands();
	}
	
	public static void clear()
	{
		permatches.forEach((uuid, permatch) -> permatch.remove());
		permatches.clear();
	}
	
	public static void clear(OfflinePlayer p)
	{
		PermissionAttachment permatch = permatches.get(p.getUniqueId().toString());
		if (permatch != null) { permatch.remove(); }
		permatches.remove(p.getUniqueId().toString());
	}
	
	private static Set<Permission> includeWildcards(Set<Permission> permissions)
	{
		for (Permission perm : permissions)
		{
			//Bukkit.getPluginManager().getPermissions().stream().forEach(poingas -> Bukkit.getLogger().warning("POINGAS  :" + poingas.getName()));
			if (perm.getName().startsWith("-") && perm.getName().endsWith(".*"))
			{
				Bukkit.getPluginManager().getPermissions().stream()
				.filter(p -> p.getName()
						.contains(perm.getName()
								.replace(".*", "")))
				.collect(Collectors.toSet()).addAll(permissions);
				continue;
			}
			
			if (perm.getName().endsWith(".*"))
			{
				Bukkit.getPluginManager().getPermissions().stream()
				.filter(p -> p.getName()
						.contains(perm.getName()
								.replace(".*", "")))
				.collect(Collectors.toSet()).addAll(permissions);
				continue;
			}
			
		}
		//permissions.stream().forEach(poingas2 -> Bukkit.getLogger().warning("POINGAS2  :" + poingas2.getName()));
		return permissions;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		if (pd == null) return;
		
		Roles.recalculate(pd);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		clear(e.getPlayer());
	}

	@Override
	public void reload()
	{
		Bukkit.getLogger().warning("Reloading permissions.yml");
		
		FileConfiguration db = getConfig();
		
		for (Role r : Role.values())
		{
			r.setId(db.getString(r.getName() + ".id"));
			r.setColor(db.getString(r.getName() + ".color"));
			Set<Permission> permissions = includeWildcards(
							db.getStringList(r.getName() + ".permissions")
							.stream()
							.map(p -> new Permission(p))
							.collect(Collectors.toSet())
							);
			
			r.setPermissions(permissions);
			allPermissions.addAll(permissions);
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
