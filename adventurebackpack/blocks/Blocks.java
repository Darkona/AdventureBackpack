package adventurebackpack.blocks;

import net.minecraft.block.Block;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.blocks.tileentities.TileKamikaze;
import adventurebackpack.blocks.tileentities.TileSleepingBag;
import adventurebackpack.config.BlockInfo;
import adventurebackpack.config.ItemInfo;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {

	public static Block advbackpack;
	public static Block sleepingbag;
    public static Block lightblock;
	
	public static void init() {
		advbackpack = new BlockAdvBackpack(BlockInfo.ADVBACKPACK_ID);
		sleepingbag = new BlockSleepingBag(BlockInfo.SLEEPINGBAG_ID);
		lightblock = new BlockLight(BlockInfo.LIGHTBLOCK_ID);
	}

	public static void registerBlocks() {
		GameRegistry.registerBlock(advbackpack, BlockInfo.ADVBACKPACK_KEY);
		GameRegistry.registerBlock(sleepingbag, BlockInfo.SLEEPINGBAG_KEY);
		GameRegistry.registerBlock(lightblock, BlockInfo.LIGHTBLOCK_KEY);
	}

	public static void addNames() {
		LanguageRegistry.addName(advbackpack, ItemInfo.AB_NAME);
		LanguageRegistry.addName(sleepingbag, BlockInfo.SLEEPINGBAG_NAME);
		LanguageRegistry.addName(lightblock, BlockInfo.LIGHTBLOCK_NAME);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileAdvBackpack.class, "adventurebackpacktileentity");
		GameRegistry.registerTileEntity(TileSleepingBag.class, "sleepingbagtileentity");
		GameRegistry.registerTileEntity(TileKamikaze.class, "lighttileentity");
	}
}
