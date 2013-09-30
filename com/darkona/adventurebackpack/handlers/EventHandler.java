package com.darkona.adventurebackpack.handlers;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fluids.FluidStack;

import com.darkona.adventurebackpack.common.Actions;
import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.common.events.HoseSpillEvent;
import com.darkona.adventurebackpack.common.events.HoseSuckEvent;
import com.darkona.adventurebackpack.entity.EntityRideableSpider;
import com.darkona.darkonacore.LightningStrikeEvent;

public class EventHandler {

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void Suck(HoseSuckEvent event) {
		FluidStack result = Actions.attemptFill(event.world, event.target, event.entityPlayer, event.currentTank);
		if (result != null)
		{
			event.fluidResult = result;
			event.setResult(Result.ALLOW);
		} else
		{
			event.setResult(Result.DENY);
		}
	}

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void Spill(HoseSpillEvent event) {
		FluidStack result = Actions.attemptPour(event.player, event.world, event.x, event.y, event.z, event.currentTank);
		if (result != null)
		{
			event.fluidResult = result;
			event.setResult(Result.ALLOW);
		} else
		{
			event.setResult(Result.DENY);
		}
	}

	@ForgeSubscribe(priority = EventPriority.HIGH)
	public void playerDies(LivingDeathEvent event) {
		if (event.entity instanceof EntityPlayer && Utils.isWearingBackpack((EntityPlayer) event.entity))
		{
			Actions.tryPlaceOnDeath((EntityPlayer) event.entity);
		}
		event.setResult(Result.ALLOW);
	}

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void transformBackpack(LightningStrikeEvent event) {
		if (event.entityHit instanceof EntityPlayer && Utils.isWearingBackpack((EntityPlayer) event.entityHit))
		{
			Actions.electrify((EntityPlayer) event.entityHit);
			event.setResult(Result.ALLOW);
		}
	}

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void rideSpider(EntityInteractEvent event) {
		if (!event.entityPlayer.worldObj.isRemote && Utils.getBackpackColorName(Utils.getWearingBackpack(event.entityPlayer)).equals("Spider")
				&& !(event.entityPlayer.ridingEntity instanceof EntityRideableSpider))
		{
			if (event.target instanceof EntityRideableSpider)
			{
				event.entityPlayer.mountEntity(event.target);
			} else if (event.target instanceof EntitySpider)
			{
				EntityRideableSpider pet = new EntityRideableSpider(event.target.worldObj);
				pet.setLocationAndAngles(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
				event.target.setDead();
				event.target.worldObj.spawnEntityInWorld(pet);
				event.entityPlayer.mountEntity(pet);
				event.entityPlayer.eyeHeight -= 1;
			}
		}
		event.setResult(Result.ALLOW);
	}

	@ForgeSubscribe(priority = EventPriority.HIGH)
	public void fall(LivingFallEvent event) {

		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (event.distance > 4)
				player.playSound("tile.piston.in", 0.5F, player.getRNG().nextFloat() * 0.25F + 0.6F);
			event.distance *= (event.distance > 6) ? (Utils.isWearingBoots(player) ? 0.6 : 1) : 0;
		}
		event.setResult(Result.ALLOW);
	}

}
