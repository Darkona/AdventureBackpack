package adventurebackpack.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;

public interface IAdvBackpack extends IInventory {
	
	FluidTank getRightTank();

	void setRightTank(FluidTank rightTank);
	
	public FluidTank getLeftTank();
	
	public void setLeftTank(FluidTank leftTank);

	public NBTTagCompound writeToNBT();
	
	public boolean updateTankSlots(FluidTank tank, int slotIn);
	
	
}
