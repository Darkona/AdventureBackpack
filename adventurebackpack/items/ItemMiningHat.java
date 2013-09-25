package adventurebackpack.items;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import adventurebackpack.blocks.Blocks;
import adventurebackpack.blocks.tileentities.TileKamikaze;
import adventurebackpack.client.models.ModelMiningHat;
import adventurebackpack.common.Textures;
import adventurebackpack.common.Utils;
import adventurebackpack.config.ItemInfo;

public class ItemMiningHat extends ItemArmor {

	private boolean shining = true;
	
	public ItemMiningHat(int par1) {
		
		super(par1, EnumArmorMaterial.IRON, 2, 1);
		setCreativeTab(CreativeTabs.tabCombat);
		setFull3D();
		setMaxStackSize(1);
		setUnlocalizedName(ItemInfo.HELMET_UNLOCALIZED_NAME);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 0;
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return ModelMiningHat.instance;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return Textures.resourceString("textures/items/helmetStandard.png");
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		// TODO Auto-generated method stub
		super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack helmet) {
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayersHat(world, player, true, 75);
		
		if(!helmet.hasTagCompound()){
			helmet.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = helmet.getTagCompound();

		int prevX = nbt.getInteger("lightX");
		int prevY = nbt.getInteger("lightY");
		int prevZ = nbt.getInteger("lightZ");
		
		//If you are outside at daytime, no need for the helmet to be on.
		// TODO might change to being outside altogether, seems legit.
		
		if (world.canBlockSeeTheSky(
				MathHelper.floor_double(player.posX),
				MathHelper.floor_double(player.posY),
				MathHelper.floor_double(player.posZ)) && world.isDaytime() )
		{
			//nbt.setBoolean("hasLight", false);
			return;
		}
		
		if(mop!= null && mop.typeOfHit == EnumMovingObjectType.TILE)
		{
			int mopX = mop.blockX;
			int mopY = mop.blockY;
			int mopZ = mop.blockZ;
	
				
				switch (mop.sideHit) {
					case 0: mopY--; break;
					case 1: mopY++; break;
					case 2: mopZ--; break;
					case 3: mopZ++; break;
					case 4: mopX--; break;
					case 5: mopX++; break;
				}
				//If it's already shiny, keep it shiny and move on
				if (world.getBlockId(mopX, mopY, mopZ) == Blocks.lightblock.blockID) {
//					TileEntity te = world.getBlockTileEntity(mopX, mopY, mopZ);
//					if(te instanceof TileKamikaze) ((TileKamikaze)te).keepLiving();
					return;
				}
				
				//If the point of view has moved, remove the previous shiny block, unless you put a block there or something
//				if(shining && (prevX != mopX || prevY != mopY || prevZ != mopZ) && world.isAirBlock(prevX, prevY, prevZ)) {
//					world.setBlockToAir(prevX, prevY, prevZ);
//				}
				
				//Thanks Euclides! This gets the distance to the block that's going to shine
				int distance = MathHelper.floor_double(Math.sqrt( Math.pow((player.posX - mopX), 2) + Math.pow((player.posY - mopY), 2) + Math.pow((player.posZ - mopZ), 2)));
				
				//Look for a viable place to put the block, first try with the place you're looking at
				if( world.isAirBlock(mopX, mopY, mopZ) && world.setBlock(mopX, mopY, mopZ, Blocks.lightblock.blockID, 15 - (distance/10), 3))//Make it bright!
				{
					nbt.setInteger("lightX", mopX);
					nbt.setInteger("lightY", mopY);
					nbt.setInteger("lightZ", mopZ);
					nbt.setBoolean("hasLight", true);
				}else //If that fails, try a bit around.
				{
					for (int i = mopX - 1; i <= mopX + 1; i++)
					{		
						for (int j = mopY - 1; j <= mopY + 1; j++)
						{			
							for (int k = mopZ - 1; k <= mopZ + 1; k++)
							{					
								if( world.isAirBlock(i, j, k) && world.setBlock(i, j, k, Blocks.lightblock.blockID, 15 - (distance/10), 3))//Make it bright!
								{
									nbt.setInteger("lightX", i);
									nbt.setInteger("lightY", j);
									nbt.setInteger("lightZ", k);
									nbt.setBoolean("hasLight", true);
									break;
								}
							}
						}
					}
				}
			
		}else{
			if(shining && world.isAirBlock(prevX, prevY, prevZ))world.setBlockToAir(prevX, prevY, prevZ);
		}
		helmet.setTagCompound(nbt);
	}
}
