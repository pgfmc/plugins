package net.pgfmc.bot;

import net.pgfmc.core.file.Configify;

public class DiscordConfig extends Configify {

	@Override
	public void reload() {
		Main.plugin.reloadConfig();
	}

	@Override
	public void enable() {}

	@Override
	public void disable() {}
}
