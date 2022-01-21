package net.pgfmc.shop.trade;

import org.bukkit.Material;

import net.pgfmc.core.inventoryAPI.BaseInventory;
import net.pgfmc.core.inventoryAPI.extra.SizeData;
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
		
		public Boolean ready = false;
		public Trader oppo;
		public final PlayerData pd;

		public Trader(PlayerData pd) {
			super(SizeData.BIG, "");
			this.pd = pd;
			
			
			int[] intar = {3, 5, 12, 14, 21, 23, 30, 32, 39, 41, 48, 50};
			for (int i : intar) {
				setItem(i, Material.IRON_BARS).n(" ");
			}
		}
		
		public void unready() {
			
			ready = false;
			setItem(22, Material.RED_CONCRETE).n("§c[NOT READY]");
			setAction(22, (p, e) -> {
				ready();
			});
			oppo.setItem(40, Material.RED_TERRACOTTA);
		}
		
		public void ready() {
			
			ready = true;
			setItem(22, Material.GREEN_CONCRETE).n("§a[READY]");
			setAction(22, (p, e) -> {
				unready();
			});
			oppo.setItem(40, Material.GREEN_TERRACOTTA);
		}
	}
}
