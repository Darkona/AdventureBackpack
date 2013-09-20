package adventurebackpack.proxies;

import net.minecraftforge.client.MinecraftForgeClient;
import adventurebackpack.blocks.Blocks;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.blocks.tileentities.TileSleepingBag;
import adventurebackpack.client.render.RendererAdvBackpack;
import adventurebackpack.client.render.RendererHose;
import adventurebackpack.client.render.RendererItemAdvBackpack;
import adventurebackpack.client.render.RendererSleepingBag;
import adventurebackpack.handlers.ClientTickHandler;
import adventurebackpack.handlers.KeyBindHandler;
import adventurebackpack.items.Items;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{
	
	private static ClientTickHandler clientTickHandler;
	private static KeyBindHandler keybindhandler;
	@Override
	public void initSounds() {
	}

	@Override
	public void registerStuff() {
		clientTickHandler = new ClientTickHandler();
        TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);
        

		keybindhandler = new KeyBindHandler();
		KeyBindingRegistry.registerKeyBinding(keybindhandler);
	
		super.registerStuff();	
	}
	
	public void registerKeyBinding(){
	}
	
	@Override
	public void initRenderers() {
		 
		 ClientRegistry.bindTileEntitySpecialRenderer(TileSleepingBag.class, new RendererSleepingBag());
		 ClientRegistry.bindTileEntitySpecialRenderer(TileAdvBackpack.class, new RendererAdvBackpack());
		 MinecraftForgeClient.registerItemRenderer(Items.hose.itemID, new RendererHose());
		 MinecraftForgeClient.registerItemRenderer(Items.advBackpack.itemID, new RendererItemAdvBackpack());
		 MinecraftForgeClient.registerItemRenderer(Blocks.advbackpack.blockID, new RendererItemAdvBackpack());
	}

}
