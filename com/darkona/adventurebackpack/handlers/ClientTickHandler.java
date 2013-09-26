package com.darkona.adventurebackpack.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.inventory.SlotTool;
import com.darkona.adventurebackpack.items.ItemAdvBackpack;
import com.darkona.adventurebackpack.items.ItemHose;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler {
	private static int dWheel;
	private static int theSlot = -1;

	private static boolean isHose = false;
	private static boolean isTool = false;
	
	private static boolean jump = false;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		dWheel = Mouse.getDWheel() / 120;
		if (player != null && player.isSneaking())
		{
			ItemStack backpack = player.getCurrentArmor(2);
			if (backpack != null && backpack.getItem() instanceof ItemAdvBackpack)
			{

				Minecraft.getMinecraft().playerController.updateController();
				if (player.getCurrentEquippedItem() != null)
				{
					if (SlotTool.isValidTool(player.getCurrentEquippedItem()))
					{
						isTool = true;
						theSlot = player.inventory.currentItem;
					}
					if (player.getCurrentEquippedItem().getItem() instanceof ItemHose)
					{
						isHose = true;
						theSlot = player.inventory.currentItem;
					}
				}
			}
		} else
		{
			theSlot = -1;
		}
		
		if(player != null && player.movementInput.jump && player.onGround){
			jump = true;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null)
		{
			if (theSlot > -1 && dWheel != Mouse.getDWheel())
			{

				if (isHose)
				{
					player.inventory.currentItem = theSlot;
					player.sendQueue.addToSendQueue(PacketHandler.makePacket(3, dWheel - Mouse.getDWheel(), theSlot));
				}

				if (isTool)
				{
					player.sendQueue.addToSendQueue(PacketHandler.makePacket(4, dWheel - Mouse.getDWheel(), theSlot));
					player.inventory.currentItem = theSlot;
				}

			}
			theSlot = -1;
			isHose = false;
			isTool = false;

		}
		
		if( player!= null && jump && Utils.isWearingBoots(player)){
			player.playSound("tile.piston.out", 0.5F, player.getRNG().nextFloat() * 0.25F + 0.6F);
			player.motionY += 0.35;
			player.jumpMovementFactor += 0.2;
			player.fallDistance = 0;
			jump = false;
		}

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "AdventureBackpack: Tick!";
	}

}
