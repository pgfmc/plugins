package net.pgfmc.shop.Inventories;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.SizeData;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.shop.Listing;

public class ListingBuy extends BaseInventory {

    PlayerData player;
    Listing listing;

    public ListingBuy(Listing listing, PlayerData pd) {
        super(SizeData.SMALL, "");
        this.player = pd;
        this.listing = listing;
        
        
        setAction(0, (p, e) -> {
            p.openInventory(new MainScreen(pd).getInventory()); // opens back to Main screen
        });
        setItem(0, Material.FEATHER).n("§cBack");

        removeItem();
    }

    private void removeItem() {
    	
    	setItem(2, Material.GRAY_CONCRETE).n("");
    	setItem(10, Material.GRAY_CONCRETE).n("");
    	setItem(12, Material.GRAY_CONCRETE).n("");
    	setItem(20, Material.GRAY_CONCRETE).n("");
    	
    	// slot in the center off the above slots :)
    	setAction(11, (p, e) -> {
            enterItem(e.getCursor());
        });
    	setItem(11, Material.AIR);
    	
    	setItem(13, listing.getTrade());
    	
    	setItem(6, Material.RED_CONCRETE).n("");
    	setItem(14, Material.RED_CONCRETE).n("");
    	setItem(16, Material.RED_CONCRETE).n("");
    	setItem(24, Material.RED_CONCRETE).n("");
    	
    	setItem(15, listing.getItem().getType()).l("§dPlace " + listing.getPrice() + "\n§dinto the slot to the left\n§dto buy.");
    }

    private void enterItem(ItemStack item) {
    	
    	
    	setAction(11, (p, e) -> {
            e.setCancelled(false);
            removeItem();
        });
    	setItem(11, item);
    	
    	setItem(2, Material.YELLOW_CONCRETE).n("");
    	setItem(10, Material.YELLOW_CONCRETE).n("");
    	setItem(12, Material.YELLOW_CONCRETE).n("");
    	setItem(20, Material.YELLOW_CONCRETE).n("");

    	setItem(6, Material.GREEN_CONCRETE).n("");
    	setItem(14, Material.GREEN_CONCRETE).n("");
    	setItem(16, Material.GREEN_CONCRETE).n("");
    	setItem(24, Material.GREEN_CONCRETE).n("");
    	
    	setAction(15, (p, e) -> {
            e.setCancelled(false);
            buyItem();
        });
    	setItem(15, listing.getItem());
    }

    private void buyItem() {
        listing.deleteListing();
    }
}
