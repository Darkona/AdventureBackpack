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

	@Instance(ModInformation.ID)
	public static AdventureBackpack instance;

	@SidedProxy(clientSide = "com.darkona.adventurebackpack.proxies.ClientProxy", serverSide = "com.darkona.adventurebackpack.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	public static CreativeTabs AdvBackpackTab;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		registerCreativeTab();
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
		proxy.initCreativeTab();
		proxy.registerCreativeTabLabel();
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.interoperabilityWithOtherMods();
	}
	
	//@SideOnly(Side.CLIENT)
	private void registerCreativeTab(){
		AdvBackpackTab = new CreativeTabs(CreativeTabs.getNextID(), ModInformation.NAME){
			
			@SideOnly(Side.CLIENT)
			public int getTabIconItemIndex(){
				return ABPItems.advBackpack.BlockID; 
			}	
		};
	}
	
}
