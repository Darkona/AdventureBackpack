package com.darkona.adventurebackpack.blocks;

import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.blocks.tileentities.TileKamikaze;
import com.darkona.adventurebackpack.blocks.tileentities.TileSleepingBag;
import com.darkona.adventurebackpack.config.BlockInfo;
import com.darkona.adventurebackpack.config.ItemInfo;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ABPBlocks {

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
