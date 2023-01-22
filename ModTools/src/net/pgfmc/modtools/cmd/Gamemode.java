package net.pgfmc.modtools.cmd;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

 /**
  * Shortened gamemode commands
  * @author bk
  */
public class Gamemode implements CommandExecutor {
	
	private static Map<String, GameMode> mode = Map.of("gmc", GameMode.CREATIVE, "gms", GameMode.SURVIVAL, "gma", GameMode.ADVENTURE, "gmsp", GameMode.SPECTATOR);

	/**
	 * Gamemode commands are /gms /gmc /gma /gmsp
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length != 0)
		{
			Player p2 = Bukkit.getPlayer(args[0]);
			if (p2 != null) { p = p2; } else
			{
				p.sendMessage("§cThat player isn't online.");
				return true;
			}
		}
		
		p.setGameMode(mode.get(label));
		p.sendMessage("§6Gamemode has been set to " + mode.get(label).toString().toLowerCase() + ".");
		
		return true;
	}
	
}
