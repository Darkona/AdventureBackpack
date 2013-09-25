package adventurebackpack.blocks.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileKamikaze extends TileEntity {

	private int life;
	
	public TileKamikaze(){
		life = 3;
	}
	
	
	@Override
	public void updateEntity() {
		if(life == 0) seppuku(this.worldObj,this.xCoord,this.yCoord,this.zCoord);
		else life--;
	}
	
	public void seppuku(World world, int x, int y, int z){
		//Just for checking if they're actually suiciding. 
		//System.out.println("SEPPUKU at" + Utils.printCoordinates(x, y, z));
		world.setBlockToAir(x,y,z);
		world.removeBlockTileEntity(x, y, z);
		invalidate();
	}

	public void keepLiving() {
		life = 3;		
	}
	
}
