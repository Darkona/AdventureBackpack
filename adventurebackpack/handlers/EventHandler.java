package adventurebackpack.handlers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fluids.FluidStack;
import adventurebackpack.common.Actions;
import adventurebackpack.common.Utils;
import adventurebackpack.common.events.HoseSpillEvent;
import adventurebackpack.common.events.HoseSuckEvent;
import darkona.transformation.LightningStrikeEvent;

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
	
	public void pistonJump(LivingJumpEvent event){
		
	}
	
	@ForgeSubscribe(priority = EventPriority.HIGH)
	public void fall(LivingFallEvent event){
		
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			event.distance *= (event.distance > 6) ? (Utils.isWearingBoots(player) ? 0.6 : 1) : 0;
			if (event.distance > 6) player.playSound("tile.piston.in", 0.5F, player.getRNG().nextFloat() * 0.25F + 0.6F);
		}
		event.setResult(Result.ALLOW);
	}
}
