package net.pgfmc.startq.actions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.pgfmc.startq.Main;
import net.pgfmc.startq.util.Zipper;

public class Backup {
	
	private final String PATH_SOURCE = Main.WORKING_DIRECTORY;
	private final String PATH_DESTINATION = Main.BACKUP_DIRECTORY;
	
	// Date format for the archive file name
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, YYYY @ kkmm");
	
	public Backup()
	{
		System.out.println("Starting Backup...");
		System.out.println("Backup directory: " + PATH_DESTINATION);
		
		Zipper.zip(new File(PATH_SOURCE), new File(PATH_DESTINATION), DATE_FORMAT.format(new Date()) + ".zip");
		
		System.out.println("Finished Backup!");
	}

}
