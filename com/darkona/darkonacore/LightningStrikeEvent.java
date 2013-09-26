package com.darkona.darkonacore;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class LightningStrikeEvent extends Event{

	public final net.minecraft.entity.Entity entityHit;
	public final net.minecraft.entity.effect.EntityLightningBolt lightning;
	
	public LightningStrikeEvent(net.minecraft.entity.Entity entity, net.minecraft.entity.effect.EntityLightningBolt bolt){
		super();
		this.entityHit = entity;
		this.lightning = bolt;
	}
	
	public static void postMe(net.minecraft.entity.Entity entity, net.minecraft.entity.effect.EntityLightningBolt bolt){
		MinecraftForge.EVENT_BUS.post( new LightningStrikeEvent(entity, bolt));
	}
}
