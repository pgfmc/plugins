package net.pgfmc.shop.Inventories;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
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
    	
    	setAction(0, (p, e) -> {
            p.getInventory().addItem(sell);
            p.openInventory(new MainScreen(pd).getInventory());
        });
    	setItem(0, Material.FEATHER).n("§cBack");
        
        setAction(4, (p, e) -> {
        	p.getInventory().addItem(inv.getItem(4));
        	setOpen();
        	price = null;
        });
        setItem(4, sell);
        
        setItem(3, Material.GRAY_CONCRETE);
        setItem(5, Material.GRAY_CONCRETE);
        
        setAction(10, priceAmount(-10));
        setAction(11, priceAmount(-5));
        setAction(12, priceAmount(-1));
        
        
        
        setItem(10, Material.RED_CONCRETE).n(" ").a(10);
        setItem(11, Material.RED_CONCRETE).n(" ").a(5);
        setItem(12, Material.RED_CONCRETE).n(" ");
        
        setAction(13, (p, e) -> {
            if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                if (price != null) {
                    price.setType(e.getCursor().getType());
                } else {
                    price = new ItemStack(e.getCursor().getType());
                }
                setItem(13, price.getType());
            }
        });
        setItem(13, Material.AIR);
        
        setAction(14, priceAmount(1));
        setAction(15, priceAmount(5));
        setAction(16, priceAmount(10));
        
        setItem(14, Material.GREEN_CONCRETE).n(" ");
        setItem(15, Material.GREEN_CONCRETE).n(" ").a(5);
        setItem(16, Material.GREEN_CONCRETE).n(" ").a(10);
        
        setItem(22, Material.GRAY_CONCRETE).n("§7To set the price of your item,").l("§r§7put an item in the slot above!");
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
        	
        	if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) return;
        	
        	if (e.getAction() == InventoryAction.PLACE_ALL) {
        		setPrice(e.getCursor());
        		p.setItemOnCursor(new ItemStack(Material.AIR));
        	} else if (e.getAction() == InventoryAction.PLACE_ONE) {
        		ItemStack pi = e.getCursor();
        		
        		setPrice(pi);
        		pi.setAmount(1);
        		
        		if (pi.getAmount() == 1) {
        			p.setItemOnCursor(new ItemStack(Material.AIR));
        		} else {
        			pi.setAmount(pi.getAmount() - 1);
        			p.setItemOnCursor(pi);
        		}
        	} 
        	return;
        });
    }
    
    private Butto priceAmount(int a) {
    	return (p, e) -> {
    		
    		if (price == null) return;
    		
    		int b = price.getAmount();
    		if (b + a < 1) {
    			price.setAmount(1);
    		} else if (b + a > price.getMaxStackSize()) {
    			price.setAmount(price.getMaxStackSize());
    		} else {
    			price.setAmount(b + a);
    		}
    		setItem(13, price);
        };
    }
}
