package adventurebackpack.inventory;

import forestry.api.core.IToolScoop;
import buildcraft.api.tools.IToolPipette;
import buildcraft.api.tools.IToolWrench;
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
				valid = true;
			}
			// BuildCraft
			if (item instanceof IToolWrench || item instanceof IToolPipette)
			{
				valid = true;
			}
			// IndustrialCraft
			if (item instanceof ISpecialElectricItem || item instanceof ICustomElectricItem)
			{
				valid = true;
			}
			// Tinker's Construct
			try
			{
				if (java.lang.Class.forName("tconstruct.library.tools.ToolCore").isInstance(item))
				{
					valid = true;
				}
			} catch (ClassNotFoundException e)
			{

			}
			// Railcraft
			if (item instanceof IToolCrowbar)
			{
				valid = true;
			}
			// Forestry
			if (item instanceof IToolScoop)
			{
				valid = true;
			}
			// Just for extra compatibility
			if (name.contains("wrench") || name.contains("hammer") || name.contains("axe") || name.contains("shovel"))
			{
				valid = true;
			}

		}

		return valid;
	}

}
