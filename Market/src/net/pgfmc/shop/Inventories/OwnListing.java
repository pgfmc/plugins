package net.pgfmc.shop.Inventories;

import org.bukkit.Material;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.SizeData;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.shop.Listing;

/**
 * @author CrimsonDart
 * @version 4.0.0
 * @since 4.0.0
 */
public class OwnListing extends BaseInventory {
    
    public OwnListing(Listing listing, PlayerData pd) {
        super(SizeData.SMALL, "");
        
        
        setAction(0, (p, e) -> {
            p.openInventory(new MainScreen(pd).getInventory());
        });
        setItem(0 , Material.FEATHER).n("§cBack");
    }
}
