package net.pgfmc.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;

public class Zipper {
	
	public static void zip(String source, String dest) throws IOException
	{
        Path destPath = Files.createFile(Paths.get(dest));
        Path sourcePath = Paths.get(source);
        
        try (ZipOutputStream zipper = new ZipOutputStream(Files.newOutputStream(destPath)))
        {
        	try(Stream<Path> paths = Files.walk(sourcePath))
        	{
        		paths.filter(path -> !Files.isDirectory(path))
        		.forEach(path -> {
        			ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
        			try {
            			zipper.putNextEntry(zipEntry);
            			Files.copy(path, zipper);
            			zipper.closeEntry();
            		} catch (IOException e) { e.printStackTrace(); }
        		});
        	}
        }

        Bukkit.getLogger().warning("Created zip file: " + destPath);
    }
    
}
