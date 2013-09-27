package com.darkona.adventurebackpack.inventory;

import mods.railcraft.api.core.items.IToolCrowbar;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
//import tconstruct.library.tools.ToolCore;
import apis.ic2.api.item.ICustomElectricItem;
import apis.ic2.api.item.ISpecialElectricItem;
import buildcraft.api.tools.IToolPipette;
import buildcraft.api.tools.IToolWrench;
import forestry.api.core.IToolScoop;

@SuppressWarnings("deprecation")
public class SlotTool extends Slot {

	public SlotTool(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isValidTool(stack);
	}

	public static boolean isValidTool(ItemStack stack) {

		boolean valid = false;

		if (stack != null && stack.stackSize == 1)
		{

			Item item = stack.getItem();
			String name = item.getUnlocalizedName().toLowerCase();
			// Vanilla
			if (item instanceof ItemTool || item instanceof ItemHoe)
			{
				return true;
			}
			// BuildCraft
			if (item instanceof IToolWrench || item instanceof IToolPipette)
			{
				return true;
			}
			// IndustrialCraft
			if (item instanceof ISpecialElectricItem || item instanceof ICustomElectricItem)
			{
				return true;
			}
			
			
		
			// Railcraft
			if (item instanceof IToolCrowbar)
			{
				return true;
			}
			// Forestry
			if (item instanceof IToolScoop)
			{
				return true;
			}
		
			// Just for extra compatibility
			if (name.contains("wrench") || name.contains("hammer") || name.contains("axe") || name.contains("shovel") || name.contains("grafter"))
			{
				return true;
			}
			try
			{
				// Tinker's Construct
				if (java.lang.Class.forName("tconstruct.library.tools.ToolCore").isInstance(item))
				{
					return true;
				}
			} catch (Exception oops){}

		}

		return valid;
	}

}
