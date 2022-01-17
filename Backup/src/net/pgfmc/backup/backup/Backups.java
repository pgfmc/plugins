package net.pgfmc.backup.backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.coreprotect.CoreProtectAPI;
import net.pgfmc.backup.Main;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.Mixins;

public class Backups {
	
	/**
	 * Creates the backup with information from the provided Backup object
	 * Will restart the server immediately after saving the backup
	 * 
	 * Time it takes to copy depends on world size
	 * ~10 seconds for small world
	 * 
	 * @param b A Backup object
	 */
	public static void backup(Backup b)
	{
		// if (!(CoreMain.machine == Machine.MAIN)) { return; }
		
		System.out.println("Purging");
		// Purge old CoreProtect data
		CoreProtectAPI co = Main.plugin.getCoreProtect();
		if (co != null) { co.performPurge(1209600); } // 14 days in seconds
		
		System.out.println("Purging 2.0");
		// Remove log files
		File logs = new File(CoreMain.pwd + File.separator + "logs");
		for(File f: logs.listFiles())
		{
			if (!f.isDirectory() && f.lastModified() < new Date().getTime() - 172800000L) // Checks if older than 2 days in milliseconds
			{
				System.out.println("Deleting log: " + f.getName());
				f.delete();
			} else
			{
				System.out.println("Saving log: " + f.getName());
			}
		}
		
		System.out.println("Creating thread.");
		
		/*
		 * Save the server before backing up
		 * Not doing this results in faulty backups
		 */
		Bukkit.getScheduler().callSyncMethod(Main.plugin, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all"));
		
		/*
		 * Creates a new thread to run this on, makes it so server doesn't crash lol (jk idk how it works)
		 */
		Thread thread = new Thread() {
			public void run() {				
				try {
					String sourceDir = CoreMain.pwd;
					String destDir = "C:\\Users\\bk\\Desktop\\BACKUPS";//CoreMain.backupDir + b.backup.get("date") + File.separator;
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
					
					/*
					 * Uses info from the Backup object to create a YML with
					 * helpful information
					 */
					FileConfiguration info = Mixins.getDatabase(destDir + "info.yml");
					info.set("info", b.backup.toString());
					
					Mixins.saveDatabase(info, destDir + "info.yml");
					
					System.out.println("Successfully created backup at \"" + destDir + "\"."
							+ "\nRestarting the server now.");
					
					/*
					 * Shutdown the server
					 * Does not shutdown if the backup fails
					 */
					Bukkit.getServer().shutdown();
					
				} catch (IOException e) {
					System.out.println("Failed to create backup, some files may have copied over.");
					b.sender.sendMessage("§cFailed to create backup, some files may have copied over."
							+ "\nNot restarting the server.");
					e.printStackTrace();
				}
				
				System.out.println("Thread has ended.");
			}
			
		};
		
		
		thread.start();
	}
	
	public static void restore()
	{
		// TODO
	}

}
