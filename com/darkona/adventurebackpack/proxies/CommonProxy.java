package com.darkona.adventurebackpack.proxies;

import com.darkona.adventurebackpack.api.FluidEffectRegistry;
import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.entity.ABPEntities;
import com.darkona.adventurebackpack.fluids.Fluids;
import com.darkona.adventurebackpack.handlers.EventHandler;
import com.darkona.adventurebackpack.handlers.PacketHandler;
import com.darkona.adventurebackpack.items.ABPItems;
import com.darkona.adventurebackpack.items.recipes.RecipeBackpackDye;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

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
		GameRegistry.addRecipe(new RecipeBackpackDye());
	}

	public void registerEntities(){
		ABPEntities.init();
	}
	
	public void interoperabilityWithOtherMods() {
		FMLInterModComms.sendMessage("AppliedEnergistics", "moveabletile", TileAdvBackpack.class.getName());
	}
}
