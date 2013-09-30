package com.darkona.adventurebackpack.items;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.client.models.ModelMiningHat;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.config.ItemInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMiningHelmet extends ItemArmor {
	
	public ItemMiningHelmet(int par1) {
		
		super(par1, EnumArmorMaterial.IRON, 2, 0);
		setFull3D();
		setMaxStackSize(1);
		setUnlocalizedName(ItemInfo.HELMET_UNLOCALIZED_NAME);
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return ModelMiningHat.instance.setHelmetStack(itemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return Textures.resourceString("textures/items/helmetStandard.png");
	}

	@Override
	public String getItemDisplayName(ItemStack helmet) {
		if(helmet.hasTagCompound()){
			switch(helmet.getTagCompound().getByte("mode")){
			case 0 : return "Mining Helmet: Deactivated";
			case 1 : return "Mining Helmet: Activated";
			case 2 : return "Mining Helmet: Auto";
			default: return "Mining Helmet";
			}
		}
		return "Mining Helmet";
	}
	
	@Override
	public void onUpdate(ItemStack helmet, World world, Entity entity, int par4, boolean currentItem) {
		if(entity instanceof EntityPlayer && currentItem){
			shine(world, (EntityPlayer)entity, helmet);
		}
		if(!currentItem && helmet.hasTagCompound()){
			NBTTagCompound nbt = helmet.getTagCompound();
			int prevX = nbt.getInteger("prevX");
			int prevY = nbt.getInteger("prevY");
			int prevZ = nbt.getInteger("prevZ");
			if(world.getBlockId(prevX, prevY, prevZ) == ABPBlocks.lightblock.blockID){
				world.setBlockToAir(prevX, prevY, prevZ);
				nbt.setBoolean("mustRemove", false);
				helmet.setTagCompound(nbt);
			}
		}
		
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		ItemStack helmet = entityItem.getEntityItem();
		World world = entityItem.worldObj;
		if(helmet.hasTagCompound()){
			NBTTagCompound nbt = helmet.getTagCompound();
			int prevX = nbt.getInteger("prevX");
			int prevY = nbt.getInteger("prevY");
			int prevZ = nbt.getInteger("prevZ");
			if(world.getBlockId(prevX, prevY, prevZ) == ABPBlocks.lightblock.blockID){
				world.setBlockToAir(prevX, prevY, prevZ);
				nbt.setBoolean("mustRemove", false);
				helmet.setTagCompound(nbt);
			}
		}
		return super.onEntityItemUpdate(entityItem);
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack helmet) {
		shine(world, player, helmet);
		//checkDirectionAndPlaceTorch(world, player, helmet);
	}
	
	@Override	
	public ItemStack onItemRightClick(ItemStack helmet, World par2World, EntityPlayer par3EntityPlayer) {
		if(!helmet.hasTagCompound()){
			helmet.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = helmet.getTagCompound();
		nbt.setByte("mode", (byte)((nbt.getByte("mode")+1) % 3));
		return helmet;
	}
	
	private void removeLight(World world, int x, int y, int z){
		if(world.getBlockId(x, y, z) == ABPBlocks.lightblock.blockID){
			world.setBlockToAir(x, y, z);
		}
	}
	
	private void shine(World world, EntityPlayer player, ItemStack helmet){
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayersHat(world, player, true, 75);
		boolean day = world.isDaytime();
		if(!helmet.hasTagCompound()){
			helmet.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = helmet.getTagCompound();

		
		int prevX = nbt.getInteger("prevX");
		int prevY = nbt.getInteger("prevY");
		int prevZ = nbt.getInteger("prevZ");
		if(nbt.getByte("mode") == 0) {
			removeLight(world, prevX,prevY,prevZ);
			return;
		}
		
		// TODO might change to being outside altogether, seems legit
		//If you are outside at daytime, no need for the helmet to be activated.
		if (nbt.getByte("mode") == 2 && world.canBlockSeeTheSky(
				MathHelper.floor_double(player.posX),
				MathHelper.floor_double(player.posY),
				MathHelper.floor_double(player.posZ)) && day ) return;
		
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
		
			//Grrrrr
			if(mopX != prevX || mopY != prevY || mopZ != prevZ && nbt.getBoolean("mustRemove")){
						removeLight(world,prevX,prevY,prevZ);
					 nbt.setBoolean("mustRemove", false);
					 helmet.setTagCompound(nbt);
				
				
			}
			
			//If it's already shiny, keep it shiny and move on
			if (world.getBlockId(mopX, mopY, mopZ) == ABPBlocks.lightblock.blockID || world.getBlockLightValue(mopX, mopY, mopZ) > 6) {
				return;
			}

			//Thanks Euclides! This gets the distance to the block that's going to shine
			int distance = MathHelper.floor_double(Math.sqrt( Math.pow((player.posX - mopX), 2) + Math.pow((player.posY - mopY), 2) + Math.pow((player.posZ - mopZ), 2)));
				
			//Look for a viable place to put the block, first try with the place you're looking at
			if( world.isAirBlock(mopX, mopY, mopZ))
			{
				//Make it bright!
				
				 if(world.setBlock(mopX, mopY, mopZ, ABPBlocks.lightblock.blockID, 15 - (distance/10), 3))
				 {
					 nbt.setInteger("prevX", mopX);
					 nbt.setInteger("prevY", mopY);
					 nbt.setInteger("prevZ", mopZ);
					 nbt.setBoolean("mustRemove", true);
					 helmet.setTagCompound(nbt);
				 }
				 
			}else //If that fails, try a bit around.
			{
				for (int i = mopX - 1; i <= mopX + 1; i++)
				{		
					for (int j = mopY - 1; j <= mopY + 1; j++)
					{			
						for (int k = mopZ - 1; k <= mopZ + 1; k++)
						{			
							//Make it bright!
							if( world.isAirBlock(i, j, k) && world.setBlock(i, j, k, ABPBlocks.lightblock.blockID, 15 - (distance/10), 3) )
							{
								 nbt.setInteger("prevX", i);
								 nbt.setInteger("prevY", j);
								 nbt.setInteger("prevZ", k);
								 nbt.setBoolean("mustRemove", true);
								 helmet.setTagCompound(nbt);
							}else{
								nbt.setBoolean("mustRemove", false);
								 helmet.setTagCompound(nbt);
							}
						}
					}
				}
			}
			
		}// MOP == NULL or entity, looking at nothing
		if(nbt.getBoolean("mustRemove")){
			if(world.getBlockId(prevX, prevY, prevZ) == ABPBlocks.lightblock.blockID)world.setBlockToAir(prevX, prevY, prevZ);
			nbt.setBoolean("mustRemove", false);
		}
	}
	
	private void tryPlaceTorch(World world, int x, int y, int z, boolean coord, boolean sign, EntityPlayer player){
		int torchSlot = hasTorches(player);
		if(torchSlot == -1) return; 
		if(coord){
			if (sign){
				for (int i = x+2; i >= x; i--){
					if( placeTorch(world,i,y+1,z) || placeTorch(world,i,y,z) ){
						discountTorch(player, torchSlot);
						return;
					}
				}
			}else{
				for (int i = x-2; i <= x; i++){
					if(placeTorch(world,i,y+1,z) || placeTorch(world,i,y,z)){
						discountTorch(player, torchSlot);
						return;
					}
				}
			}
		}else{
			if (sign){
				for (int i = z+2; i >= z; i--){
					
					if(placeTorch(world,x,y+1,i) || placeTorch(world,x,y,i)){
						discountTorch(player, torchSlot);
						return;
					}
				}
			}else{
				for (int i = z-2; i <= z; i++){
					if(placeTorch(world,x,y+1,i) || placeTorch(world,x,y,i)){
						discountTorch(player, torchSlot);
						return;
					}
				}
			}
		}
		
	}
	
	private void discountTorch(EntityPlayer player, int slot) {
		player.inventory.decrStackSize(slot, 1);
	}

	private int hasTorches(EntityPlayer player) {
		for(int i = 0; i < player.inventory.mainInventory.length; i++){
			if(player.inventory.getStackInSlot(i)!=null && player.inventory.getStackInSlot(i).itemID == Block.torchWood.blockID){
				return i;
			}
		}
		return -1;
	}

	private boolean placeTorch(World world, int x, int y, int z) {
		return
				world.getBlockLightValue( x, y, z) < 7 &&
				Block.torchWood.canPlaceBlockAt(world, x, y, z) &&
				world.isAirBlock(x, y, z) &&
				!world.canBlockSeeTheSky(x, y, z) &&
				world.setBlock(x, y, z, Block.torchWood.blockID);
				
	}

	private void checkDirectionAndPlaceTorch(World world, EntityPlayer player, ItemStack helmet){
		int torchX = MathHelper.floor_double(player.posX);
		int torchY = MathHelper.floor_double(player.posY);
		int torchZ = MathHelper.floor_double(player.posZ);
		int dir = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
		if(world.canBlockSeeTheSky(torchX,torchY,torchZ))return;
		switch(dir){
		case 0:/* torchX --;*/ torchZ--;tryPlaceTorch(world, torchX, torchY, torchZ, true, false, player);break;
		case 1: /*torchZ --;*/ torchX--;tryPlaceTorch(world, torchX, torchY, torchZ, false, false, player);break;
		case 2: /*torchX ++;*/ torchZ++;tryPlaceTorch(world, torchX, torchY, torchZ, true, true, player);break;
		case 3: /*torchZ ++;*/ torchX++;tryPlaceTorch(world, torchX, torchY, torchZ, false, true, player);break;
		}
	}
	
}
