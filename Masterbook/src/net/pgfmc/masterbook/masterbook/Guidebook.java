package net.pgfmc.masterbook.masterbook;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Guidebook {
	
	public static ItemStack completeBook;
	
	
	public static ItemStack getCopmleteBook() {
		
		if (completeBook != null) {
			return completeBook;
		}
		
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
		
		BookMeta bm = (BookMeta) item.getItemMeta();
		
		
		List<String> pages = new ArrayList<>();
		
		pages.add("\n"
				+ "   §2WELCOME TO PGF!!\n"
				+ "\n"
				+ "§rThis book is your guide to how this server works. Please read, we want you to know whats happening!\n"
				+ "\n\n\n\n\n"
				+ "        §8-> Commands list\n"
				+ "");
		pages.add("      §9-COMMANDS-\n"
				+ "\n"
				+ "§3/home §6<name>§7: §0Go to home at §6<name>§0. \n"
				+ "Max: 3 homes\n"
				+ "\n"
				+ "§3/sethome §6<name>§7: §0Set current position as a home as §6<name>§0.\n"
				+ "\n"
				+ "§3/delhome §6<name>§7: §0Deletes home §6<name>§0.\n"
				+ "");
		pages.add("§3/homes§7: §0list all of your homes.\n"
				+ "\n"
				+ "§3/back§7: §0Go back to your last known location.\n"
				+ "\n"
				+ "§3/tpa §6<player>§7: §0Request to teleport to §6<player>§0.\n"
				+ "\n"
				+ "§3/tpaccept§7: §0accept tpa request.");
		pages.add("§3/tpaccept §6<player>§7:\n"
				+ "§0accept §6<player>§0's request.\n"
				+ "\n"
				+ "§3/afk§7: §0go afk.\n"
				+ "\n"
				+ "§3/fr §6<player>§7: §0Send friend request to §6<player>§0.");
		pages.add("§3/fa§7: §0Accept friend request.\n"
				+ "\n"
				+ "§3/uf §6<player>§7: §0Unfriend §6<player>§0.\n"
				+ "\n"
				+ "§3/fav §6<player>§7: §0Favorite §6<player>§0.\n"
				+ "\n"
				+ "§3/unfav §6<player>§7: §0Unfavorite §6<player>§0.\n"
				+ "\n"
				+ "§3/flist§7: §0Open friends list.");
		pages.add("§3/goto §6<world>§7: §0Go to §6<world>§0.\n"
				+ "\n\n\n\n\n\n\n\n\n\n\n"
				+ "                 §7-> Claims");
		pages.add("        §9-CLAIMS-\n"
				+ "\n"
				+ "§0To claim land, place down a §bBeacon§0, or §6Gold Block§0. The §bbeacon§0's range increases with each tier, and the §6Gold block §0has a 7 block radius protection.\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "          §7-> Containers  ");
		pages.add("     §9-CONTAINERS-\n"
				+ "\n"
				+ "§6Chests§0, §6Claims§0, §6Donkeys§0, etc. can be locked! Using a Tripwire Hook, the access to a container can change!   \n"
				+ "Access locks:\n"
				+ "  §6Owner only\n"
				+ "  Favorites Only\n"
				+ "  Friends Only\n"
				+ "  §7Unlocked\n"
				+ "                  -> Duels");
		pages.add("          §9-DUEL-\n"
				+ "\n"
				+ "§3Challenge §0/ §3Accept §0/ Join duel: Hit the player you want to fight with your §bsword§0. \n"
				+ "\n"
				+ "Get ready to fight on the spot!");
		
		bm.setPages(pages);
		
		bm.setAuthor("pgfmc.net");
		bm.setTitle("Guidebook");
		
		item.setItemMeta(bm);
		completeBook = item;
		
		return item;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
