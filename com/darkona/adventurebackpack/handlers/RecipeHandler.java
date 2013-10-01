package com.darkona.adventurebackpack.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.darkona.adventurebackpack.items.ABPItems;
import com.darkona.adventurebackpack.items.recipes.RecipeBackpackDye;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandler {

	public static void init(){
		//Backpack Colors
		GameRegistry.addRecipe(new RecipeBackpackDye());
		
		//Piston Boots
		GameRegistry.addRecipe(new ItemStack(ABPItems.pistonboots,1),
				"PRP",
				"BGB", 
				'P', new ItemStack(Block.pistonBase,1),
				'R', new ItemStack(Item.redstone,1),
				'B', new ItemStack(Item.bootsLeather,1),
				'G', new ItemStack(Item.ingotGold,1)
				);
		
		//Adventure suit
		GameRegistry.addRecipe(new ItemStack(ABPItems.advsuit),
			"LCL",
			"GPG", 
			'C', new ItemStack(Block.cloth,1,0),
			'L', new ItemStack(Item.leather,1),
			'P', new ItemStack(Item.legsLeather,1),
			'G', new ItemStack(Item.ingotGold,1)
		);
		
		//Sleeping Bag
		GameRegistry.addRecipe(new ItemStack(ABPItems.bpcomponent,1,1),
				"  C",
				"XXX", 
				'C', new ItemStack(Block.cloth,1,0),
				'X', new ItemStack(Block.carpet,1,14)
			);
		
		//Backpack Tank
		GameRegistry.addRecipe(new ItemStack(ABPItems.bpcomponent,1,2),
				"III",
				"PGP",
				"III",
				'I', new ItemStack(Item.ingotIron,1),
				'P', new ItemStack(Block.thinGlass,1),
				'G', new ItemStack(Block.glass,1)
			);
		
		//Adventure Backpack
		GameRegistry.addRecipe(new ItemStack(ABPItems.advBackpack,1),
				"IEI",
				"TWT",
				"LBL",
				'I', new ItemStack(Item.ingotIron,1),
				'E', new ItemStack(Item.enderPearl,1),
				'T', new ItemStack(ABPItems.bpcomponent,1,2),
				'W', new ItemStack(Block.workbench,1),
				'L', new ItemStack(Item.leather,1),
				'B', new ItemStack(ABPItems.bpcomponent,1,1)
			);
		
		//Hose Head
		GameRegistry.addRecipe(new ItemStack(ABPItems.bpcomponent,1,3),
				" I ",
				"G G",
				"GCG",
				'I', new ItemStack(Item.ingotIron,1),
				'G', new ItemStack(Item.ingotGold,1),
				'C', new ItemStack(Item.dyePowder,1,2)
			);
		
		//Helmet
		GameRegistry.addRecipe(new ItemStack(ABPItems.miningHat,1),
				"GIG",
				"RLR",
				"IBI",
				'I', new ItemStack(Item.ingotIron,1),
				'G', new ItemStack(Item.ingotGold,1),
				'R', new ItemStack(Item.redstone,1),
				'B', new ItemStack(Block.lever,1),
				'L', new ItemStack(Block.redstoneLampIdle,1)
			);
		
		//Hose
		GameRegistry.addRecipe(new ItemStack(ABPItems.hose,1),
				" H ",
				"CEC",
				"C C",
				'H', new ItemStack(ABPItems.bpcomponent,1,3),
				'C', new ItemStack(Item.dyePowder,1,2),
				'E', new ItemStack(Item.enderPearl,1)
			);
	}
}
