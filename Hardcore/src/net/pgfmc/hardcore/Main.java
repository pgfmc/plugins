package net.pgfmc.hardcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.util.Logger;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;

        if (Bukkit.getPluginManager().getPlugin("PGF-Survival") != null) {
            Logger.log("PGF-Survival is installed, features from PGF-Hardcore are disabled.");

            return;
        }

        Data.loadGifts();
        new RegisterGift();
	}
	
	@Override
	public void onDisable() {
        Data.saveGifts();
	}
}
