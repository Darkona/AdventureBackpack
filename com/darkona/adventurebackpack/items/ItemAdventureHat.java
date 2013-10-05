package com.darkona.adventurebackpack.items;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.client.models.ModelAdventureHat;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.config.ItemInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdventureHat extends ItemArmor {
	
	public ItemAdventureHat(int par1) {
		
		super(par1, EnumArmorMaterial.CLOTH, 2, 0);
		setFull3D();
		setMaxStackSize(1);
		setUnlocalizedName(ItemInfo.HAT_UNLOCALIZED_NAME);
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return ModelAdventureHat.instance;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return Textures.resourceString("textures/items/item.adventureHat.png");
	}

	@Override
	public String getItemDisplayName(ItemStack helmet) {
		return "Adventurer's Fedora";
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack helmet) {
		//checkDirectionAndPlaceTorch(world, player, helmet);
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
