package com.darkona.adventurebackpack.proxies;

import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.blocks.tileentities.TileSleepingBag;
import com.darkona.adventurebackpack.client.render.RendererAdvBackpack;
import com.darkona.adventurebackpack.client.render.RendererHose;
import com.darkona.adventurebackpack.client.render.RendererItemAdvBackpack;
import com.darkona.adventurebackpack.client.render.RendererMiningHat;
import com.darkona.adventurebackpack.client.render.RendererSleepingBag;
import com.darkona.adventurebackpack.handlers.ClientTickHandler;
import com.darkona.adventurebackpack.handlers.KeyBindHandler;
import com.darkona.adventurebackpack.items.ABPItems;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	private static ClientTickHandler clientTickHandler;
	private static KeyBindHandler keybindhandler;
	
	@Override
	public void initSounds() {
	}

	@Override
	public void registerPacketHandling() {
		clientTickHandler = new ClientTickHandler();
		TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);

		keybindhandler = new KeyBindHandler();
		KeyBindingRegistry.registerKeyBinding(keybindhandler);

		super.registerPacketHandling();
	}

	public void registerKeyBinding() {
	}

	@Override
	public void initRenderers() {

		ClientRegistry.bindTileEntitySpecialRenderer(TileSleepingBag.class, new RendererSleepingBag());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAdvBackpack.class, new RendererAdvBackpack());
		MinecraftForgeClient.registerItemRenderer(ABPItems.hose.itemID, new RendererHose());
		MinecraftForgeClient.registerItemRenderer(ABPItems.advBackpack.itemID, new RendererItemAdvBackpack());
		MinecraftForgeClient.registerItemRenderer(ABPItems.miningHat.itemID, new RendererMiningHat());
		MinecraftForgeClient.registerItemRenderer(ABPBlocks.advbackpack.blockID, new RendererItemAdvBackpack());
	}

}
