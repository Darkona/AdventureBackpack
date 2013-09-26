package com.darkona.adventurebackpack.items;

import com.darkona.adventurebackpack.config.ItemInfo;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class ABPItems {

	public static ItemAdvBackpack advBackpack;
	public static ItemHose hose;
	public static ItemMachete machete;
	public static ItemMiningHelmet miningHat;
	public static ItemPistonBoots pistonboots;

	public static void init() {
		advBackpack = new ItemAdvBackpack(ItemInfo.AB_ID);
		hose = new ItemHose(ItemInfo.HOSE_ID);
		machete = new ItemMachete(ItemInfo.MACHETE_ID);
		miningHat = new ItemMiningHelmet(ItemInfo.HELMET_ID);
		pistonboots = new ItemPistonBoots(ItemInfo.BOOTS_ID);
	}

	public static void addNames() {
		LanguageRegistry.addName(advBackpack, ItemInfo.AB_NAME);
		LanguageRegistry.addName(hose, ItemInfo.HOSE_NAME);
		LanguageRegistry.addName(machete, ItemInfo.MACHETE_NAME);
		LanguageRegistry.addName(miningHat, ItemInfo.HELMET_NAME);
		LanguageRegistry.addName(pistonboots, ItemInfo.BOOTS_NAME);
	}
}
