package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

public interface Mixins {
	
	/**
	 * Gets the file at dir or returns a newly created file
	 * 
	 * @param dir The directory of the file
	 * @return The file at dir or a newly created file
	 */
	public static File getFile(final Path path) {
		final File file = path.toFile();
		
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
		
		if (file.exists()) return file;
		
		Logger.debug("Attempting to create file at " + path);
		
		try {
			file.createNewFile();
			return file;
			
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error("File at " + path + " couldnt be created!");
		}
		
		return null;
	}
	
	public static void writeKeyValueToToml(final File file, final Map<String, Object> content)
	{
		final Toml toml = new Toml()
				.read(file);
		
		final Map<String, Object> map = toml.toMap();
		map.putAll(content);
		
		final TomlWriter writer = new TomlWriter();
		
		try {
			writer.write(map, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writeKeyValueToToml(final Path path, final Map<String, Object> content)
	{
		final File file = getFile(path);
		writeKeyValueToToml(file, content);
		
	}
	
}
