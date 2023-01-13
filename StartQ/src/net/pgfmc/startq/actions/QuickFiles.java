package net.pgfmc.startq.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import net.pgfmc.startq.Main;
import net.pgfmc.startq.util.Copy;

public class QuickFiles {
	
	private final String PATH_SOURCE = Main.WORKING_DIRECTORY + File.separator + "startQ";
	private final String PATH_DESTINATION = Main.WORKING_DIRECTORY;
	
	public QuickFiles()
	{
		System.out.println("Starting QuickFiles...");
		
		Copy.copyDirectory(new File(PATH_SOURCE), new File(PATH_DESTINATION));
		
		resetStartQDirectories();
		
		System.out.println("Finished QuickFiles!");
	}
	
	private void resetStartQDirectories()
	{
		System.out.println("Resetting StartQ directories...");
		try {
			
			// Delete the StartQ directory
			try (Stream<Path> tree = Files.walk(new File(PATH_SOURCE).toPath())) {
			    Iterator<Path> i = tree.iterator();
			    while (i.hasNext()) {
			        i.next().toFile().delete();
			    }
			}
			
			new File(PATH_SOURCE).mkdirs();
			new File(PATH_DESTINATION).mkdirs();
			
			Path sourceDirectory = Paths.get(PATH_DESTINATION);
			Path destDirectory = Paths.get(PATH_SOURCE);
			
			
			
			try (Stream<Path> tree = Files.walk(sourceDirectory)) {
			    Iterator<Path> i = tree.iterator();
			    while (i.hasNext()) {
			        Path sourceFolder = i.next();
			        Path destFolder = destDirectory.resolve(sourceDirectory.relativize(sourceFolder));
			        try {
				        if (Files.isDirectory(sourceFolder) && sourceFolder.compareTo(Paths.get(PATH_SOURCE)) < 0) {
				            Files.createDirectories(destFolder);
				        }
			        } catch (IOException e)
			        {
			        	e.printStackTrace();
			        }
			    }
			}
			
			System.out.println("Done resetting StartQ directories!");
			
		} catch (IOException e) {
			System.out.println("Hit a roadblock; could not copy files.");
			e.printStackTrace();
		}
	}
	
}
