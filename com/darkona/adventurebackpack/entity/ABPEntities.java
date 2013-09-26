package com.darkona.adventurebackpack.entity;

import com.darkona.adventurebackpack.AdventureBackpack;

import cpw.mods.fml.common.registry.EntityRegistry;

public class ABPEntities {

	public ABPEntities() {
		
	}
	
	public static void init(){
		EntityRegistry.registerModEntity(EntityRideableSpider.class, "Spider", 0, AdventureBackpack.instance, 64, 1, true);
	}

}
