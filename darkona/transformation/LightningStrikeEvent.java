package darkona.transformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class LightningStrikeEvent extends Event{

	public final Entity entityHit;
	public final EntityLightningBolt lightning;
	
	public LightningStrikeEvent(Entity entity, EntityLightningBolt bolt){
		super();
		this.entityHit = entity;
		this.lightning = bolt;
	}
	
	public static void postMe(Entity entity, EntityLightningBolt bolt){
		MinecraftForge.EVENT_BUS.post( new LightningStrikeEvent(entity, bolt));
	}
}
