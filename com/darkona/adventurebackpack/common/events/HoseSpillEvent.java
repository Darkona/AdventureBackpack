package com.darkona.adventurebackpack.common.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class HoseSpillEvent extends FluidSpilledEvent {
	public final FluidTank currentTank;
	public final EntityPlayer player;
	public FluidStack fluidResult;

	public HoseSpillEvent(EntityPlayer theNoob, World world, int x, int y, int z, FluidTank tank) {
		super(tank.getFluid(), world, x, y, z);
		currentTank = tank;
		player = theNoob;
	}

}
