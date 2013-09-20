package adventurebackpack.proxies;

import net.minecraftforge.common.MinecraftForge;
import adventurebackpack.api.FluidEffectRegistry;
import adventurebackpack.blocks.Blocks;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.fluids.Fluids;
import adventurebackpack.handlers.EventHandler;
import adventurebackpack.handlers.PacketHandler;
import adventurebackpack.items.Items;
import adventurebackpack.items.recipes.RecipeBackpackDye;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy{

	public static PacketHandler packethandler;
	public void initSounds() {
		
	}

	
	public void initRenderers() {
	}
	
	public void registerStuff(){
		
		packethandler = new PacketHandler();
	}
	
	public void registerEvents(){
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	
	public void initBlocks(){
		Blocks.init();
		Blocks.registerBlocks();
		Blocks.addNames();
		Blocks.registerTileEntities();
		FluidEffectRegistry.init();
		
	}

	public void initFluids(){
		Fluids.init();
	}
	
	public void registerItems(){
		Items.init();
		Items.addNames();
	}
	
	public void registerRecipes(){
		GameRegistry.addRecipe(new RecipeBackpackDye());
	}
	
	public void interoperabilityWithOtherMods(){
		FMLInterModComms.sendMessage( "AppliedEnergistics", "moveabletile", TileAdvBackpack.class.getName() );
	}
}
