package net.pgfmc.core.cmd.test.inventory;

import org.bukkit.event.inventory.InventoryType;

import net.kyori.adventure.text.Component;
import net.pgfmc.core.api.inventory.BaseInventory;

public class TestAnyInventorySizeInventory extends BaseInventory {

	public TestAnyInventorySizeInventory(int size, Component name) {
		super(size, name);
		
		if (size > 0)
		{
			setBack(0, new TestInventorySizeInventory(27, Component.text("Size: 27")).getInventory());
		}
		
	}
	
	public TestAnyInventorySizeInventory(InventoryType type, Component name)
	{
		super(type, name);
		
		if (type.getDefaultSize() > 0)
		{
			setBack(0, new TestInventorySizeInventory(27, Component.text("Size: 27")).getInventory());
		}
		
	}
		
}
