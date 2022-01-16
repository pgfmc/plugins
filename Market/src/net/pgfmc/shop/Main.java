package net.pgfmc.shop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.shop.commands.Shop;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		// ---------------------------------------------------- File creation thingy ( for storing data )
		
		File file = new File(getDataFolder().toString()); // Creates a File object
		
		if (!file.exists()) {
			file.mkdir();
		}
		
		file = new File(getDataFolder() + File.separator); // Creates a File object
		
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		file = new File(getDataFolder() + File.separator + "playerdata.yml"); // Creates a File object
		
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// ------------------------------------------------------ Commands And Events
		
		this.getCommand("market").setExecutor(new Shop());
		
		// XXX Listing.loadListings();
	}
	
	// functions used all around the place in this pluign :)
	
	public static ItemStack createItem(String name, Material mat) // function for creating an item with a custom name
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack switchLore(ItemStack item, List<String> lore) { // changes the lore of an item
		
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static String makePlural(ItemStack itemStack) { // takes an item, and then represents it in a string || as in: "1 diamond" or "4 Dark Prismarine Blocks" || automatically 
		
		boolean iP = true;
		
		String name = Main.getName(itemStack.getType()); // --------- plural stuff
		if (itemStack.getAmount() == 1) {
			return("�b" + String.valueOf(itemStack.getAmount()) + " " + name);
		} else {
			
			switch(itemStack.getType()) { // Switch for special case plurals.
			
			case GRANITE: iP = false;
			case POLISHED_GRANITE: iP = false;
			case DIORITE: iP = false;
			case POLISHED_DIORITE: iP = false;
			case ANDESITE: iP = false;
			case POLISHED_ANDESITE: iP = false;
			case DIRT: iP = false;
			case COARSE_DIRT: iP = false;
			case PODZOL: iP = false;
			case CRIMSON_NYLIUM: iP = false;
			case WARPED_NYLIUM: iP = false;
			case BEDROCK: iP = false;
			case SAND: iP = false;
			case RED_SAND: iP = false;
			case GRAVEL: iP = false;
			case STRIPPED_CRIMSON_HYPHAE: iP = false;
			case STRIPPED_WARPED_HYPHAE: iP = false;
			case OAK_WOOD: iP = false;
			case CRIMSON_HYPHAE: iP = false;
			case WARPED_HYPHAE: iP = false;
			case GRASS: iP = false;
			case SEAGRASS: iP = false;
			case LILY_OF_THE_VALLEY: return("Lilies of the Valley");
			case CRIMSON_FUNGUS: return("Crimson Fungi");
			case WARPED_FUNGUS: return("Warped Fungi");
			case CRIMSON_ROOTS: iP = false;
			case WARPED_ROOTS: iP = false;
			case NETHER_SPROUTS: iP = false;
			case WEEPING_VINES: iP = false;
			case TWISTING_VINES: iP = false;
			case KELP: iP = false;
			case BAMBOO: iP = false;
			case GOLD_BLOCK: return("Blocks of Gold");
			case IRON_BLOCK: return("Blocks of Iron");
			case TNT: iP = false;
			case BRICKS: iP = false;
			case BOOKSHELF: return("Bookshelves");
			case OBSIDIAN: iP = false;
			case TORCH: return("Torches");
			case DIAMOND_BLOCK: return("Blocks of Diamond");
			case FARMLAND: iP = false;
			case REDSTONE_TORCH: return("Redstone Torches");
			case SNOW: iP = false;
			case CACTUS: return("Cacti");
			case CLAY: iP = false;
			case NETHERRACK: iP = false;
			case SOUL_SAND: iP = false;
			case SOUL_SOIL: iP = false;
			case BASALT: iP = false;
			case POLISHED_BASALT: iP = false;
			case SOUL_TORCH: return("Soul Torches");
			case IRON_BARS: iP = false;
			case VINE: iP = false;
			case MYCELIUM: iP = false;
			case EMERALD_BLOCK: return("Blocks of Emerald");
			case QUARTZ_BLOCK: return("Blocks of Quartz");
			case COAL_BLOCK: return("Blocks of Coal");
			case PRISMARINE: iP = false;
			case DARK_PRISMARINE: iP = false;
			case SCAFFOLDING: iP = false;
			case FLINT_AND_STEEL: iP = false;
			case GUNPOWDER: iP = false;
			case WHEAT_SEEDS: iP = false;
			case WHEAT: iP = false;
			case BREAD: iP = false;
			case LEATHER_LEGGINGS: iP = false;
			case FLINT: iP = false;
			case REDSTONE: iP = false;
			case LEATHER: iP = false;
			case CHEST_MINECART: return "Minecarts with Chests";
			case FURNACE_MINECART: return "Minecarts with Furnaces";
			case GLOWSTONE_DUST: iP = false;
			case COCOA_BEANS: iP = false;
			case LAPIS_LAZULI: iP = false;
			case BONE_MEAL: iP = false;
			case SUGAR: iP = false;
			case SHEARS: iP = false;
			case BLAZE_POWDER: iP = false;
			case ENDER_EYE: return "Eyes of Ender";
			case EXPERIENCE_BOTTLE: return "Bottles O' Enchanting";
			case CARROT_ON_A_STICK: return "Carrots on a Stick";
			case WARPED_FUNGUS_ON_A_STICK: return "Warped Fungi on a Stick";
			case QUARTZ: iP = false;
			case TNT_MINECART: return "Minecarts with TNT";
			case HOPPER_MINECART: return "Minecarts with Hoppers";
			case PRISMARINE_CRYSTALS: iP = false;
			case RABBIT_FOOT: return "Rabbit's Feet";
			case RABBIT: iP = false;
			case COOKED_RABBIT: iP = false;
			case BEETROOT: iP = false;
			case BEETROOT_SEEDS: iP = false;
			case BEETROOT_SOUP: iP = false;
			case DRAGON_BREATH: iP = false;
			case ELYTRA: iP = false;
			case TOTEM_OF_UNDYING: return "Totems of Undying";
			case HEART_OF_THE_SEA: return "Hearts of the Sea";
			case SWEET_BERRIES: iP = false;
			case HONEYCOMB: iP = false;
			case NETHERITE_BLOCK: return "Blocks of Netherite";
			case ANCIENT_DEBRIS: iP = false;
			case CRYING_OBSIDIAN: iP = false;
			case ROTTEN_FLESH: iP = false;
			
			default: break;
			}
			
			if (iP == true) {
				
				 // Plural Exemptions
				if (name.endsWith("Wood") || name.endsWith("Planks") || name.endsWith("Wool") || name.endsWith("stone") || name.endsWith("Stone") || name.endsWith("Leaves") || name.endsWith("Slab") || name.endsWith("Stair") || name.endsWith("Bricks") || name.endsWith("Terracotta") || name.endsWith("Carpet") || name.endsWith("Glass") || name.endsWith("Ice") || name.endsWith("Concrete") || name.endsWith("Powder") || name.endsWith("Coral") || name.endsWith("Leggings") || name.endsWith("Boots") || name.endsWith("Cod") || name.endsWith("Salmon") || name.endsWith("Fish") || name.endsWith("Dye") || name.endsWith("Mutton") || name.endsWith("Armor") || name.endsWith("Fruit")) {
					return(name);
				}
				if (name.endsWith("s") || name.endsWith("x") || name.endsWith("sh")) {
					return("�b" + String.valueOf(itemStack.getAmount()) + " " + name + "es");
				} else if (name.endsWith("y")){
					return("�b" + String.valueOf(itemStack.getAmount()) + " " + name.substring(0, name.length() - 2) + "ies");
				} else if (name.startsWith("Arrow of")) {
					return("�b" + String.valueOf(itemStack.getAmount()) + " Arrows of" + name.substring(8, name.length() - 1));
				} else if (name.startsWith("Potion of")) {
					return("�b" + String.valueOf(itemStack.getAmount()) + " Potions of" + name.substring(9, name.length() - 1));
				} else if (name.startsWith("Splash Potion of")) {
					return("�b" + String.valueOf(itemStack.getAmount()) + " Splash Potions of" + name.substring(15, name.length() - 1));
				} else if (name.startsWith("Lingering Potion of")) {
					return("�b" + String.valueOf(itemStack.getAmount()) + " Lingering Potions of" + name.substring(18, name.length() - 1));
				} else {
					return("�b" + String.valueOf(itemStack.getAmount()) + " " + name + "s");
				}
			}
		}
		System.out.println("Plural could not be found for item " + name);
		return name;
	}
	
	public static String getName(Material material) { // gets the singular name from the material type
		
		switch(material) {
		
		case CHEST_MINECART: return "Minecart with Chest";
		case CHIPPED_ANVIL: return "Slightly Damaged Anvil";
		case CLAY_BALL: return "Clay";
		case COMPARATOR: return "Redstone Comparator";
		case DAMAGED_ANVIL: return "Very Damaged Anvil";
		case EXPERIENCE_BOTTLE: return "Bottle O' Enchanting";
		case FILLED_MAP: return "Map";
		case FURNACE_MINECART: return "Minecart with Furnace";
		case GLISTERING_MELON_SLICE: return "Glistering Melon";
		case HONEYCOMB_BLOCK: return "Block of Honey";
		case MELON_SLICE: return "Melon";
		case RABBIT_FOOT: return "Rabbit's Foot";
		case REPEATER: return "Redstone Repeater";
		case LEATHER_LEGGINGS: return "Leather Pants";
		case LEATHER_HELMET: return "Leather Cap";
		case LEATHER_CHESTPLATE: return "Leather Tunic";
		case ENDER_EYE: return "Eye of Ender";
		case TNT_MINECART: return "Minecart with TNT";
		case HOPPER_MINECART: return "Minecart with Hopper";
		
		default: 	String name = material.name();

					if (name.contains("MUSIC_DISC_")) {
						name = name.replaceFirst("MUSIC_DISC_", "");
					}
		
					name = name.toLowerCase();
					name = name.replace("_", " ");
					String[] list = name.split(" ");
					
					name = "";
					for (String string : list) {
						
						char[] charArray = string.toCharArray();
						charArray[0] = Character.toUpperCase(charArray[0]);
						name = name + new String(charArray) + " ";
					}
					name = name.stripTrailing();
					return name;
		}
	}
}
