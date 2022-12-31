package net.pgfmc.startq;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import net.pgfmc.startq.actions.QuickFiles;
import net.pgfmc.startq.actions.Update;

public class Main {
	
	public static final String WORKING_DIRECTORY = System.getProperty("user.dir");
	public static String BACKUP_DIRECTORY;
	
	public static void main(String args[]) throws InterruptedException
	{
		checkForBackupDirectory(args);
		checkForServerJar();
		
		System.out.println("Working directory: " + WORKING_DIRECTORY);
		System.out.println("Backup directory: " + BACKUP_DIRECTORY);
		
		//if (!BACKUP_DIRECTORY.equals("null")) new Backup(); // Runs first
		new QuickFiles(); // Runs second
		try { new Update(); } catch (MalformedURLException e) { e.printStackTrace(); } // Runs third
		
		System.out.println("Done! Closing in 5 seconds...");
		TimeUnit.SECONDS.sleep(5);
		
		System.exit(0); // Turns the program off with no error code
		
	}
	
	private static void checkForBackupDirectory(String[] args)
	{
		try {
			if (args.length != 1)
			{
				System.out.println("Backup directory not found!");
				System.out.println("Please only put the backup directory in the JVM Arguments");
				
				System.out.println("Closing in 5 seconds...");
				TimeUnit.SECONDS.sleep(5);
				
				System.exit(1); // Turns the program off with error code 1
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		BACKUP_DIRECTORY = new File(args[0]).getAbsolutePath();

	}
	
	private static void checkForServerJar()
	{
		try {
			// Gets all server jars in the working directory, error if none
			String[] jarsInWorkingDirectory = new File(WORKING_DIRECTORY).list((directory, filename) -> filename.equals("server.jar"));
			
			if (jarsInWorkingDirectory == null)
			{
				System.out.println("No server found!");
				System.out.println("Place StartQ.jar in the working directory of the server.");
				
				System.out.println("Closing in 5 seconds...");
				TimeUnit.SECONDS.sleep(5);
				
				System.exit(2); // Turns the program off with error code 2
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
