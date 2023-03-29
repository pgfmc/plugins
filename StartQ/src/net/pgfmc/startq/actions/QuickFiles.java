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
	
	private final String STARTQ_DIR = Main.SERVER_DIR + File.separator + "startQ";
	private final String SERVER_DIR = Main.SERVER_DIR;

	public QuickFiles()
	{
		System.out.println("Starting QuickFiles...");

		Copy.copyDirectory(new File(STARTQ_DIR), new File(SERVER_DIR));

		resetStartQDirectories();

		System.out.println("Finished QuickFiles!");
	}

	private void resetStartQDirectories()
	{
		System.out.println("Resetting StartQ directories...");
		try {

			// Delete the StartQ directory
			try (Stream<Path> tree = Files.walk(new File(STARTQ_DIR).toPath())) {
			    Iterator<Path> i = tree.iterator();
			    while (i.hasNext()) {
			        i.next().toFile().delete();
			    }
			}

			new File(STARTQ_DIR).mkdirs();
			new File(SERVER_DIR).mkdirs();

			Path sourceDirectory = Paths.get(SERVER_DIR);
			Path destDirectory = Paths.get(STARTQ_DIR);



			try (Stream<Path> tree = Files.walk(sourceDirectory)) {
			    Iterator<Path> i = tree.iterator();
			    while (i.hasNext()) {
			        Path sourceFolder = i.next();
			        Path destFolder = destDirectory.resolve(sourceDirectory.relativize(sourceFolder));
			        try {
				        if (Files.isDirectory(sourceFolder) && sourceFolder.compareTo(Paths.get(STARTQ_DIR)) < 0) {
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
