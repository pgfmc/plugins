package net.pgfmc.startq;

import java.io.File;
import java.util.concurrent.TimeUnit;

import net.pgfmc.startq.actions.Backup;
import net.pgfmc.startq.actions.QuickFiles;

public class Main {
	
	public static final String SERVER_DIR = System.getProperty("user.dir");
	public static String BACKUP_DIRECTORY;

	public static void main(String args[]) throws InterruptedException
	{
		checkForBackupDirectory(args);
		checkForServerJar();

		System.out.println("Working directory: " + SERVER_DIR);

		if (!args[0].equals("null")) new Backup(); // Runs first
		new QuickFiles(); // Runs second
		// try { new Update(); } catch (MalformedURLException e) { e.printStackTrace(); } // Runs third TODO Kinda useless right now, but works

		System.out.println("Done!");

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
			String[] jarsInWorkingDirectory = new File(SERVER_DIR).list((directory, filename) -> filename.equals("server.jar"));
			
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
