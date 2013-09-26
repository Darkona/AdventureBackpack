package com.darkona.adventurebackpack.blocks;

import java.util.Random;

import com.darkona.adventurebackpack.blocks.tileentities.TileKamikaze;
import com.darkona.adventurebackpack.common.Textures;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLight extends Block {

	public BlockLight(int id) {
		super(id, Material.air);
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		par1World.setBlockToAir(par2, par3, par4);
		super.updateTick(par1World, par2, par3, par4, par5Random);
	}

		
	@Override
	public int getLightOpacity(World world, int x, int y, int z) {
		return 0;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean isOpaqueCube()
	    {
	        return false;
	    }
	 
	@Override
	public boolean canBeReplacedByLeaves(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		 par1World.setBlockToAir(par2, par3, par4);
	}

	@Override
	public boolean isCollidable()
    {
        return false;
    }
	
	@Override
	 public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg){
		blockIcon = iconReg.registerIcon(Textures.resourceString("blockLight"));		
	}
	
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		// TODO Auto-generated method stub
		return (par1World.isAirBlock(par2, par3, par4));
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}
	
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		
	}
	
	@Override
	 public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	    {
	        return null;
	    }

	
	public static void onBlockRemoved(World world, int prevX, int prevY, int prevZ) {
		
	}

//	@Override
//	public TileEntity createNewTileEntity(World world) {
//		return new TileKamikaze();
//	}


}
