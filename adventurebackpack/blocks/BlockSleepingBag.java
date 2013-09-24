package adventurebackpack.blocks;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import adventurebackpack.blocks.tileentities.TileSleepingBag;
import adventurebackpack.common.Utils;
import adventurebackpack.config.BlockInfo;

public class BlockSleepingBag extends BlockContainer{

	public BlockSleepingBag(int par1) {
		super(par1, Material.cloth);
		setCreativeTab(CreativeTabs.tabTools);
		setLightValue(0.1F);
		setHardness(0.5f);
		setUnlocalizedName(BlockInfo.SLEEPINGBAG_NAME);
	}

	public static int[][] footBlockToHeadBlockMap = new int[][] { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };

	@Override
	public Icon getIcon(int par1, int par2) {

		return Block.cloth.getIcon(par1, 14);
	}
	
	public static int getDirection(int meta)
    {
        return meta & 3;
    }
	
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, int x, int y, int z) {
		
		return (world.getBlockMetadata(x, y, z) & 8) < 8;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean placeBlock(World world, int x, int y, int z, int meta){
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		int newX = x + footBlockToHeadBlockMap[getDirection(meta)][0];
		int newZ = z + footBlockToHeadBlockMap[getDirection(meta)][1];		
		System.out.println("META IS "+meta);	
		int newMeta = Utils.getOppositeCardinalFromMeta(meta);	
		System.out.println("NEWMETA IS"+newMeta);
		if(canPlaceBlockAt(world, newX, y, newZ)){
			world.setBlock(newX, y, newZ, this.blockID, newMeta|=8, 3);
			return true;
		}else{
			world.setBlockToAir(x, y, z);
			return false;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		
		int meta = MathHelper.floor_double((player.rotationYaw * 4) / 360 + 0.5D) & 3;
		placeBlock(world, x, y, z, meta);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		 return new TileSleepingBag();	
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileSleepingBag();
	}

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int side) {
		return (ForgeDirection.getOrientation(side) == ForgeDirection.UP);
	}

	@Override
	public int getBedDirection(IBlockAccess world, int x, int y, int z) {
		return getDirection(world.getBlockMetadata(x, y, z));
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		//world.notifyBlockChange(x, y, z, this.blockID);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int nBlockID) {
		int dir = getDirection(world.getBlockMetadata(x, y, z));
		if(world.getBlockId(x+footBlockToHeadBlockMap[dir][0], y, z+footBlockToHeadBlockMap[dir][1]) != this.blockID){
			world.setBlockToAir(x, y, z);
		}
	
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int blockID, float hitX, float hitY, float hitZ) {
		if(!isBedFoot(world, x, y, z))return true;
		if (world.isRemote)
		{
			return true;
		} else
		{
			int meta = world.getBlockMetadata(x, y, z);

			if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
			{
				if (isBedOccupied(meta))
				{
					EntityPlayer entityplayer1 = null;
					Iterator<?> iterator = world.playerEntities.iterator();

					while (iterator.hasNext())
					{
						EntityPlayer entityplayer2 = (EntityPlayer) iterator.next();

						if (entityplayer2.isPlayerSleeping())
						{
							ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;

							if (chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z)
							{
								entityplayer1 = entityplayer2;
							}
						}
					}

					if (entityplayer1 != null)
					{
						player.addChatMessage("tile.bed.occupied");
						return true;
					}

					setBedOccupied(world, x, y, z,player, false);
				}

				EnumStatus enumstatus = player.sleepInBedAt(x, y, z);

				switch(enumstatus){
				case OK:setBedOccupied(world, x, y, z,player, true);break;
				case NOT_POSSIBLE_NOW:player.addChatMessage("tile.bed.noSleep");break;
				case NOT_SAFE: player.addChatMessage("tile.bed.notSafe");break;
				case NOT_POSSIBLE_HERE:player.addChatMessage("You can't sleep here");break;
				case OTHER_PROBLEM:player.addChatMessage("You can't sleep");break;
				case TOO_FAR_AWAY:player.addChatMessage("You are too far from the sleeping bag");break;
				}

			} else
			{
				world.newExplosion((Entity) null, x + 0.5F, y + 0.5F, z + 0.5F, 5.0F, true, true);
			}
		}
		return true;
	}

	private boolean isBedOccupied(int meta) {
		return (meta & 4) > 0;
	}

	@Override
	public void setBedOccupied(World world, int x, int y, int z, EntityPlayer player, boolean occupied) {
		int meta = world.getBlockMetadata(x, y, z);
		meta = occupied ? meta |= 4 : meta & -5;
		world.setBlockMetadataWithNotify(x, y, z, meta, 4);
	}

	@Override
	public boolean isBed(World world, int x, int y, int z, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		return super.canRenderInPass(pass);
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int meta) {
		world.removeBlockTileEntity(x, y, z);
		world.destroyBlock(x, y, z, false);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int dir = getDirection(world.getBlockMetadata(x, y, z));
		switch (dir)
		{
		case 0:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 2.0F);
			break;
		case 1:
			setBlockBounds(-1.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
			break;
		case 2:
			setBlockBounds(0.0F, 0.0F, -1.0F, 1.0F, 0.2F, 1.0F);
			break;
		case 3:
			setBlockBounds(0.0F, 0.0F, 0.0F, 2.0F, 0.2F, 1.0F);
			break;
		default:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
			break;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, start, end);
	}
}
