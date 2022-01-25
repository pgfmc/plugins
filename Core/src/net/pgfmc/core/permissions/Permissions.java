package net.pgfmc.core.permissions;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
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
import net.pgfmc.core.permissions.Roles.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Configify;
import net.pgfmc.core.util.Mixins;

public class Permissions extends Configify implements Listener {
	
	private static HashMap<String, PermissionAttachment> permatches = new HashMap<>();
	
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
		if (p.isOnline()) return p.getPlayer().hasPermission(permission);
		
		return Roles.getPermissionsAsString(Roles.getRolesByPlayer(p)).contains(permission);
	}
	
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
		System.out.println("Recalculating permisisons for player " + op.getName());
		
		Set<Role> roles = Roles.getRolesByPlayer(op);
		Set<Permission> perms = Roles.getPermissions(roles);
		
		Player p = op.getPlayer();
		
		if (p == null || !op.isOnline())
		{
			clear(op);
			System.out.println("Updating perms failed, player was offline");
			return;
		}
		
		clear(op);
		
		PermissionAttachment permatch = p.addAttachment(CoreMain.plugin);
		permatches.put(p.getUniqueId().toString(), permatch);
		
		// Bukkit.getPluginManager().getPermissions().forEach(pp -> permatch.unsetPermission(pp));
		
		for (Permission perm : perms)
		{
			System.out.println(perm.getName());
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
			//Bukkit.getPluginManager().getPermissions().stream().forEach(poingas -> System.out.println("POINGAS  :" + poingas.getName()));
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
		permissions.stream().forEach(poingas2 -> System.out.println("POINGAS2  :" + poingas2.getName()));
		return permissions;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Roles.recalculate(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		clear(e.getPlayer());
	}

	@Override
	public void reload()
	{
		System.out.println("Reloading permissions.yml");
		
		FileConfiguration db = getConfig();
		
		for (Role r : Role.values())
		{
			r.setId(db.getString(r.getName() + ".id"));
			r.setColor(db.getString(r.getName() + ".color"));
			r.setPermissions(
					includeWildcards(
							db.getStringList(r.getName() + ".permissions")
							.stream()
							.map(p -> new Permission(p))
							.collect(Collectors.toSet())
							));
		}
		
		PlayerData.getOnlinePlayerData().stream()
		.forEach(pd -> Roles.recalculate(pd));
	}

	@Override
	public void enable() {
		reload();
		
	}

	@Override
	public void disable() {}
}
