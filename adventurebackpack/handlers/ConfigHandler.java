package adventurebackpack.handlers;

import java.io.File;

import net.minecraftforge.common.Configuration;
import adventurebackpack.config.BlockInfo;
import adventurebackpack.config.ItemInfo;

public class ConfigHandler {

	private static final String ITEM_COMMENT = "Sets the IDs for the mod's items.";
	private static final String BLOCK_COMMENT = "Sets the IDs for the mod's blocks.";

	public static void init(File file) {
		Configuration config = new Configuration(file);

		config.load();
		/*
		 * ======================================= BLOCKS
		 * =======================================
		 */
		{
			config.addCustomCategoryComment(Configuration.CATEGORY_BLOCK, BLOCK_COMMENT);
			BlockInfo.ADVBACKPACK_ID = config.getBlock(BlockInfo.ADVBACKPACK_KEY, BlockInfo.ADVBACKPACK_DEFAULT).getInt();
			BlockInfo.SLEEPINGBAG_ID = config.getBlock(BlockInfo.SLEEPINGBAG_KEY, BlockInfo.SLEEPINGBAG_DEFAULT).getInt();
			BlockInfo.LIGHTBLOCK_ID = config.getBlock(BlockInfo.LIGHTBLOCK_KEY, BlockInfo.LIGHTBLOCK_DEFAULT).getInt();
		}
		/*
		 * ======================================= ITEMS
		 * =======================================
		 */
		{
			config.addCustomCategoryComment(Configuration.CATEGORY_ITEM, ITEM_COMMENT);
			ItemInfo.AB_ID = config.getItem(ItemInfo.AB_KEY, ItemInfo.AB_DEFAULT).getInt() - 256;
			ItemInfo.HOSE_ID = config.getItem(ItemInfo.HOSE_KEY, ItemInfo.HOSE_DEFAULT).getInt() - 256;
			ItemInfo.MACHETE_ID = config.getItem(ItemInfo.MACHETE_KEY, ItemInfo.MACHETE_DEFAULT).getInt() - 256;
			ItemInfo.HELMET_ID = config.getItem(ItemInfo.HELMET_KEY, ItemInfo.HELMET_DEFAULT).getInt() - 256;
		}
		config.save();

	}

}
