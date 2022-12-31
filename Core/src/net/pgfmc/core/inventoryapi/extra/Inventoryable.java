package net.pgfmc.core.inventoryapi.extra;

import net.pgfmc.core.inventoryapi.BaseInventory;

/**
 * does literally nothing rn
 * @author CrimsonDart
 *
 */
public interface Inventoryable extends Buttonable {
	
	public abstract BaseInventory toInventory();
	
	public default Butto toAction() {
		return (p, e) -> {
			p.openInventory(toInventory().getInventory());
		};
	}
}
