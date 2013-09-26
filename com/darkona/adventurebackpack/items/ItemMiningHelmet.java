package com.darkona.adventurebackpack.items;

import net.minecraft.client.model.ModelBiped;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
		//setCreativeTab(CreativeTabs.tabCombat);
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
			if(nbt.getBoolean("mustRemove") && world.getBlockId(prevX, prevY, prevZ) == ABPBlocks.lightblock.blockID){
				world.setBlockToAir(prevX, prevY, prevZ);
				nbt.setBoolean("mustRemove", false);
			}
		}
		
	}
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack helmet) {
			shine(world, player, helmet);
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
	
	@SideOnly(Side.CLIENT)
	private void shine(World world, EntityPlayer player, ItemStack helmet){
		MovingObjectPosition mop = Utils.getMovingObjectPositionFromPlayersHat(world, player, true, 75);
		boolean day = world.isDaytime();
		if(!helmet.hasTagCompound()){
			helmet.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = helmet.getTagCompound();

		if(nbt.getByte("mode") == 0) return;
		int prevX = nbt.getInteger("prevX");
		int prevY = nbt.getInteger("prevY");
		int prevZ = nbt.getInteger("prevZ");
		
		
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
				if(world.getBlockId(prevX, prevY, prevZ) == ABPBlocks.lightblock.blockID)world.setBlockToAir(prevX, prevY, prevZ);
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
	
	
}
