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
				'P', new ItemStack(Block.pistonBase),
				'R', new ItemStack(Item.redstone),
				'B', new ItemStack(Item.bootsLeather),
				'G', new ItemStack(Item.ingotGold)
				);
		
		//Adventure suit
		GameRegistry.addRecipe(new ItemStack(ABPItems.advsuit),
			"LCL",
			"GPG", 
			'C', new ItemStack(Block.cloth,1),
			'L', new ItemStack(Item.leather),
			'P', new ItemStack(Item.legsLeather),
			'G', new ItemStack(Item.ingotGold)
		);
		
		
	}
}
