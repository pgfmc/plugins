package net.pgfmc.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileCopy {
	
	public void copy(String sourceDir, String destDir) {				
		try {
			File source = new File(sourceDir);
			File dest = new File(destDir);
			dest.mkdirs();
			
			/*
			 * Copy all files and directories from source to dest
			 * Copied this code from someplace lol
			 */
			Path sourcePath = source.toPath();
			Path destPath = dest.toPath();

			try (Stream<Path> tree = Files.walk(sourcePath)) {
			    Iterator<Path> i = tree.iterator();
			    while (i.hasNext()) {
			        Path sourceTemp = i.next();
			        Path destTemp = destPath.resolve(sourcePath.relativize(sourceTemp));
			        try {
				        if (Files.isDirectory(sourceTemp)) {
				            Files.createDirectories(destTemp);
				        } else {
				            Files.copy(sourceTemp, destTemp);
				        }
			        } catch (IOException e)
			        {
			        	e.printStackTrace();
			        }
			    }
			}
			
			System.out.println("Successfully copied files to \"" + destDir + "\".");
			
		} catch (IOException e) {
			System.out.println("Failed to copy files.");
			e.printStackTrace();
		}
	}
}
