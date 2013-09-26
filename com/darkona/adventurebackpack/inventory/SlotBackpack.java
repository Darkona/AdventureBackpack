package com.darkona.adventurebackpack.inventory;

import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.items.ItemAdvBackpack;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBackpack extends Slot {

	public SlotBackpack(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return (!(stack.getItem() instanceof ItemAdvBackpack) && !(stack.getItem().itemID == ABPBlocks.advbackpack.blockID));
	}
}
