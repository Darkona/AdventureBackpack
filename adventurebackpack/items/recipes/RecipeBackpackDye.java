package adventurebackpack.items.recipes;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import adventurebackpack.common.Utils;
import adventurebackpack.items.ItemAdvBackpack;

public class RecipeBackpackDye implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		ItemStack backpack = null;
		ItemStack dyeItem = null;

		for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i)
		{
			ItemStack stackChecking = inventorycrafting.getStackInSlot(i);

			if (stackChecking != null)
			{
				if (stackChecking.getItem() instanceof ItemAdvBackpack)
				{
					if (backpack != null)
					{
						return false;
					}

					backpack = stackChecking;
				} else
				{
					if (!Utils.getPossibleDyes(stackChecking))
					{
						return false;
					}

					dyeItem = stackChecking;
				}
			}
		}

		return backpack != null && dyeItem != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		ItemStack backpack = null;
		ItemStack dyeItem = null;
		ItemStack output = null;
		for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i)
		{
			ItemStack stackChecking = inventorycrafting.getStackInSlot(i);

			if (stackChecking != null)
			{
				if (stackChecking.getItem() instanceof ItemAdvBackpack)
				{
					if (backpack != null)
					{
						return null;
					}
					backpack = stackChecking;
					output = backpack.copy();
					output.stackSize = 1;
				} else
				{
					if (!Utils.getPossibleDyes(stackChecking))
					{
						return null;
					}
					dyeItem = stackChecking;
				}
			}
		}
		output.stackTagCompound.setInteger("lastTime", 0);
		String color;
		if (OreDictionary.getOreName(OreDictionary.getOreID(dyeItem)).contains("dye"))
		{
			color = OreDictionary.getOreName(OreDictionary.getOreID(dyeItem)).substring(3);
			output.stackTagCompound.setString("color", color);
			output.stackTagCompound.setString("colorName",color);
			return output;
		}
		for (Item item : Utils.items)
		{
			if (dyeItem.getItem() == item)
			{
				String itemName = item.getUnlocalizedName(dyeItem).substring(5);
				color = Utils.capitalize(itemName);
				if (item == Item.recordCat)
				{
					color = "Rainbow";
				}
				output.stackTagCompound.setString("color", color);
				output.stackTagCompound.setString("colorName", Utils.getDisplayNameForColor(color));
				return output;
			}
		}
		for (Block blocky : Utils.blockies)
		{
			if (dyeItem.itemID == blocky.blockID)
			{
				String blockName = blocky.getUnlocalizedName().substring(5);
				color = Utils.capitalize(blockName);
				if (blocky.blockID == Block.mushroomRed.blockID)
				{
					color = "MushroomRed";
				}
				if (blocky.blockID == Block.mushroomBrown.blockID)
				{
					color = "MushroomBrown";
				}
				output.stackTagCompound.setString("color", color);
				output.stackTagCompound.setString("colorName", Utils.getDisplayNameForColor(color));
				return output;
			}
		}
		return null;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
