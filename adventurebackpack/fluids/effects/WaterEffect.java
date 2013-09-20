package adventurebackpack.fluids.effects;

import adventurebackpack.api.FluidEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraftforge.fluids.FluidRegistry;

public class WaterEffect extends FluidEffect {

	public WaterEffect() {
		super(FluidRegistry.WATER, 7);
		msg = "Water is refreshing";
	}

	@Override
	public void affectDrinker(World world, EntityPlayer player) {
		if (world.provider.isHellWorld
				|| world.getBiomeGenForCoords((int) player.posX, (int) player.posZ).getIntTemperature() >= 60
				|| world.getBiomeGenForCoords((int) player.posX, (int) player.posZ) instanceof BiomeGenDesert) {
			Potion.heal.performEffect(player, 2);
		}
		player.getFoodStats().addStats(1, 0.1f);
		player.addPotionEffect(new PotionEffect(Potion.regeneration.id, time * 20, -1, false));
		if (player.isBurning()) {
			player.extinguish();
			player.curePotionEffects(new ItemStack(Item.bucketMilk, 1));
		}
	}
}
