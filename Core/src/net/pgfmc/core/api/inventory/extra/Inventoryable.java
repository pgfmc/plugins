package net.pgfmc.core.api.inventory.extra;

import net.pgfmc.core.api.inventory.BaseInventory;

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
