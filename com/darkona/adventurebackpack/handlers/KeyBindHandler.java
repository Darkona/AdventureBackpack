package com.darkona.adventurebackpack.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class KeyBindHandler extends KeyHandler {

	public static KeyBinding openBackpackGUI = new KeyBinding("Open Backpack", Keyboard.KEY_B);

	public static KeyBinding[] keyBindArray = { openBackpackGUI };
	public static boolean[] repeats = new boolean[keyBindArray.length];

	public KeyBindHandler() {
		super(keyBindArray, repeats);
	}

	@SuppressWarnings("rawtypes")
	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
	private boolean keyHasBeenPressed;
	public static boolean keyPressed = false;;

	@Override
	public String getLabel() {
		return "ActionKey";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player == null || tickEnd)
			return;
		if (kb == openBackpackGUI)
		{
			keyHasBeenPressed = true;

		}

	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if (keyHasBeenPressed)
		{
			keyHasBeenPressed = false;
			if (Minecraft.getMinecraft().inGameHasFocus)
			{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(1));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnumSet<TickType> ticks() {

		return tickTypes;
	}

	public static void playerTick(EntityPlayer player) {

	}

}
