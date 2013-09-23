package adventurebackpack.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import adventurebackpack.api.FluidEffect;
import adventurebackpack.api.FluidEffectRegistry;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.config.BlockInfo;
import adventurebackpack.handlers.PacketHandler;
import adventurebackpack.inventory.InventoryItem;
import adventurebackpack.items.ItemHose;
import adventurebackpack.items.Items;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class Actions {

	public static boolean cycling = false;
	public static boolean switching = false;
	public static boolean bedActivated = false;

	public static FluidStack attemptFill(World world, MovingObjectPosition mop, EntityPlayer player, FluidTank tank) {
		try
		{
			if (!world.canMineBlock(player, mop.blockX, mop.blockY, mop.blockZ))
				return null;
			if (!player.canPlayerEdit(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, null))
				return null;
			int fluidID = Utils.isBlockRegisteredAsFluid(world.getBlockId(mop.blockX, mop.blockY, mop.blockZ));
			FluidStack fluid = new FluidStack(fluidID, Constants.bucket);
			// To-do make it dependent on tank name from the hose
			if (fluidID > -1)
			{
				if (tank.getFluid() == null || tank.getFluid().containsFluid(fluid))
				{
					int accepted = tank.fill(fluid, false);
					if (accepted > 0)
					{
						world.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
						return fluid;
					}
				}
			}
		} catch (Exception oops)
		{
			System.out.println("Something bad happened while filling the tank D:");
			oops.printStackTrace();
		}
		return null;
	}

	public static FluidStack attemptPour(EntityPlayer player, World world, int x, int y, int z, FluidTank tank) {
		try
		{
			FluidStack fluid = tank.getFluid();
			if (fluid != null)
			{
				if (fluid.getFluid().canBePlacedInWorld())
				{
					Material material = world.getBlockMaterial(x, y, z);
					boolean flag = !material.isSolid();

					if (!world.isAirBlock(x, y, z) && !flag)
					{
						return null;
					}

					if (world.provider.isHellWorld && fluid.getFluid() == FluidRegistry.WATER)
					{ /* HELL */
						world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F,
								2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
						for (int l = 0; l < 12; ++l)
						{
							world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
						}
					} else
					{
						/* NOT HELL */
						FluidStack drainedFluid = tank.drain(Constants.bucket, false);
						if (drainedFluid != null && drainedFluid.amount >= Constants.bucket)
						{
							if (!world.isRemote && flag && !material.isLiquid())
							{
								world.destroyBlock(x, y, z, true);
							}
							if (fluid.getFluid() == FluidRegistry.WATER)
							{
								world.setBlock(x, y, z, fluid.getFluid().getBlockID() - 1, 0, 3);
							} else
							{
								if (fluid.getFluid() == FluidRegistry.LAVA)
								{
									world.setBlock(x, y, z, fluid.getFluid().getBlockID() - 1, 0, 3);
								} else
								{
									world.setBlock(x, y, z, fluid.getFluid().getBlockID(), 0, 3);
								}
							}
							return drainedFluid;
						}
					}
				}
			}

		} catch (Exception ex)
		{
			System.out.println("Something bad happened when spilling fluid into the world D:");
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean setFluidEffect(World world, EntityPlayer player, FluidTank tank) {
		FluidStack drained = tank.drain(Constants.bucket, false);
		boolean done = false;
		// Map<Integer, FluidEffect> lol =
		// FluidEffectRegistry.getRegisteredFluidEffects();
		if (drained != null && drained.amount >= Constants.bucket)
		{

			for (FluidEffect effect : FluidEffectRegistry.getEffectsForFluid(drained.getFluid()))
			{
				if (effect != null)
				{
					effect.affectDrinker(world, player);
					if (world.isRemote)
					{
						player.sendChatToPlayer(ChatMessageComponent.createFromText(effect.msg));
					}
					done = true;
				}
			}
		}
		return done;
	}

	public static void switchHose(EntityPlayer player, int direction, int slot) {
		// player.inventory.currentItem = slot;
		if (!switching && !cycling)
		{
			switching = true;

			ItemStack hose = player.inventory.mainInventory[slot];
			NBTTagCompound tag = hose.hasTagCompound() ? hose.stackTagCompound : new NBTTagCompound();
			if (direction < 0)
			{
				int mode = ItemHose.getHoseMode(hose);
				mode = (mode + 1) % 3;
				tag.setInteger("mode", mode);
			} else
			{
				int tank = ItemHose.getHoseTank(hose);
				tank = (tank + 1) % 2;
				tag.setInteger("tank", tank);
			}
			hose.setTagCompound(tag);
			switching = false;
		}

	}

	public static void cycleTool(EntityPlayer player, int direction, int slot) {
		if (!cycling && !switching)
		{
			cycling = true;
			InventoryItem backpack = Utils.getBackpackInv(player, true);
			ItemStack current = player.getCurrentEquippedItem();
			if (direction < 0)
			{
				player.inventory.setInventorySlotContents(slot, backpack.getStackInSlot(3));
				backpack.setInventorySlotContents(3, backpack.getStackInSlot(0));
				backpack.setInventorySlotContents(0, current);
			} else
			{
				if (direction > 0)
				{
					player.inventory.mainInventory[slot] = backpack.inventory[0];
					backpack.setInventorySlotContents(0, backpack.getStackInSlot(3));
					backpack.setInventorySlotContents(3, current);
				}
			}
		}
		cycling = false;
	}

	public static String whereTheHellAmI() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER)
		{
			return "Server";
		} else if (side == Side.CLIENT)
		{
			return "Client";
		} else
		{
			return "I don't know";
		}
	}

	/**
	 * This is not being used. Looks for a open 2x1 spot on the ground, deploys
	 * a sleeping bag and sets the player to sleep on it.
	 * 
	 * @param player
	 */
	public static void deploySleepingBagFromPlayer(EntityPlayer player) {
		World world = player.worldObj;
		boolean foundSpot = false;
		Block bed = Block.bed;
		int playerX = (int) player.posX;
		int playerY = (int) player.posY;
		int playerZ = (int) player.posZ;
		int coordX = 0;
		int coordY = 0;
		int coordZ = 0;
		for (int i = playerX - 1; i < playerX + 2; i++)
		{
			for (int j = playerZ - 1; j < playerZ + 2; j++)
			{
				if (!foundSpot && world.isAirBlock(i, playerY, j) && world.isBlockSolidOnSide(i, playerY - 1, j, ForgeDirection.UP)
						&& bed.canPlaceBlockAt(world, i, playerY, j)
						&& (bed.canPlaceBlockAt(world, i + 1, playerY, j) || bed.canPlaceBlockAt(world, i, playerY, j + 1)))
				{
					coordX = i;
					coordZ = j;
					coordY = playerY;
					foundSpot = true;
				}
			}
		}

		if (foundSpot && !world.getBlockMaterial(coordX, coordY, coordZ).isSolid())
		{

			if (world.setBlock(coordX, coordY, coordZ, BlockInfo.SLEEPINGBAG_ID, 5, 3))
			{
				bed.onBlockAdded(world, coordX, coordY, coordZ);
				bed.onBlockPlacedBy(world, coordX, coordY, coordZ, player, new ItemStack(Block.bed, 1));
				bed.onPostBlockPlaced(world, coordX, coordY, coordZ, 8);
				EnumStatus enumstatus = player.sleepInBedAt(coordX, coordY, coordZ);
				if (enumstatus == EnumStatus.OK)
				{
					player.closeScreen();
					bed.setBedOccupied(world, coordX, coordY, coordZ, player, true);
				} else
				{
					if (enumstatus == EnumStatus.NOT_POSSIBLE_NOW)
					{
						player.closeScreen();
						player.addChatMessage("tile.bed.noSleep");
						world.setBlockToAir(coordX, coordY, coordZ);
					} else if (enumstatus == EnumStatus.NOT_SAFE)
					{
						player.closeScreen();
						player.addChatMessage("tile.bed.notSafe");
						world.setBlockToAir(coordX, coordY, coordZ);
					}
				}

			}

			// bed.onBlockActivated(world, bedx, bedy, bedz, player, 0, 0, 0,
			// 0);

		}
	}

	public static int[] canDeploySleepingBag(int coordX, int coordY, int coordZ) {
		World world = Minecraft.getMinecraft().theWorld;
		TileAdvBackpack te = (TileAdvBackpack) world.getBlockTileEntity(coordX, coordY, coordZ);
		int newMeta = -1;

		if (te.isSBDeployed() == false)
		{
			int meta = world.getBlockMetadata(coordX, coordY, coordZ);
			switch (meta)
			{
			case 0:
				--coordZ;
				if (world.isAirBlock(coordX, coordY, coordZ) && world.getBlockMaterial(coordX, coordY - 1, coordZ).isSolid())
				{
					if (world.isAirBlock(coordX, coordY, coordZ - 1) && world.getBlockMaterial(coordX, coordY - 1, coordZ - 1).isSolid())
					{
						newMeta = 2;
					}
				}
				break;
			case 1:
				++coordX;
				if (world.isAirBlock(coordX, coordY, coordZ) && world.getBlockMaterial(coordX, coordY - 1, coordZ).isSolid())
				{
					if (world.isAirBlock(coordX + 1, coordY, coordZ) && world.getBlockMaterial(coordX + 1, coordY - 1, coordZ).isSolid())
					{
						newMeta = 3;
					}
				}
				break;
			case 2:
				++coordZ;
				if (world.isAirBlock(coordX, coordY, coordZ) && world.getBlockMaterial(coordX, coordY - 1, coordZ).isSolid())
				{
					if (world.isAirBlock(coordX, coordY, coordZ + 1) && world.getBlockMaterial(coordX, coordY - 1, coordZ + 1).isSolid())
					{
						newMeta = 0;
					}
				}
				break;
			case 3:
				--coordX;
				if (world.isAirBlock(coordX, coordY, coordZ) && world.getBlockMaterial(coordX, coordY - 1, coordZ).isSolid())
				{
					if (world.isAirBlock(coordX - 1, coordY, coordZ) && world.getBlockMaterial(coordX - 1, coordY - 1, coordZ).isSolid())
					{
						newMeta = 1;
					}
				}
				break;
			default:
				break;
			}
		}
		int result[] = { newMeta, coordX, coordY, coordZ };
		return result;
	}

	/**
	 * Looks for 2x1 space in front of the backpack, and if there's space,
	 * deploys a sleeping bag. If there's a sleeping bag, it retrieves it
	 * instead.
	 * 
	 * @param player
	 *            The player activating this. Used to get the world object.
	 * @param coordX
	 *            X coordinate of the backpack tile entity.
	 * @param coordY
	 *            Y coordinate of the backpack tile entity.
	 * @param coordZ
	 *            Z coordinate of the backpack tile entity.
	 */
	public static void deploySleepingBagFromBackpack(EntityPlayer player, int coordX, int coordY, int coordZ) {
		World world = player.worldObj;
		PacketDispatcher.sendPacketToPlayer(PacketHandler.makePacket(5, 0, coordX, coordY, coordZ), (Player) player);
		TileEntity te = world.getBlockTileEntity(coordX, coordY, coordZ);
		if (te instanceof TileAdvBackpack && !((TileAdvBackpack) te).isSBDeployed())
		{
			int can[] = canDeploySleepingBag(coordX, coordY, coordZ);
			if (can[0] > -1)
			{
				if (((TileAdvBackpack) te).deploySleepingBag(player, world, can[1], can[2], can[3], can[0]))
				{
					player.closeScreen();
				}
			} else if (!world.isRemote)
			{
				player.addChatMessage("Can't deploy the sleeping bag");
			}
		} else
		{
			if(te instanceof TileAdvBackpack && !((TileAdvBackpack) te).removeSleepingBag(world)){
				player.addChatMessage("Can't deploy the sleeping bag");
			}
		}
		te.updateEntity();
		player.closeScreen();

	}

	public static void openCraftingGrid(EntityPlayer player) {

	}

	public static boolean tryPlaceOnDeath(EntityPlayer player) {
		ItemStack backpack = Utils.getWearingBackpack(player);
		if (backpack != null)
		{
			World world = player.worldObj;
			if (backpack.stackTagCompound.getString("colorName").equals("Creeper"))
			{
				BackpackAbilities.instance.itemCreeper(player, world, backpack);
			}
			ChunkCoordinates spawn = getNearestEmptyChunkCoordinates(world, (int) player.posX, (int) player.posY, (int) player.posZ, 10, false);
			if (spawn != null)
			{
				if (Items.advBackpack.placeBackpack(player.inventory.armorInventory[2], player, world, spawn.posX, spawn.posY, spawn.posZ,
						ForgeDirection.UP.ordinal(), false))
				{
					return true;
				}
			}
		}
		return false;
	}

	private static ChunkCoordinates getNearestEmptyChunkCoordinates(World world, int x, int y, int z, int radius, boolean except) {

		for (int i = x; i <= x + radius; ++i)
		{
			for (int j = y; j <= y + (radius / 2); ++j)
			{
				for (int k = z; k <= z + (radius); ++k)
				{
					if (except && world.doesBlockHaveSolidTopSurface(i, j - 1, k) && world.isAirBlock(i, j, k) && !compareCoordinates(x, y, z, i, j, k))
					{
						return new ChunkCoordinates(i, j, k);
					}
					if (!except && world.doesBlockHaveSolidTopSurface(i, j - 1, k) && world.isAirBlock(i, j, k))
					{
						return new ChunkCoordinates(i, j, k);
					}
				}
			}
		}
		return null;
	}

	private static boolean compareCoordinates(int X1, int Y1, int Z1, int X2, int Y2, int Z2) {
		return (X1 == X2 && Y1 == Y2 && Z1 == Z2);
	}

	public static void electrify(EntityPlayer player) {
		ItemStack stack = Utils.getWearingBackpack(player);
		if (stack.stackTagCompound != null)
		{
			if (stack.stackTagCompound.hasKey("color") && stack.stackTagCompound.getString("color").contains("PorkchopRaw"))
			{
				stack.stackTagCompound.setString("color", "Pigman");
				stack.stackTagCompound.setString("colorName", "Zombie Pigman");
			} else if (stack.stackTagCompound.hasKey("color") && !stack.stackTagCompound.getString("color").contains("Pigman"))
			{
				player.inventory.armorInventory[2].stackTagCompound.setString("color", "Electric");
				stack.stackTagCompound.setString("colorName", "Electric");
			}
		}
	}

}
