package net.pgfmc.shop.Inventories;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.Butto;
import net.pgfmc.core.inventoryAPI.extra.SizeData;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class CreateListing extends BaseInventory {

    PlayerData pd;
    ItemStack price;

    public CreateListing(PlayerData pd) {
        super(SizeData.SMALL, "Sell Item!");
        this.pd = pd;

        setOpen();
    }

    private void setPrice(ItemStack sell) {

        //buttons = new Button[buttons.length];
        //inv.clear();
    	
    	
    	setAction(0, (p, e) -> {
            p.getInventory().addItem(sell);
            p.openInventory(new MainScreen(pd).getInventory());
        });
    	setItem(0, Material.FEATHER).n("§cBack");
        
        setAction(4, (p, e) -> {
            e.setCancelled(false);
        });
        setItem(4, sell);
        
        setItem(3, Material.GRAY_CONCRETE);
        setItem(5, Material.GRAY_CONCRETE);

        setItem(10, Material.RED_CONCRETE).n("");
        setItem(11, Material.RED_CONCRETE).n("").a(5);
        setItem(12, Material.RED_CONCRETE).n("").a(10);
        
        setAction(13, (p, e) -> {
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                if (price != null) {
                    price.setType(e.getCursor().getType());
                } else {
                    price = e.getCursor().clone();
                }
            }
        });
        setItem(13, Material.AIR);
        
        setItem(14, Material.GREEN_CONCRETE).n("");
        setItem(15, Material.GREEN_CONCRETE).n("").a(5);
        setItem(16, Material.GREEN_CONCRETE).n("").a(10);
        
        setItem(22, Material.GRAY_CONCRETE).n("To set the price of your item, \nput an item in the slot above!");
    }

    private void setOpen() {

        buttons = new Butto[buttons.length];
        getInventory().clear();
        
        
        setAction(0, (p, e) -> {
            p.openInventory(new MainScreen(pd).getInventory());
        });
        setItem(0, Material.FEATHER).n("§cBack");

        setItem(4, Material.GRAY_CONCRETE);
        setItem(12, Material.GRAY_CONCRETE);
        setItem(14, Material.GRAY_CONCRETE);
        setItem(22, Material.GRAY_CONCRETE);

        setAction(13, (p, e) -> {
            setPrice(e.getCursor());
        });
    }
    


    
}
