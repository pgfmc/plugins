package net.pgfmc.core.cmd.test.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.pgfmc.core.api.inventory.BaseInventory;

public class TestInventorySizeInventory extends BaseInventory {

	public TestInventorySizeInventory(int size, String name) {
		super(size, name);
		
		/*
		 * 
		 */
		setItem(16, Material.PAPER)
			.n("List creatable InventoryType");
		
		setAction(16, (player, event) -> {
			
			final StringBuilder builder = new StringBuilder();
			builder.append(ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + "Creatable InventoryType:\n");
			
			for (final InventoryType type : InventoryType.values())
			{
				if (!type.isCreatable()) continue;
				
				final Inventory inventory = Bukkit.createInventory(null, type);
				
				builder.append(type.name() + "; " + inventory.getSize() + " | ");
				
			}
			
			player.sendMessage(ChatColor.LIGHT_PURPLE + builder.toString());
			
		});
		
		/*
		 * 
		 */
		setItem(3, Material.HOPPER)
			.n("Hopper");
		
		setAction(3, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(5, "Hopper").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(4, Material.CHEST)
			.n("Size 9");
		
		setAction(4, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(9, "Size: 9").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(5, Material.CHEST)
			.n("Size 18");
		
		setAction(5, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(18, "Size: 18").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(12, Material.CHEST)
			.n("Size 36");
		
		setAction(12, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(36, "Size: 36").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(13, Material.CHEST)
			.n("Size 45");
		
		setAction(13, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(45, "Size: 45").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(14, Material.CHEST)
		.n("Size 54");
	
		setAction(14, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(54, "Size: 54").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(21, Material.JUKEBOX)
		.n("Jukebox");
	
		setAction(21, (player, event) -> {
			//player.openInventory(new TestAnyInventorySizeInventory(InventoryType.JUKEBOX, "Jukebox").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(22, Material.LECTERN)
			.n("Lectern");
		
		setAction(22, (player, event) -> {
			player.openInventory(new TestAnyInventorySizeInventory(InventoryType.LECTERN, "Lectern").getInventory());
		});
		
		/*
		 * 
		 */
		setItem(23, Material.CHISELED_BOOKSHELF)
		.n("Chiseled Bookshelf");
	
		setAction(23, (player, event) -> {
			//player.openInventory(new TestAnyInventorySizeInventory(InventoryType.CHISELED_BOOKSHELF, "Chiseled Bookshelf").getInventory());
		});
		
	}
	
	

}
