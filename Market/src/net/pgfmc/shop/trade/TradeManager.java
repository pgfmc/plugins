package net.pgfmc.shop.trade;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class TradeManager {
	
	public TradeManager(PlayerData p1, PlayerData p2) {
		
		Trader t1 = new Trader(p1);
		p1.setData("trade", t1);
		
		Trader t2 = new Trader(p2);
		p2.setData("trade", t2);
		
		t1.oppo = t2;
		t2.oppo = t1;
		
		t1.unready();
		t2.unready();
	}
	
	public class Trader extends BaseInventory {
		
		public Boolean ready;
		public Trader oppo;
		public final PlayerData pd;

		public Trader(PlayerData pd) {
			super(54, "");
			this.pd = pd;
			
			
			int[] intar = {3, 5, 12, 14, 21, 23, 30, 32, 39, 41, 48, 50};
			for (int i : intar) {
				setItem(i, Material.IRON_BARS).n(" ");
			}
			
			int[] inter = {0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29, 36, 37, 38, 45, 46, 47};
			for (int i : inter) {
				setAction(i, (p, e) -> {
					e.setCancelled(false);
					
					ItemStack item = e.getCursor();
					
					oppo.setItem(i + 6, item);
				});
			}
		}
		
		public void unready() {
			if (!ready) return;
			
			ready = false;
			setItem(22, Material.RED_CONCRETE).n("§c[NOT READY]");
			setAction(22, (p, e) -> {
				ready();
			});
			oppo.setItem(40, Material.RED_TERRACOTTA);
		}
		
		public void ready() {
			if (ready) return;
			
			ready = true;
			setItem(22, Material.GREEN_CONCRETE).n("§a[READY]");
			setAction(22, (p, e) -> {
				unready();
			});
			oppo.setItem(40, Material.GREEN_TERRACOTTA);
		}
		
		
		
		
		
		
		
		
		
	}
}
