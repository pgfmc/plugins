package net.pgfmc.startq.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
	
	// Takes in a source directory as a file and a destination directory as a file
	public static void zip(File sourceDirectory, File destDirectory, String zipFileName)
	{
		System.out.println("Starting zip operation: " + sourceDirectory.getAbsolutePath() + " to " + destDirectory.getAbsolutePath() + File.separator + zipFileName + " ...");
		
		// Makes the directories in case they don't exist
		sourceDirectory.mkdirs();
		destDirectory.mkdirs();
		
		// Makes a new ZipOutputStream by taking in a FileOutputStream of the file of the zip archive (path + filename.zip)
        try (ZipOutputStream zipper = new ZipOutputStream(Files.newOutputStream(Paths.get(destDirectory.getAbsolutePath() + File.separator + zipFileName))))
        {
        	// Streams all the Path in the source directory
        	try(Stream<Path> paths = Files.walk(sourceDirectory.toPath()))
        	{
        		// filter out anything that isn't a file (paths are auto generated when copying)
        		paths.filter(path -> !Files.isDirectory(path))
        		.forEach(path -> { // for the Path for every file in source directory
        			
        			// Make a new ZipEntry of the [[path + filename.*]] (path must be to a file)
        			ZipEntry zipEntry = new ZipEntry(sourceDirectory.toPath().relativize(path).toString());
        			
        			try {
        				// Put the ZipEntry we made to the ZipOutputStream so we can write to it (currently no data in the ZipEntry other than the file name and directory)
            			zipper.putNextEntry(zipEntry);
            			// Read all bytes from the current Path file, then write to the ZipOutputStream (which is currently outputting to the above ZipEntry)
            			Files.copy(path, zipper);
            			
            			// Must close the current ZipEntry to save/confirm the data
            			zipper.closeEntry();
            		} catch (IOException e) { e.printStackTrace(); }
        		});
        		
        	}
        	
        	// Must close the ZipOutputStream to save/confirm the data
        	zipper.close();
        } catch (IOException e1) {
        	
        	System.out.println("Hit a roadblock; could not zip files.");
        	
			e1.printStackTrace();
		}
        
        System.out.println("Finished zip operation!");
        
    }
    
}
