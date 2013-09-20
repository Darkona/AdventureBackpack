package adventurebackpack.fluids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class Fluids {

	public static Fluid milk;

	public static void init() {
		milk = new FluidMilk();
		milk.setUnlocalizedName("milk");
		FluidContainerRegistry.registerFluidContainer(milk, new ItemStack(Item.bucketMilk, 1), new ItemStack(
				Item.bucketEmpty, 1));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(Item.potion, 1),
				new ItemStack(Item.glassBottle, 1));
	}

}
