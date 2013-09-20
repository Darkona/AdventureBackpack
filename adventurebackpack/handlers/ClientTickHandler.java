package adventurebackpack.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

import adventurebackpack.inventory.SlotTool;
import adventurebackpack.items.Hose;
import adventurebackpack.items.ItemAdvBackpack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler {
	public static int dWheel;
	public static int theSlot = -1;

	public static boolean isHose = false;
	public static boolean isTool = false;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		dWheel = Mouse.getDWheel() / 120;
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null && player.isSneaking()) {
			ItemStack backpack = player.getCurrentArmor(2);
			if (backpack != null && backpack.getItem() instanceof ItemAdvBackpack) {

				Minecraft.getMinecraft().playerController.updateController();
				if (player.getCurrentEquippedItem() != null) {
					if (SlotTool.isValidTool(player.getCurrentEquippedItem())) {
						isTool = true;
						theSlot = player.inventory.currentItem;
					}
					if (player.getCurrentEquippedItem().getItem() instanceof Hose) {
						isHose = true;
						theSlot = player.inventory.currentItem;
					}
				}
			}
		} else {
			theSlot = -1;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			if (theSlot > -1 && dWheel != Mouse.getDWheel()) {

				if (isHose) {
					player.inventory.currentItem = theSlot;
					player.sendQueue.addToSendQueue(PacketHandler.makePacket(3, dWheel - Mouse.getDWheel(), theSlot));
				}

				if (isTool) {
					player.sendQueue.addToSendQueue(PacketHandler.makePacket(4, dWheel - Mouse.getDWheel(), theSlot));
					player.inventory.currentItem = theSlot;
				}

			}
			theSlot = -1;
			isHose = false;
			isTool = false;

		}

	}

	@Override
	public EnumSet<TickType> ticks() {
		// TODO Auto-generated method stub
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "AdventureBackpack: Tick!";
	}

}
