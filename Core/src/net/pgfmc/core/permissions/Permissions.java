package net.pgfmc.core.permissions;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Permissions {
	
	public static final String[] disabledPerms = {
			"minecraft.command.teammsg"
	};
	
	public static final String[] defaultPerms = {
			"pgf.cmd.back",
			"pgf.cmd.home.*",
			"pgf.cmd.home.homes",
			"pgf.cmd.home.home",
			"pgf.cmd.home.set",
			"pgf.cmd.home.del",
			"pgf.cmd.tp.tpa",
			"pgf.cmd.tp.tpaccept",
			"pgf.cmd.tp.tpdeny",
			"pgf.cmd.afk",
			"pgf.cmd.help",
			"pgf.cmd.link",
			"pgf.cmd.unlink",
			"teams.friend.*",
			"teams.friend.request",
			"teams.friend.accept",
			"teams.friend.unfriend",
			"teams.friend.list",
			"teams.friend.fav",
			"teams.friend.unfav",
			"bukkit.command.seed",
			"bukkit.command.tps", 
			"bukkit.command.list",
			"pgf.dim.survival",
			"ultimatechairs.use",
			"pgf.cmd.warp.warp",
			"pgf.cmd.warp.warps"
	};
	
	public static final String[] donatorPerms = {
			"pgf.cmd.donator.echest",
			"pgf.cmd.donator.nick",
			"pgf.cmd.home.donator",
			"pgf.cmd.donator.craft",
			"ultimatechairs.sit"
	};
	
	public static final String[] veteranPerms = {
			// inherits donatorPerms
	};
	
	public static final String[] modPerms = {
			"pgf.admin.gamemode.creative", 
			"pgf.admin.gamemode.survival", 
			"pgf.admin.gamemode.adventure", 
			"pgf.admin.gamemode.spectator", 
			"bukkit.command.plugins", 
			"bukkit.command.give",
			"bukkit.command.kick",
			"bukkit.command.teleport",
			"bukkit.command.say",
			"bukkit.command.ban.list"
	};
	
	public static final String[] traineePerms = {
			"bukkit.command.kick",
			"bukkit.command.teleport",
			"bukkit.command.say",
			"bukkit.command.ban.list"
	};
	
	public static final String[] devPerms = {
			"pgf.admin.toggledim",
			"pgf.admin.backup",
			"pgf.admin.restore",
			"pgf.admin.debug"
	};
	
	public static final String[] adminPerms = {
			"pgf.admin.vanish", 
			"pgf.cmd.fly",
			"pgf.admin.god",
			"pgf.admin.sudo",
			"pgf.cmd.heal",
			"pgf.admin.day",
			"pgf.admin.skull",
			"pgf.admin.pgf",
			"pgf.cmd.warp.set",
			"pgf.cmd.warp.del",
			"pgf.admin.tag",
			"pgf.admin.broadcast",
			"bukkit.command.restart",
			"bukkit.commands.timings",
			"bukkit.command.reload",
			"bukkit.command.ban.ip",
			"bukkit.command.ban.player",
			"bukkit.command.clear",
			"bukkit.command.defaultgamemode",
			"bukkit.command.difficulty",
			"bukkit.command.effect",
			"bukkit.command.enchant",
			"bukkit.command.gamemode",
			"bukkit.command.gamerule",
			"bukkit.command.kill",
			"bukkit.command.unban.player",
			"bukkit.command.unban.ip",
			"bukkit.command.save.perform",
			"bukkit.command.save.off",
			"bukkit.command.save.on",
			"bukkit.command.scoreboard",
			"bukkit.command.spawnpoint",
			"bukkit.command.stop",
			"bukkit.command.time.set",
			"bukkit.command.time.add",
			"bukkit.command.toggledownfall",
			"bukkit.command.weather",
			"bukkit.command.whitelist.add",
			"bukkit.command.whitelist.remove",
			"bukkit.command.whitelist.list",
			"bukkit.command.whitelist.enable",
			"bukkit.command.whitelist.disable",
			"bukkit.command.whitelist.reload",
			"bukkit.command.xp"
	};
	
	public static void recalcPerms(PlayerData pd) {
		
		List<Role> r = pd.getData("Roles");
		
		Player p = pd.getPlayer();
		
		if (p == null)
		{
			System.out.println("Updating perms failed, player was offline");
			return;
		}
			
		PermissionAttachment permatch = p.addAttachment(CoreMain.plugin);
		
		if (p.isOp())
		{
			for (String s : defaultPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : disabledPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : veteranPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : modPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : devPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : adminPerms) {
				permatch.setPermission(s, true);
			} 
		} else if (r != null) {
				
				for (String s : defaultPerms) {
					permatch.setPermission(s, true);
				}
				
				for (String s : disabledPerms) {
					permatch.setPermission(s, false);
				}
				
				for (String s : donatorPerms) {
					permatch.setPermission(s, r.contains(Role.VETERAN) || r.contains(Role.DONATOR));
				}
				
				for (String s : modPerms) {
					permatch.setPermission(s, r.contains(Role.MODERATOR));
				}
				
				for (String s : devPerms) {
					permatch.setPermission(s, r.contains(Role.DEVELOPER));
				}
				
				for (String s : adminPerms) {
					permatch.setPermission(s, r.contains(Role.ADMIN));
				}
			} else if (r == null) {
				for (String s : defaultPerms) {
					permatch.setPermission(s, true);
				}
				
				for (String s : disabledPerms) {
					permatch.setPermission(s, false);
				}
				
				for (String s : veteranPerms) {
					permatch.setPermission(s, false);
				}
				
				for (String s : modPerms) {
					permatch.setPermission(s, false);
				}
				
				for (String s : devPerms) {
					permatch.setPermission(s, false);
				}
				
				for (String s : adminPerms) {
					permatch.setPermission(s, false);
				}
			}
		
		p.recalculatePermissions();
		p.updateCommands();
	}
}
