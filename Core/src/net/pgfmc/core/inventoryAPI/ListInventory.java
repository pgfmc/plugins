package net.pgfmc.core.inventoryAPI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.SizeData;

public abstract class ListInventory<T> extends BaseInventory {
	
	/**
	 * The List of entries, in the form of Button(s).
	 */
	private T[][] pages;
	
	/**
	 * holds the current page.
	 */
	transient int page = 1;
	
	/**
	 * Abstract method used to get the list used to construct this Inventory.
	 * @return The list used in this Inventory.
	 */
	protected abstract List<T> load();
	
	/**
	 * Method to be implemented by the extender of this class.
	 * The input is an entry from the list returned from load();
	 * This method must return a Butto lambda function reflecting the 
	 * input value.
	 * @param entry The input value from load().
	 * @param slot The slot this entry is in.
	 * @return The Butto lambda function ran when this entry is pressed in-inventory.
	 */
	protected abstract Butto toAction(T entry);
	
	/**
	 * Translates an entry in the list returned by load() to an ItemStack.
	 * The ItemStack that is returned should represent/reflect the input entry.
	 * @param entry The object to be translated into an ItemStack.
	 * @return The Item used to represent the input entry in-inventory.
	 */
	protected abstract ItemStack toItem(T entry);
	
	/**
	 * Constructor for PagedInventory. The inventory can be in two sizes: 27 or 56 (single or double chest). 
	 * The method {@code setEntries()} is the method used to load entries.
	 * @param size The size of the inventory. can only be 27 or 56.
	 * @param name Name displayed at the top of the inventory's interface.
	 * @param itemController The function that is ran per entry in "entries"; itemController must return a button Object, with the entry itself as the input.
	 */
	public ListInventory(SizeData size, String name) {
		super(size, name);
		
		if (size == SizeData.DROPPER || size == SizeData.HOPPER) {
			throw new IllegalArgumentException();
		}
		
		refresh();
	}
	
	/**
	 * reloads the list that this ListInventory is based on.
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		List<T> entries = load();
		
		pages = (T[][]) new Object[(int) Math.ceil(entries.size() / (float) sizeD.getPageSize())][sizeD.getPageSize()];
		
		for (int i = 0;
				i < entries.size();
				i++	) {
			pages[i / sizeD.getPageSize()][i % sizeD.getPageSize()] = entries.get(i);
		}
		
		setPage(page);
	}
	
	/**
	 * Input the entry slot number, and the function will return the appropriate inventory slot number (only for SMALL inventories)
	 * @param index The index of the item in the given page.
	 * @return Returns the Respective slot for the given "index" in a 27 slot inventory.
	 */
	private int entryToSlot(int index) {
		
		if (index >= 0 && index < 7) {
			return index + 2;
		} else if (index >= 7 && index < 14) {
			return index + 4;
		} else if (index >= 14 && index < 21) {
			return index + 6;
		} else {
			new Exception("input \"index\" is out of bounds!");
			return -1;
		}
	}
	
	public void flipPage(int flips) {
		if (flips + page > 0 && flips + page <= pages.length) {
			page = page + flips;
			setPage(page);
		}
	}
	
	/**
	 * Manages all page turning. 
	 * @param page The page that the inventory will be set to.
	 */
	private void setPage(int newPage) {
		
		if (newPage > pages.length || newPage < 1) return;
		
		//buttons = new Button[sizeD.getSize()];
		//inv.clear();
		
		page = newPage;
		
		if (sizeD == SizeData.BIG) {
			
			// sets the Previous page button, if apropriate.
			if (page > 1) {
				setAction(48, (x, e) -> {
					flipPage(-1);
				});
				
				setItem(48, Material.IRON_HOE).n("Previous Page");
				
				
			} else {
				setItem(48, Material.AIR);
				setAction(48, null);
			}
			
			// sets the next page button, if apropriate.
			if (page < pages.length) {
				
				setAction(50, (x, e) -> {
					flipPage(+1);
				});
				
				setItem(50, Material.ARROW).n("Next Page");
				
			} else {
				setItem(50, Material.AIR);
				setAction(50, null);
			}
			
			T[] currentPage = pages[page -1];
			for (int i = 0;
					i < 36;
					i++) {
				int enty = entryToSlot(i);
				if (currentPage[i] == null) continue;
				
				setAction(enty, toAction(currentPage[i]));
				setItem(enty, toItem(currentPage[i]));
			}
			
		} else if (sizeD == SizeData.SMALL) {
			
			// sets the Previous page button, if apropriate.
			if (page > 1) {
				
				setAction(9, (x, e) -> {
					flipPage(-1);
				});
				
				setItem(9, Material.IRON_HOE).n("Previous Page");
				
			} else {
				setItem(9, Material.AIR);
				setAction(9, null);
			}
			
			// sets the next page button, if apropriate.
			if (page < pages.length) {
				
				setAction(18, (x, e) -> {
					flipPage(+1);
				});
				
				setItem(18, Material.ARROW).n("Next Page");
				
				
			} else {
				setItem(18, Material.AIR);
				setAction(18, null);
			}
			
			T[] currentPage = pages[page -1];
			for (int i = 0;
					i < 21;
					i++) {
				int enty = entryToSlot(i);
				if (currentPage[i] == null) continue;
				
				setAction(enty, toAction(currentPage[i]));
				setItem(enty, toItem(currentPage[i]));
			}
		}
	}
}