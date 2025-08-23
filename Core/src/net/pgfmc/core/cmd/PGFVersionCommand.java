package net.pgfmc.core.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.pgfmc.core.CoreMain;

public class PGFVersionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        sender.sendMessage(ChatColor.GOLD + "Getting PGF plugin versions...");
        
        final Plugin[] plugins = CoreMain.plugin.getServer().getPluginManager().getPlugins();
        for (final Plugin plugin : plugins)
        {
            final String name = plugin.getName();
            if (!name.contains("PGF")) continue;

            sender.sendMessage(ChatColor.LIGHT_PURPLE + name + ChatColor.RED + " (v" + plugin.getDescription().getVersion() + ")");
        }

        return true;
    }
    
}
