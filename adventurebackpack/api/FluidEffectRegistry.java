package adventurebackpack.api;

import java.util.ArrayList;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import adventurebackpack.fluids.effects.FuelEffect;
import adventurebackpack.fluids.effects.LavaEffect;
import adventurebackpack.fluids.effects.MelonEffect;
import adventurebackpack.fluids.effects.MilkEffect;
import adventurebackpack.fluids.effects.OilEffect;
import adventurebackpack.fluids.effects.WaterEffect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;

public class FluidEffectRegistry {

	static BiMap<Integer, FluidEffect> effects = HashBiMap.create();

	public static final FluidEffect WATER_EFFECT = new WaterEffect();
	public static final FluidEffect LAVA_EFFECT = new LavaEffect();
	public static final FluidEffect MILK_EFFECT = new MilkEffect();
	public static final FluidEffect OIL_EFFECT = new OilEffect();
	public static final FluidEffect FUEL_EFFECT = new FuelEffect();
	public static final FluidEffect MELON_EFFECT = new MelonEffect();

	static int effectID = 0;

	public FluidEffectRegistry() {
	}

	public static void init() {
		effects.clear();
		registerFluidEffect(LAVA_EFFECT);
		registerFluidEffect(WATER_EFFECT);
		registerFluidEffect(MILK_EFFECT);
		registerFluidEffect(OIL_EFFECT);
		registerFluidEffect(FUEL_EFFECT);
		// registerFluidEffect(MELON_EFFECT);
	}

	public static int registerFluidEffect(FluidEffect effect) {

		if (effect.effectID != -1 && !effects.containsKey(effect.effectID))
		{
			effects.put(++effectID, effect);
			effect.effectID = effectID;
			return effectID;
		}
		return -1;

	}

	public static Map<Integer, FluidEffect> getRegisteredFluidEffects() {
		return ImmutableMap.copyOf(effects);
	}

	public static boolean hasFluidEffect(Fluid fluid) {
		for (FluidEffect effect : getRegisteredFluidEffects().values())
		{
			if (fluid == effect.fluid)
				return true;
		}
		return false;
	}

	public static ArrayList<FluidEffect> getEffectsForFluid(Fluid fluid) {
		ArrayList<FluidEffect> effectsForFluid = new ArrayList<FluidEffect>();
		for (FluidEffect effect : effects.values())
		{
			if (fluid == effect.fluid)
			{
				effectsForFluid.add(effect);
			}
		}
		return effectsForFluid;
	}

}