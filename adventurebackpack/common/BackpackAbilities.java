package adventurebackpack.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.inventory.InventoryItem;

public class BackpackAbilities {

	public static BackpackAbilities instance = new BackpackAbilities();

	public static boolean hasAbility(String colorName) {
		for (String valid : validWearingBackpacks) {
			if (valid.equals(colorName))
				return true;
		}
		return false;
	}

	public void executeAbility(String colorName, EntityPlayer player, World world, Object backpack) {
		if (backpack instanceof ItemStack && player != null) {
			for (String valid : validWearingBackpacks) {
				if (valid.equals(colorName)) {
					try {
						this.getClass().getMethod("item" + valid, EntityPlayer.class, World.class, ItemStack.class)
								.invoke(instance, player, world, backpack);
					} catch (Exception oops) {
						// oops.printStackTrace(); Discard silently, nobody
						// cares.
					}
				}
			}
		} else if (backpack instanceof TileAdvBackpack) {
			for (String valid : validTileBackpacks) {
				if (valid.equals(colorName)) {
					try {
						this.getClass().getMethod("tile" + valid, World.class, TileAdvBackpack.class)
								.invoke(instance, world, backpack);
					} catch (Exception oops) {
						// oops.printStackTrace(); Discard silently, nobody
						// cares.
					}
				}
			}
		}
	}

	private static String[] validWearingBackpacks = { "Cactus", "Cow", "Pig", "Dragon", "Slime", "Chicken" };

	private static String[] validTileBackpacks = { "Cactus" };

	private int toTicks(int seconds) {
		return seconds * 20;
	}

	private boolean isUnderRain(EntityPlayer player) {
		return player.worldObj.canLightningStrikeAt(MathHelper.floor_double(player.posX),
				MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ))
				|| player.worldObj.canLightningStrikeAt(MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY + player.height), MathHelper.floor_double(player.posZ));
	}

	public void itemCactus(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {
			int lastDropTime = (backpack.stackTagCompound.hasKey("lastTime")) ? backpack.stackTagCompound
					.getInteger("lastTime") - 1 : 5;

			int drops = 0;
			if (player.isInWater())
				drops += 2;
			if (isUnderRain(player))
				drops += 1;

			if (lastDropTime <= 0 && drops > 0) {
				InventoryItem inv = Actions.getBackpackInv(player, true);
				FluidStack raindrop = new FluidStack(FluidRegistry.WATER, drops);
				inv.leftTank.fill(raindrop, true);
				inv.rightTank.fill(raindrop, true);
				inv.onInventoryChanged();
				lastDropTime = 5;
			}
			backpack.stackTagCompound.setInteger("lastTime", lastDropTime);
		}
	}

	public void itemPig(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {
			int oinkTime = backpack.stackTagCompound.hasKey("lastTime") ? backpack.stackTagCompound
					.getInteger("lastTime") - 1 : toTicks(30);
			if (oinkTime <= 0) {
				world.playSoundAtEntity(player, "mob.pig.say", 0.8f, 1f);
				oinkTime = toTicks(world.rand.nextInt(31) + 30);
			}
			backpack.stackTagCompound.setInteger("lastTime", oinkTime);
		}
	}

	public void itemSlime(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {
			if (player.onGround && player.isSprinting()) {
				int i = 1;
				for (int j = 0; j < i * 2; ++j) {
					float f = world.rand.nextFloat() * (float) Math.PI * 2.0F;
					float f1 = world.rand.nextFloat() * 0.5F + 0.5F;
					float f2 = MathHelper.sin(f) * i * 0.5F * f1;
					float f3 = MathHelper.cos(f) * i * 0.5F * f1;
					world.spawnParticle("slime", player.posX + f2, player.boundingBox.minY, player.posZ + f3, 0.0D,
							0.0D, 0.0D);
				}
				int slimeTime = backpack.stackTagCompound.hasKey("lastTime") ? backpack.stackTagCompound
						.getInteger("lastTime") - 1 : 5;
				if (slimeTime <= 0) {
					world.playSoundAtEntity(player, "mob.slime.small", 0.4F,
							(world.rand.nextFloat() - world.rand.nextFloat()) * 1F);
					slimeTime = 5;
				}
				backpack.stackTagCompound.setInteger("lastTime", slimeTime);
			}
		}

	}

	public void itemChicken(EntityPlayer player, World world, ItemStack backpack) {

		if (Utils.isWearing(player)) {
			int eggTime = backpack.stackTagCompound.hasKey("lastTime") ? backpack.stackTagCompound
					.getInteger("lastTime") - 1 : toTicks(300);
			if (eggTime <= 0) {
				player.playSound("mob.chicken.plop", 1.0F,
						(world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F + 1.0F);
				if (!world.isRemote)
					player.dropItem(Item.egg.itemID, 1);
				eggTime = toTicks(world.rand.nextInt(301) + 300);
			}
			backpack.stackTagCompound.setInteger("lastTime", eggTime);
		}
	}

	public void itemMelon(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {
			int lastDropTime = (backpack.stackTagCompound.hasKey("lastTime")) ? backpack.stackTagCompound
					.getInteger("lastTime") - 1 : 5;

			int drops = 0;
			if (player.isInWater())
				drops += 5;
			if (isUnderRain(player))
				drops += 2;

			if (lastDropTime <= 0 && drops > 0) {
				InventoryItem inv = Actions.getBackpackInv(player, true);
				FluidStack raindrop = new FluidStack(FluidRegistry.getFluid("melonJuice"), drops);
				inv.leftTank.fill(raindrop, true);
				inv.rightTank.fill(raindrop, true);
				inv.onInventoryChanged();
				lastDropTime = 5;
			}
			backpack.stackTagCompound.setInteger("lastTime", lastDropTime);
		}
	}

	public void itemDragon(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {

		}
	}

	public void itemCow(EntityPlayer player, World world, ItemStack backpack) {
		if (Utils.isWearing(player)) {
			int oinkTime = backpack.stackTagCompound.hasKey("lastTime") ? backpack.stackTagCompound
					.getInteger("lastTime") - 1 : toTicks(30);
			if (oinkTime <= 0) {
				world.playSoundAtEntity(player, "mob.cow.say", 0.8f, 1f);
				oinkTime = toTicks(world.rand.nextInt(31) + 30);
			}
			backpack.stackTagCompound.setInteger("lastTime", oinkTime);
		}
	}

	/*
	 * ==================================== TILE ABILITIES
	 * ==========================================
	 */

	public void tileCactus(World world, TileAdvBackpack backpack) {
		if (world.isRaining() && world.canBlockSeeTheSky(backpack.xCoord, backpack.yCoord, backpack.zCoord)) {
			int dropTime = backpack.lastTime - 1;
			if (dropTime <= 0) {
				FluidStack raindrop = new FluidStack(FluidRegistry.WATER, 2);
				backpack.leftTank.fill(raindrop, true);
				backpack.rightTank.fill(raindrop, true);
				dropTime = 5;
			}
			backpack.lastTime = dropTime;
			backpack.onInventoryChanged();
		}
	}
}
