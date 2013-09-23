package aussie.codechicken.core.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

	public static int getLuminosity(FluidStack stack, double density) {
		Fluid fluid = stack.getFluid();
		if (fluid == null)
			return 0;
		int light = fluid.getLuminosity(stack);
		if (fluid.isGaseous())
			light = (int) (light * density);
		return light;
	}
}
