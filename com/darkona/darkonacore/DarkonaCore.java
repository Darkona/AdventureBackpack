package com.darkona.darkonacore;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class DarkonaCore extends DummyModContainer {

	public DarkonaCore() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "DarkonaCore";
		meta.name = "DarkonaCore";
		meta.version = "1"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "Roll Credits ...";
		meta.authorList = Arrays.asList("Darkona");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";

	}
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
		
	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@Subscribe
	public void init(FMLInitializationEvent event) {
		
		
	}
	
	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {
	
	}
}
