package com.darkona.adventurebackpack.blocks.tileentities;

import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.common.Utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileKamikaze extends TileEntity {

	private int life;
	
	public TileKamikaze(){
		life = 1;
	}
	
	
	@Override
	public void updateEntity() {
		if(life<=0){
			World world = this.worldObj;
			
		}
	}
	
	public void seppuku(World world, int x, int y, int z){
		//Just for checking if they're actually suiciding. 
		
		if(world.getBlockId(x, y, z)==ABPBlocks.lightblock.blockID){
			System.out.println("SEPPUKU at" + Utils.printCoordinates(x, y, z));
			world.setBlockToAir(x,y,z);
			world.removeBlockTileEntity(x, y, z);
		}
		
	}

	public void keepLiving() {
		life = 3;		
	}
	
}
