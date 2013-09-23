package adventurebackpack;

import adventurebackpack.handlers.ConfigHandler;
import adventurebackpack.handlers.GuiHandler;
import adventurebackpack.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION, acceptedMinecraftVersions = ModInformation.MCVERSION)
@NetworkMod(channels = { ModInformation.CHANNEL }, clientSideRequired = true, serverSideRequired = true, packetHandler = adventurebackpack.handlers.PacketHandler.class)
public class AdventureBackpack {

	@Instance(ModInformation.ID)
	public static AdventureBackpack instance;

	@SidedProxy(clientSide = "adventurebackpack.proxies.ClientProxy", serverSide = "adventurebackpack.proxies.CommonProxy")
	public static CommonProxy proxy;

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
		proxy.registerStuff();

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
