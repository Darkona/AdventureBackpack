package adventurebackpack.blocks;

import java.util.Iterator;

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
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import adventurebackpack.blocks.tileentities.TileSleepingBag;
import adventurebackpack.config.BlockInfo;

public class BlockSleepingBag extends BlockContainer {

	public BlockSleepingBag(int par1) {
		super(par1, Material.cloth);
		setCreativeTab(CreativeTabs.tabTools);
		setLightValue(0.1F);
		setHardness(0.5f);
		setUnlocalizedName(BlockInfo.SLEEPINGBAG_NAME);
	}

	private int[][] footBlockToHeadBlockMap = new int[][] { { 0, 1 },{ -1, 0 }, { 0, -1 }, { 1, 0 } };

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isBedFoot(IBlockAccess world, int x, int y, int z) {
		return true;
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
	public void onBlockPlacedBy(World world, int x, int y, int z,EntityLivingBase player, ItemStack stack) {
		int dir = MathHelper.floor_double((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, dir, 3);
		createNewTileEntity(world);
	}

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3,int par4, int side) {
		if (ForgeDirection.getOrientation(side) == ForgeDirection.UP)return true;
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public int getBedDirection(IBlockAccess world, int x, int y, int z) {
		return super.getBedDirection(world, x, y, z);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileSleepingBag();
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileSleepingBag();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		} else {
			int meta = world.getBlockMetadata(x, y, z);
			
			if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell) {
				if (isBedOccupied(meta)) {
					EntityPlayer entityplayer1 = null;
					Iterator<?> iterator = world.playerEntities.iterator();

					while (iterator.hasNext()) {
						EntityPlayer entityplayer2 = (EntityPlayer) iterator.next();

						if (entityplayer2.isPlayerSleeping()) {
							ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;

							if (chunkcoordinates.posX == x
									&& chunkcoordinates.posY == y
									&& chunkcoordinates.posZ == z) {
								entityplayer1 = entityplayer2;
							}
						}
					}

					if (entityplayer1 != null) {
						player.addChatMessage("tile.bed.occupied");
						return true;
					}

					setBedOccupied(world, x, y, z, false);
				}

				EnumStatus enumstatus = player.sleepInBedAt(x, y, z);

				if (enumstatus == EnumStatus.OK) {
					setBedOccupied(world, x, y, z, true);
					return true;
				} else {
					if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW) {
						player.addChatMessage("tile.bed.noSleep");
					} else if (enumstatus == EnumStatus.NOT_SAFE) {
						player.addChatMessage("tile.bed.notSafe");
					}

					return true;
				}
			} else {
				double d0 = (double) x + 0.5D;
				double d1 = (double) y + 0.5D;
				double d2 = (double) z + 0.5D;
				world.setBlockToAir(x, y, z);
				int k1 = getDirection(meta);
				x += footBlockToHeadBlockMap[k1][0];
				z += footBlockToHeadBlockMap[k1][1];

				if (world.getBlockId(x, y, z) == this.blockID) {
					world.setBlockToAir(x, y, z);
					d0 = (d0 + (double) x + 0.5D) / 2.0D;
					d1 = (d1 + (double) y + 0.5D) / 2.0D;
					d2 = (d2 + (double) z + 0.5D) / 2.0D;
				}

				world.newExplosion((Entity) null, (double) ((float) x + 0.5F),
						(double) ((float) y + 0.5F),
						(double) ((float) z + 0.5F), 5.0F, true, true);
				return true;
			}
		}
	}

	private void setBedOccupied(World world, int x, int y, int z, boolean b) {
		// TODO Auto-generated method stub

	}

	private boolean isBedOccupied(int meta) {
		return (meta & 4) != 0;
	}

	private int getDirection(int i1) {
		return 0;
	}

	@Override
	public void setBedOccupied(World world, int x, int y, int z,EntityPlayer player, boolean occupied) {
		int meta = world.getBlockMetadata(x, y, z);
		meta = occupied ? meta |= 4 : meta & -5;
		world.setBlockMetadataWithNotify(x, y, z, meta, 4);
	}

	@Override
	public boolean isBed(World world, int x, int y, int z,EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		return super.canRenderInPass(pass);
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z,ForgeDirection side) {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,int y, int z) {
		int orientation = blockAccess.getBlockMetadata(x, y, z);
		switch(orientation){
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
			break;
		}
		
	
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return AxisAlignedBB.getAABBPool().getAABB((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
	}	
	

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, start, end);
	}		
}
