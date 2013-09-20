package adventurebackpack.fluids.effects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import adventurebackpack.api.FluidEffect;

public class MilkEffect extends FluidEffect{
	
	public MilkEffect() {
		super(FluidRegistry.getFluid("milk"), 7, "Milk makes you feel good");
	}

	@Override
	public void affectDrinker(World world, EntityPlayer player) {
		if(!world.isRemote)world.playSoundAtEntity(player, "mob.cow.say", 0.3F, 1);
		player.curePotionEffects(new ItemStack(Item.bucketMilk));
	}
}
