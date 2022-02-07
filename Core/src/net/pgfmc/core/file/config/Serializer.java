package net.pgfmc.core.file.config;

import org.bukkit.configuration.ConfigurationSection;

@Deprecated	
public interface Serializer<T> {
	
	public ConfigurationSection toFileFormat(T data);
	public T fromFileFormat(ConfigurationSection data);
}
