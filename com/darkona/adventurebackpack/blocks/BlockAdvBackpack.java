package com.darkona.adventurebackpack.blocks;

import java.util.Random;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.ModInformation;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.fluids.Fluids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAdvBackpack extends BlockContainer {

	public BlockAdvBackpack(int id) {
		super(id, Material.clay);
		setHardness(0.3f);
		setStepSound(soundClothFootstep);
		setResistance(2);
		setUnlocalizedName("Adventure Backpack");
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		int l1 = 0, l2 = ((world.getBlockMetadata(x, y, z) & 4) >= 4) ? 15 : 0;
		if (world.getBlockTileEntity(x, y, z) instanceof TileAdvBackpack)
		{
			l1 = ((TileAdvBackpack) world.getBlockTileEntity(x, y, z)).luminosity;
		}
		return (l1 > l2) ? l1 : l2;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return ((world.getBlockMetadata(x, y, z) & 8) >= 8) ? 15 : 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return ((world.getBlockMetadata(x, y, z) & 8) >= 8) ? 15 : 0;
	}

	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

		if ((world.getBlockMetadata(x, y, z) & 8) >= 8)
		{
			switch (side)
			{
			case 0:
			case 1:
			case 2:
			case 3:
				return true;
			default:
				return false;
			}
		} else
			return false;
	}

	@Override
	public boolean canProvidePower() {

		return true;
	}

	@Override
	public boolean canDragonDestroy(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
		return false;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 0, world, x, y, z);
		try
		{
			TileAdvBackpack te = (TileAdvBackpack) world.getBlockTileEntity(x, z, z);
			System.out.println("My color is" + te.getColor());
			System.out.println(Utils.getBackpackColor(te).toString());
		} catch (Exception oops)
		{
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileAdvBackpack();
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		createNewTileEntity(world);
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		meta = (meta & 8) >= 8 ? meta - 8 : meta;
		meta = (meta & 4) >= 4 ? meta - 4 : meta;
		switch (meta)
		{
		case 0:
		case 2:
			setBlockBounds(0.0F, 0.0F, 0.4F, 1.0F, 0.6F, 0.6F);
			break;
		case 1:
		case 3:
			setBlockBounds(0.4F, 0.0F, 0.0F, 0.6F, 0.6F, 1.0F);
			break;
		}
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int dir = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
		createTileEntity(world, dir);
		if (stack != null && stack.stackTagCompound != null && stack.stackTagCompound.hasKey("color"))
		{
			if (stack.stackTagCompound.getString("color").contains("BlockRedstone"))
				dir = dir | 8;
			if (stack.stackTagCompound.getString("color").contains("Lightgem"))
				dir = dir | 4;
		}
		world.setBlockMetadataWithNotify(x, y, z, dir, 3);
	}

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int side) {
		if (ForgeDirection.getOrientation(side) == ForgeDirection.UP)
			return true;
		return false;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		// ULTRA HACK MADSKILLZ
		// THIS REGISTERS THE FLUIDS' TEXTURES MUAHAHAHAHA NO NEED FOR FLUID
		// BLOCKS
		blockIcon = register.registerIcon(ModInformation.ID.toLowerCase() + ":" + Fluids.milk.getUnlocalizedName());
		Fluids.milk.setIcons(blockIcon);
		// blockIcon = register.registerIcon(Block.cloth.getItemIconName());
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return super.createTileEntity(world, metadata);
	}

	@Override
	public Icon getIcon(int par1, int par2) {
		// Fluids.milk.setIcons(Block.waterMoving.getIcon(par1, par2));
		return Block.cloth.getIcon(par1, 1);

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, start, end);
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		TileEntity backpack =  world.getBlockTileEntity(x, y, z);

		if (backpack instanceof TileAdvBackpack && !world.isRemote && player != null)
		{
			if ((player.isSneaking()) ? ((TileAdvBackpack)backpack).equip(world, player, x, y, z) : ((TileAdvBackpack)backpack).drop(world, player, x, y, z))
			{
				return world.destroyBlock(x, y, z, false);
			}
		} else
		{
			return world.destroyBlock(x, y, z, false);
		}
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion par5Explosion) {
		removeBlockByPlayer(world, null, x,y,z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof IInventory)
		{
			IInventory inventory = (IInventory) te;

			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack stack = inventory.getStackInSlotOnClosing(i);

				if (stack != null)
				{
					float spawnX = x + world.rand.nextFloat();
					float spawnY = y + world.rand.nextFloat();
					float spawnZ = z + world.rand.nextFloat();
					float mult = 0.05F;

					EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);

					droppedItem.motionX = -0.5F + world.rand.nextFloat() * mult;
					droppedItem.motionY = 4 + world.rand.nextFloat() * mult;
					droppedItem.motionZ = -0.5 + world.rand.nextFloat() * mult;

					world.spawnEntityInWorld(droppedItem);
				}
			}
		}

		super.breakBlock(world, x, y, z, id, meta);
	}
}
