package net.pgfmc.proxycore.commands;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;

import net.pgfmc.proxycore.Main;

public final class StopProxy implements RawCommand {

	@Override
	public void execute(Invocation invocation) {
		if (!(invocation.source() instanceof Player)) return;
		
		Main.plugin.proxy.shutdown();
		
	}
	
	@Override
	public boolean hasPermission(final Invocation invocation)
	{
		return invocation.source().hasPermission("net.pgfmc.proxycore.stopproxy");
	}

}
