package net.pgfmc.shop.Inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.ListInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.inventoryAPI.extra.SizeData;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.shop.Listing;

public class MainScreen extends ListInventory<Listing> {

    PlayerData pd;


    public MainScreen(PlayerData pd) {
        super(SizeData.BIG , "Market");

        this.pd = pd;
        
        setItem(49, Material.HOPPER);
        setItem(53, Material.PLAYER_HEAD);
        setItem(52, Material.EMERALD);
        setItem(45, Material.TOTEM_OF_UNDYING);
        setItem(46, Material.ANVIL);
        
    }

    @Override
    public List<Listing> load() {
        return Listing.getListings();
    }

	@Override
	protected Butto toAction(Listing arg0, int arg1) {
		
		if (arg0.getPlayer().getUniqueId().equals(pd.getUniqueId())) {
			return (p, e) -> {
				p.openInventory(new ListingBuy(arg0, pd).getInventory());
			};
		} else {
			return (p, e) -> {
				p.openInventory(new ListingBuy(arg0, pd).getInventory());
			};
		}
		
	}

	@Override
	protected ItemStack toItem(Listing arg0) {
		return new ItemWrapper(arg0.getItem().getType()).n("§dCost: " + arg0.getPrice()).gi();
	}    
}