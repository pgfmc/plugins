package net.pgfmc.friends;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.friends.commands.FavoriteCommand;
import net.pgfmc.friends.commands.FriendsListCommand;
import net.pgfmc.friends.commands.UnfavoriteCommand;
import net.pgfmc.friends.commands.UnfriendCommand;
import net.pgfmc.friends.data.Friends;
import net.pgfmc.friends.data.Friends.Relation;
import net.pgfmc.friends.events.AttackEventHandler;

public class Main extends JavaPlugin {
	
	public static final String databasePath = "plugins\\Teams\\database.yml";
	
	@Override
	public void onEnable() {
		Mixins.getFile(databasePath);
		
		getCommand("unfriend").setExecutor(new UnfriendCommand());
		getCommand("friendlist").setExecutor(new FriendsListCommand());
		getCommand("favorite").setExecutor(new FavoriteCommand());
		getCommand("unfavorite").setExecutor(new UnfavoriteCommand());
		

		PlayerDataManager.setInit(x -> x.setData("friends", new HashMap<PlayerData, Relation>()));
		PlayerDataManager.setPostLoad((x) -> Friends.load());
		

		getServer().getPluginManager().registerEvents(new AttackEventHandler(), this);
	}
	
	@Override
	public void onDisable() {
		Friends.save();
		
	}
}
