package com.darkona.adventurebackpack;

import net.minecraft.creativetab.CreativeTabs;

import com.darkona.adventurebackpack.handlers.ConfigHandler;
import com.darkona.adventurebackpack.handlers.GuiHandler;
import com.darkona.adventurebackpack.items.ABPItems;
import com.darkona.adventurebackpack.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, acceptedMinecraftVersions = ModInformation.MCVERSION)
@NetworkMod(channels = { ModInformation.CHANNEL }, clientSideRequired = true, serverSideRequired = true, packetHandler = com.darkona.adventurebackpack.handlers.PacketHandler.class)
public class AdventureBackpack {

	public static CreativeTabs AdvBackpackTab;
	
	@Instance(ModInformation.ID)
	public static AdventureBackpack instance;

	@SidedProxy(clientSide = "com.darkona.adventurebackpack.proxies.ClientProxy", serverSide = "com.darkona.adventurebackpack.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void load(FMLInitializationEvent e) {
		AdvBackpackTab = new CreativeTabs("AdventureBackpack"){
			@SideOnly(Side.CLIENT)
			public int getTabIconItemIndex(){
				return ABPItems.advBackpack.BlockID; 
			}
		};
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.init(event.getSuggestedConfigurationFile());
	}

	
	@EventHandler
	public void init(FMLInitializationEvent event) {

		proxy.initFluids();
		proxy.initBlocks();
		proxy.registerItems();
		new GuiHandler();
		proxy.initSounds();
		proxy.initRenderers();
		proxy.registerEvents();
		proxy.registerRecipes();
		proxy.registerEntities();
		proxy.registerPacketHandling();

		LanguageRegistry.instance().addStringLocalization(AdvBackpackTab.getTranslatedTabLabel(), "Adventure Backpack");

	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.interoperabilityWithOtherMods();
	}
	
	
}
