package com.darkona.adventurebackpack.entity;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.config.ItemInfo;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ABPEntities {

	public ABPEntities() {
		
	}
	
	public static void init(){
		EntityRegistry.registerModEntity(EntityRideableSpider.class, "Spider", 1, AdventureBackpack.instance, 32, 4, true);
	}

}
