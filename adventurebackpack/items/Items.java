package adventurebackpack.items;

import adventurebackpack.config.ItemInfo;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Items {

	public static ItemAdvBackpack advBackpack;
	public static Hose hose;
	public static ItemMachete machete;

	public static void init() {
		advBackpack = new ItemAdvBackpack(ItemInfo.AB_ID);
		hose = new Hose(ItemInfo.HOSE_ID, 0);
		machete = new ItemMachete(ItemInfo.MACHETE_ID);
	}

	public static void addNames() {
		LanguageRegistry.addName(advBackpack, ItemInfo.AB_NAME);
		LanguageRegistry.addName(hose, ItemInfo.HOSE_NAME);
		LanguageRegistry.addName(machete, ItemInfo.MACHETE_NAME);
	}
}
