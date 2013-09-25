package adventurebackpack.handlers;


import darkona.transformation.LightningStrikeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;
import adventurebackpack.common.Actions;
import adventurebackpack.common.Utils;
import adventurebackpack.common.events.HoseSpillEvent;
import adventurebackpack.common.events.HoseSuckEvent;

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
//		if (event.entity instanceof EntityPlayer && Utils.isWearingHelmet((EntityPlayer) event.entity)){
//			Actions.eliminateLight((EntityPlayer) event.entity);
//		}
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
}
