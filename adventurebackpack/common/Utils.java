package adventurebackpack.common;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.inventory.InventoryItem;
import adventurebackpack.items.ItemAdvBackpack;
import adventurebackpack.items.ItemMiningHat;
import adventurebackpack.items.ItemPistonBoots;

public class Utils {

	public static boolean isWearingHelmet(EntityPlayer player){
		return player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].getItem() instanceof ItemMiningHat;
	}
	
	public static boolean isWearingBackpack(EntityPlayer player) {
		return player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() instanceof ItemAdvBackpack;
	}

	public static boolean isHoldingBackpack(EntityPlayer player) {
		return player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemAdvBackpack;
	}

	public static boolean isWearingBoots(EntityPlayer player){
		return player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() instanceof ItemPistonBoots;
	}
	
	
	public static ItemStack getWearingHelmet(EntityPlayer player) {
		if(isWearingHelmet(player))return player.inventory.armorInventory[3];
		return null;
	}
	
    public static ItemStack getWearingBackpack(EntityPlayer player) {
		if (isWearingBackpack(player))
			return player.inventory.armorInventory[2];
		return null;
	}

	public static ItemStack getHoldingBackpack(EntityPlayer player) {
		if (isHoldingBackpack(player))
			return player.inventory.getCurrentItem();
		return null;
	}

	public static ItemStack getWearingBoots(EntityPlayer player){
		return isWearingBoots(player) ? player.inventory.armorInventory[0] : null ;
	}
	
	/**
	 * Analyzes the Fluid registry for matches of a fluid with a blockID equal
	 * to the blockId provided as parameter.
	 * 
	 * @param BlockID
	 *            The ID of the block.
	 * @return If the block exists as a Fluid in the Fluid Registry, return the
	 *         Fluid ID. Else, return -1.
	 */
	public static int isBlockRegisteredAsFluid(int blockID) {
		/*
		 * for (Map.Entry<String,Fluid> fluid :
		 * getRegisteredFluids().entrySet()) { int ID =
		 * (fluid.getValue().getBlockID() == BlockID) ? fluid.getValue().getID()
		 * : -1; if (ID > 0) return ID; }
		 */
		int fluidID = -1;
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			fluidID = (fluid.getBlockID() == blockID) ? fluid.getID() : -1;
			if (fluidID > 0)
				return fluidID;
		}
		return fluidID;
	}

	public static boolean shouldGiveEmpty(ItemStack cont) {
		boolean valid = true;
		// System.out.println("Item class name is: "+cont.getItem().getClass().getName());

		try
		{
			// Industrialcraft cells
			if (apis.ic2.api.item.Items.getItem("cell").getClass().isInstance(cont.getItem()))
			{
				valid = false;
			}
			// Forestry capsules
			if (java.lang.Class.forName("forestry.core.items.ItemLiquidContainer").isInstance(cont.getItem()))
			{
				valid = false;
			}
		} catch (Exception oops)
		{

		}
		// Others

		return valid;
	}

	public static ResourceLocation getBackpackColor(ItemStack item) {
		ResourceLocation color;
		if (item.stackTagCompound == null)
		{
			item.stackTagCompound = new NBTTagCompound();
		}
		if (!item.stackTagCompound.hasKey("color") || item.stackTagCompound.getString("color").isEmpty())
		{
			item.stackTagCompound.setString("color", "Standard");
		}
		color = new ResourceLocation("adventurebackpack", "textures/backpack/backpack" + item.stackTagCompound.getString("color") + ".png");
		return color;
	}

	public static ResourceLocation getBackpackColor(TileAdvBackpack tile) {
		ResourceLocation color;
		if (tile.getColor().isEmpty())
		{
			tile.setColor("Standard");
		}
		color = new ResourceLocation("adventurebackpack", "textures/backpack/backpack" + tile.getColor() + ".png");
		return color;
	}

	public static boolean getPossibleDyes(ItemStack stack) {
		if (OreDictionary.getOreName(OreDictionary.getOreID(stack)).contains("dye"))
		{
			return true;
		}
		for (Block blej : blockies)
		{
			if (stack.getItem().itemID == blej.blockID)
				return true;
		}

		for (Item lol : items)
		{
			if (stack.getItem() == lol)
				return true;
		}
		return false;
	}

	public static Block blockies[] = { Block.melon, Block.cactus, Block.glowStone, Block.blockRedstone, Block.snow, Block.sponge, Block.obsidian,
			Block.sandStone, Block.chest, Block.coalBlock, Block.hay, Block.pumpkin, Block.blockNetherQuartz, Block.blockEmerald, Block.blockDiamond,
			Block.blockIron, Block.blockSnow, Block.bookShelf, Block.blockGold, Block.cloth, Block.dragonEgg, Block.enchantmentTable, Block.mushroomRed,
			Block.mushroomBrown, Block.blockLapis };

	public static Item[] items = {

	Item.blazeRod, 
	Item.eyeOfEnder, 
	Item.feather, 
	Item.spiderEye, 
	Item.slimeBall, 
	Item.egg, 
	Item.cake, 
	Item.leather, 
	Item.skull, 
	Item.beefRaw, 
	Item.porkRaw,
	Item.cookie, 
	Item.enderPearl, 
	Item.magmaCream, 
	Item.netherStar, 
	Item.bone, 
	Item.fishRaw, 
	Item.ghastTear, 
	Item.bowlSoup, 
	Item.netherStalkSeeds,
	Item.recordCat
	};

	@SuppressWarnings("serial")
	public static HashMap<String, String> itemNames = new HashMap<String, String>() {
		{
			put("BlazeRod", "Blaze");
			put("BeefRaw", "Cow");
			put("Bone", "Wolf");
			put("Skull.creeper", "Creeper");
			put("Skull.skeleton", "Skeleton");
			put("Skull.zombie", "Zombie");
			put("Skull.wither", "Wither Skeleton");
			put("Slimeball", "Slime");
			put("MagmaCream", "Magma Cube");
			put("SpiderEye", "Spider");
			put("GhastTear", "Ghast");
			put("Feather", "Chicken");
			put("EyeOfEnder", "End");
			put("HayBlock", "Hay");
			put("FishRaw", "Ocelot");
			put("DragonEgg", "Dragon");
			put("NetherStar", "Wither");
			put("Pigman", "Zombie Pigman");
			put("MushroomBrown", "Brown Mushroom");
			put("MushroomRed", "Red Mushroom");
			put("PorkchopRaw", "Pig");
			put("BookShelf", "Books");
			put("BlockCoal", "Coal");
			put("BlockDiamond", "Diamond");
			put("BlockEmerald", "Emerald");
			put("BlockRedstone", "Redstone");
			put("BlockLapis", "Lapislazuli");
			put("QuartzBlock", "Quartz");
			put("Cloth", "Sheep");
			put("Lightgem", "Glowstone");
			put("EnchantmentTable", "Deluxe");
			put("Rainbow", "Nyan!");
			put("EnderPearl", "Enderman");
			put("MushroomStew", "Mooshroom");
			put("BlockGold", "Golden");
			put("BlockIron", "Iron");
			put("NetherStalkSeeds", "Nether");
			put("Rainbow", "Nyan!");
		}
	};

	public static ChunkCoordinates findBlock(World world, int x, int y, int z, int BlockID, int range) {
		for (int i = x - range; i <= x + range; i++)
		{
			for (int j = z - range; j <= z + range; j++)
			{
				if (world.getBlockId(i, y, j) == BlockID)
				{
					return new ChunkCoordinates(i, y, j);
				}
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static String getDisplayNameForColor(String color) {
		for (Entry entry : itemNames.entrySet())
		{
			if (((String) entry.getKey()).equals(color))
			{
				return (String) entry.getValue();
			}
		}
		return color;
	}

	public static String capitalize(String s) {
		// Character.toUpperCase(itemName.charAt(0)) + itemName.substring(1);
		return s.substring(0, 1).toUpperCase().concat(s.substring(1));
	}

	public static int getOppositeCardinalFromMeta(int meta){
		return (meta % 2 == 0) ? (meta == 0) ? 2 : 0 : ((meta + 1) % 4)+1;
	}
	
	//This is some magic that gives a block or entity as far as the argument reach goes.
	public static MovingObjectPosition getMovingObjectPositionFromPlayersHat(World world,EntityPlayer player, boolean flag, double reach) {
			float f = 1.0F;
			float playerPitch = player.prevRotationPitch
					+ (player.rotationPitch - player.prevRotationPitch) * f;
			float playerYaw = player.prevRotationYaw
					+ (player.rotationYaw - player.prevRotationYaw) * f;
			double playerPosX = player.prevPosX + (player.posX - player.prevPosX)
					* f;
			double playerPosY = (player.prevPosY + (player.posY - player.prevPosY)
					* f + 1.6200000000000001D)
					- player.yOffset;
			double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ)
					* f;
			Vec3 vecPlayer = Vec3.createVectorHelper(playerPosX, playerPosY,
					playerPosZ);
			float cosYaw = (float) Math.cos(-playerYaw * 0.01745329F - 3.141593F);
			float sinYaw = (float) Math.sin(-playerYaw * 0.01745329F - 3.141593F);
			float cosPitch = (float) -Math.cos(-playerPitch * 0.01745329F);
			float sinPitch = (float) Math.sin(-playerPitch * 0.01745329F);
			float pointX = sinYaw * cosPitch;
			float pointY = sinPitch;
			float pointZ = cosYaw * cosPitch;
			Vec3 vecPoint = vecPlayer.addVector(pointX * reach, pointY * reach,
					pointZ * reach);
			MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do_do(
					vecPlayer, vecPoint, flag, !flag);
			return movingobjectposition;
		}
	
	/**
	 * Will return a backpack inventory from a backpack on the player's armor
	 * slot if true, or from his hand if false;
	 * 
	 * @param player
	 * @param wearing
	 *            wether to take the inventory from a backpack on back or on
	 *            hand.
	 * @return
	 */
	public static InventoryItem getBackpackInv(EntityPlayer player, boolean wearing) {
		return new InventoryItem((wearing) ? player.inventory.armorItemInSlot(2) : player.inventory.getCurrentItem());
	}

	public static String printCoordinates(int x, int y, int z){
		return "X= " + x + ", Y= " + y + ", Z= " + z;
	}

	
}
