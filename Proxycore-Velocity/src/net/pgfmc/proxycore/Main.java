package net.pgfmc.proxycore;

import java.io.File;
import java.nio.file.Path;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.pgfmc.proxycore.bot.Bot;
import net.pgfmc.proxycore.commands.LinkCommand;
import net.pgfmc.proxycore.commands.StopProxyCommand;
import net.pgfmc.proxycore.commands.UnlinkCommand;
import net.pgfmc.proxycore.listeners.types.Connect;
import net.pgfmc.proxycore.listeners.types.DiscordMessage;
import net.pgfmc.proxycore.listeners.types.PingServer;
import net.pgfmc.proxycore.listeners.velocity.OnDisconnect;
import net.pgfmc.proxycore.listeners.velocity.OnPlayerChat;
import net.pgfmc.proxycore.listeners.velocity.OnPostLogin;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

@Plugin(id = "pgf", name = "Proxycore", version = "0.0.0",
        url = "https://www.pgfmc.net", description = "Core functionality for the proxy", authors = {"PGF"})
/**
 * This is a Velocity plugin (NOT a Minecraft JavaPlugin). It runs on the Velocity proxy.
 * 
 * It can send and receive Plugin Messages (packets). This can be used to communicate
 * things from one server to another like:
 * chat messages, command executions, or connecting a player to a server.
 * 
 * This plugin listens for and sends packets/plugin messages on the pgf:main Channel Identifier.
 */
public class Main {
	
	/**
	 * The Channel Identifier for this Velocity plugin.
	 */
	public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("pgf:main");
	
	public static Main plugin;
	public static LuckPerms lp;
	
    public final ProxyServer proxy;
    public final org.slf4j.Logger logger;
    public final Path dataDirectory;
    public final Path configDirectory;
    
    /**
     * Automatically injects the ProxyServer, Logger, and Path into the constructor.
     * 
     * @param proxy
     * @param logger
     * @param dataDirectory
     */
    @Inject
    public Main(ProxyServer proxy, org.slf4j.Logger logger, @DataDirectory Path dataDirectory) {
    	plugin = this;
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.configDirectory = Path.of("plugins" + File.separator + "PGF-Proxycore");
        
    }
    
    /**
     * The listener for when the proxy initializes.
     * 
     * @param event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
    	Logger.log("=============");
    	Logger.log("  Proxycore  ");
    	Logger.log("  -- PGF --  ");
    	Logger.log("=============");
    	
    	/**
		 * LuckPerms API
		 */
		
		final LuckPerms luckPermsApi = LuckPermsProvider.get();
		lp = luckPermsApi;
    	
    	/**
    	 * Initialize classes
    	 */
    	new Bot();
    	
    	/**
    	 * Register listeners
    	 */
    	proxy.getChannelRegistrar().register(IDENTIFIER);
    	new RoleManager(PluginMessageType.GET_PLAYER_ROLE, luckPermsApi);
    	proxy.getEventManager().register(this, new OnPlayerChat());
    	proxy.getEventManager().register(this, new OnPostLogin());
    	proxy.getEventManager().register(this, new OnDisconnect());
    	new Connect(PluginMessageType.CONNECT);
    	new PingServer(PluginMessageType.PING_SERVER);
    	new DiscordMessage(PluginMessageType.DISCORD_MESSAGE);
    	
    	/**
    	 * Register Commands
    	 */
    	final CommandManager manager = proxy.getCommandManager();
    	
    	/**
    	 * stopproxy
    	 */
    	final CommandMeta stopproxyMeta = manager.metaBuilder("stopproxy")
    			.plugin(this)
    			.build();
    	
    	final RawCommand stopproxyCommand = new StopProxyCommand();
    	
    	manager.register(stopproxyMeta, stopproxyCommand);
    	//
    	
    	/**
    	 * link
    	 */
    	final CommandMeta linkMeta = manager.metaBuilder("link")
    			.plugin(this)
    			.build();
    	
    	final RawCommand linkCommand = new LinkCommand();
    	
    	manager.register(linkMeta, linkCommand);
    	//
    	
    	/**
    	 * unlink
    	 */
    	final CommandMeta unlinkMeta = manager.metaBuilder("unlink")
    			.plugin(this)
    			.build();
    	
    	final RawCommand unlinkCommand = new UnlinkCommand();
    	
    	manager.register(unlinkMeta, unlinkCommand);
    	//
    	
    }
    
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event)
    {
    	Bot.shutdown();
    }
    
}
