package com.darkona.adventurebackpack.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.ModInformation;
import com.darkona.adventurebackpack.common.Actions;
import com.darkona.adventurebackpack.common.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public PacketHandler() {
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(ModInformation.CHANNEL) && packet != null)
		{
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(packet.data));
			try
			{
				int[] data = new int[is.readInt()];
				for (int i = 0; i < data.length; i++)
				{
					data[i] = is.readInt();
				}
				doShit(data, (EntityPlayer) player);
				packet = null;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Makes a packet containing an integer array, the first integer is the
	 * length of the array containing the actual values.
	 * 
	 * @param data
	 *            An array of integers representing what the handler should do.
	 *            The first integer should be the action the switch will select
	 *            to do, and the rest is the data the receiving method would
	 *            need
	 * @return a brand new, 0km, Packet250CustomPayload free of charge!
	 */
	public static Packet250CustomPayload makePacket(int... data) {

		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8 * data.length);
		DataOutputStream os = new DataOutputStream(bos);

		try
		{
			os.writeInt(data.length);
			for (int i = 0; i < data.length; i++)
			{
				os.writeInt(data[i]);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		packet.channel = ModInformation.CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;

	}

	// This does shit depending on the incoming shit
	private void doShit(int[] values, EntityPlayer player) {
		int playerX = (int) player.posX;
		int playerY = (int) player.posY;
		int playerZ = (int) player.posZ;
		World world = player.worldObj;

		switch (values[0])
		{
		case 0:
			FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 0, world, values[1], values[2], values[3]);
			break;
		case 1:
			if (Utils.isWearingBackpack(player))
			{
				FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 1, world, playerX, playerY, playerZ);
			}
			break;
		case 2:
			if (Utils.isHoldingBackpack(player))
			{
				FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 2, world, playerX, playerY, playerZ);
			}
			break;
		case 3:
			Actions.switchHose(player, values[1], values[2]);
			break;
		case 4:
			Actions.cycleTool(player, values[1], values[2]);
			break;
		case 5:
			switch (values[1])
			{
			case 0:
				Actions.deploySleepingBagFromBackpack(player, values[2], values[3], values[4]);
				break;

			case 1:
				if (values[1] == 1 && Utils.isWearingBackpack(player))
				{
					Actions.deploySleepingBagFromPlayer(player); 
				}
				break;

			case 2:
				switch (values[2])
				{

				case 0:
					FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 3, world, values[3], values[4], values[5]);
					break;
				case 1:
					if (Utils.isWearingBackpack(player))
					{
						FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 4, world, playerX, playerY, playerZ);
					} else if (Utils.isHoldingBackpack(player))
					{
						FMLNetworkHandler.openGui(player, AdventureBackpack.instance, 5, world, playerX, playerY, playerZ);
					}
					break;
				}
				break;
			}
			break;
		case 6: 
			Actions.makeSpiderJump(values[1]);
			break;
		case 7: 
			break;
		default:
			break;
		}
	}

}
