package com.darkona.adventurebackpack.handlers;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.client.gui.GuiAdvBackpack;
import com.darkona.adventurebackpack.client.gui.GuiCraftAdvBackpack;
import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.inventory.BackCraftContainer;
import com.darkona.adventurebackpack.inventory.BackpackContainer;
import com.darkona.adventurebackpack.inventory.InventoryItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(AdventureBackpack.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		switch (ID)
		{
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack)
			{
				return new BackpackContainer(player.inventory, (TileAdvBackpack) te);
			}
			break;
		case 1:
			InventoryItem inv = Utils.getBackpackInv(player, true);
			if (inv.containerStack != null)
				return new BackpackContainer(player.inventory, inv);
			break;
		case 2:
			inv = Utils.getBackpackInv(player, false);
			if (inv.containerStack != null)
				return new BackpackContainer(player.inventory, inv);
			break;
		case 3:
			te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack)
			{
				return new BackCraftContainer(player, (TileAdvBackpack) te);
			}
			break;
		case 4:
			inv = Utils.getBackpackInv(player, true);
			if (inv.containerStack != null)
				return new BackCraftContainer(player, world, inv);
			break;
		case 5:
			inv = Utils.getBackpackInv(player, false);
			if (inv.containerStack != null)
				return new BackCraftContainer(player, world, inv);
			break;
		}

		return null;

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		InventoryItem inv;
		TileEntity te;
		switch (ID)
		{
		case 0:
			te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack)
			{
				return new GuiAdvBackpack(player, (TileAdvBackpack) te);
			}
			break;
		case 1:

			inv = Utils.getBackpackInv(player, true);
			if (inv.containerStack != null)
			{
				return new GuiAdvBackpack(player, inv, true);
			}
			break;
		case 2:
			inv = Utils.getBackpackInv(player, false);
			if (inv.containerStack != null)
			{
				return new GuiAdvBackpack(player, inv, false);
			}
			break;

		case 3:
			te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack)
			{
				return new GuiCraftAdvBackpack(player, (TileAdvBackpack) te);
			}
			break;
		case 4:
			inv = Utils.getBackpackInv(player, true);
			if (inv.containerStack != null)
			{
				return new GuiCraftAdvBackpack(player, inv, true);
			}
			break;
		case 5:
			inv = Utils.getBackpackInv(player, false);
			if (inv.containerStack != null)
			{
				return new GuiCraftAdvBackpack(player, inv, false);
			}
			break;
		}
		return null;
	}
}
