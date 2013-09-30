package com.darkona.adventurebackpack.proxies;

import net.minecraftforge.common.MinecraftForge;

import com.darkona.adventurebackpack.api.FluidEffectRegistry;
import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.entity.ABPEntities;
import com.darkona.adventurebackpack.entity.EntityRideableSpider;
import com.darkona.adventurebackpack.fluids.Fluids;
import com.darkona.adventurebackpack.handlers.EventHandler;
import com.darkona.adventurebackpack.handlers.PacketHandler;
import com.darkona.adventurebackpack.handlers.RecipeHandler;
import com.darkona.adventurebackpack.items.ABPItems;

import cpw.mods.fml.common.event.FMLInterModComms;

public class CommonProxy {

	public static PacketHandler packethandler;
	
	
	public void initSounds() {

	}

	public void initRenderers() {
	}

	public void registerPacketHandling() {
		packethandler = new PacketHandler();
	}

	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public void initBlocks() {
		ABPBlocks.init();
		ABPBlocks.registerBlocks();
		ABPBlocks.addNames();
		ABPBlocks.registerTileEntities();
		FluidEffectRegistry.init();
	}

	public void initFluids() {
		Fluids.init();
	}

	public void registerItems() {
		ABPItems.init();
		ABPItems.addNames();
	}

	public void registerRecipes() {
		RecipeHandler.init();
		
	}

	public void registerEntities(){
		ABPEntities.init();
	}
	
	public void initCreativeTab(){
		
	
	}
	
	public void registerCreativeTabLabel(){}
	
	public void interoperabilityWithOtherMods() {
		FMLInterModComms.sendMessage("AppliedEnergistics", "moveabletile", TileAdvBackpack.class.getName());
		
		//Don't morph into this thing if you have Morph. It's pointless.
		try {
			Class.forName("morph.common.core.ApiHandler").getDeclaredMethod("blacklistEntity", Class.class).invoke(null, EntityRideableSpider.class);
		} catch (Exception e) {
		}
	}
}
