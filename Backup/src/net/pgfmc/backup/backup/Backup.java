package net.pgfmc.backup.backup;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.coreprotect.CoreProtectAPI;
import net.pgfmc.backup.Main;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.CoreMain.Machine;
import net.pgfmc.core.util.StringDate;
import net.pgfmc.core.util.Zipper;

/**
 * Backup command
 * @author bk
 */
public class Backup implements CommandExecutor {
	
	// For confirming a backup
	private Map<String, Backup> backups = new HashMap<String, Backup>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.equals("backup"))
		{
			sender.sendMessage("§6Creating a backup will restart the server."
					+ "\nType §f§o/backupconfirm §r§6to backup.");
			
			// For confirming later
			backups.put(sender.getName(), this);
		} else if (label.equals("backupconfirm"))
		{
			try { // lol
				sender.sendMessage("§6Creating backup §f§o" + StringDate.date()
				+ "§r§6.\nThe server will restart once complete.");
				backups.get(sender.getName()).backup();
			} catch (Exception e) {
				sender.sendMessage("Could not find a backup, first do /backup");
			}
		}
		
		
		return true;
	}
	
	/**
	 * Creates the backup with information from the provided Backup object
	 * Will restart the server immediately after saving the backup
	 * 
	 * Time it takes to copy depends on world size
	 * ~5 seconds or less for small world
	 */
	public void backup()
	{
		if (!(CoreMain.machine == Machine.MAIN)) { return; }
		
		System.out.println("Purging data");
		
		// Purge old CoreProtect data
		CoreProtectAPI co = Main.plugin.getCoreProtect();
		if (co != null) { co.performPurge(1209600); } // 14 days in seconds
		
		
		
		/*
		 * Save the server before backing up
		 * Not doing this results in faulty backups
		 */
		Bukkit.getScheduler().callSyncMethod(Main.plugin, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all"));
		
		System.out.println("Creating backup thread.");
		
		/*
		 * Creates a new thread to run this on, makes it so server doesn't crash lol (jk idk how it works)
		 */
		Thread thread = new Thread() {
			public void run() {
				try {
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
					
					// Zipper.zip(CoreMain.pwd, "C:\\Users\\bk\\Desktop\\backupss\\" + StringDate.date() + ".zip");
					Zipper.zip(CoreMain.pwd, CoreMain.backupDir + File.separator + StringDate.date() + ".zip");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Ending backup thread.");
				System.out.println("Restarting server");
				Bukkit.shutdown();
			}
		};
		
		thread.start();
	}
	
}
