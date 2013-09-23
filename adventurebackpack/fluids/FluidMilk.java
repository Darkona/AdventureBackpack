package adventurebackpack.fluids;

import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FluidMilk extends Fluid {

	public FluidMilk() {
		super("milk");
		setDensity(1200);
		setViscosity(1200);
		setUnlocalizedName("milk");
		setLuminosity(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon() {
		return stillIcon;
	}

}
