package net.pgfmc.startq.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import net.pgfmc.startq.Main;

public class Update {
	
	private final Map<String, URL> PLUGIN_DOWNLOAD_URLS = new HashMap<>(
			Map.of(   "plugins" + File.separator + "Geyser-Spigot.jar", new URL("https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/lastSuccessfulBuild/artifact/bootstrap/spigot/build/libs/Geyser-Spigot.jar")
					, "plugins" + File.separator + "floodgate-spigot.jar", new URL("https://ci.opencollab.dev/job/GeyserMC/job/Floodgate/job/master/lastSuccessfulBuild/artifact/spigot/build/libs/floodgate-spigot.jar")));
	
	public Update() throws MalformedURLException
	{
		PLUGIN_DOWNLOAD_URLS.forEach((filePathName, url) -> {
			
			try {
				ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
				
				FileOutputStream fileOutputStream = new FileOutputStream(new File(Main.WORKING_DIRECTORY).getAbsolutePath() + File.separator + filePathName);
				FileChannel fileChannel = fileOutputStream.getChannel();
				
				fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				
				fileOutputStream.close();
				
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
			
		});
		
	}

}
