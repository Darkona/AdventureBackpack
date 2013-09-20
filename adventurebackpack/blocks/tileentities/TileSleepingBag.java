package adventurebackpack.blocks.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileSleepingBag extends TileEntity
{

	public boolean occupied;

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound)
	{
		// TODO Auto-generated method stub
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setBoolean("occupied", occupied);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound)
	{

		super.readFromNBT(par1nbtTagCompound);
		if (par1nbtTagCompound.hasKey("occupied"))
		{
			occupied = par1nbtTagCompound.getBoolean("occupied");
		}
	}
}
