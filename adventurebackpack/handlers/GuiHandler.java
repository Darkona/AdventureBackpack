package adventurebackpack.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import adventurebackpack.AdventureBackpack;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.client.gui.GuiAdvBackpack;
import adventurebackpack.client.gui.GuiCraftAdvBackpack;
import adventurebackpack.common.Actions;
import adventurebackpack.common.BackCraftContainer;
import adventurebackpack.common.BackpackContainer;
import adventurebackpack.inventory.InventoryItem;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(AdventureBackpack.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		switch (ID) {
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack) {
				return new BackpackContainer(player.inventory, (TileAdvBackpack) te);
			}
			break;
		case 1:
			InventoryItem inv = Actions.getBackpackInv(player, true);
			if (inv.containerStack != null)
				return new BackpackContainer(player.inventory, inv);
			break;
		case 2:
			inv = Actions.getBackpackInv(player, false);
			if (inv.containerStack != null)
				return new BackpackContainer(player.inventory, inv);
			break;
		case 3:
			te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack) {
				return new BackCraftContainer(player.inventory, (TileAdvBackpack) te);
			}
			break;
		case 4:
			inv = Actions.getBackpackInv(player, true);
			if (inv.containerStack != null)
				return new BackCraftContainer(player, world, inv);
			break;
		case 5:
			inv = Actions.getBackpackInv(player, false);
			if (inv.containerStack != null)
				return new BackCraftContainer(player, world, inv);
			break;
		}

		return null;

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		InventoryItem inv;
		switch (ID) {
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack) {
				return new GuiAdvBackpack(player, (TileAdvBackpack) te);
			}
			break;
		case 1:

			inv = Actions.getBackpackInv(player, true);
			if (inv.containerStack != null) {
				return new GuiAdvBackpack(player, inv);
			}
			break;
		case 2:
			inv = Actions.getBackpackInv(player, false);
			if (inv.containerStack != null) {
				return new GuiAdvBackpack(player, inv);
			}
			break;

		case 3:
			te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileAdvBackpack) {
				return new GuiCraftAdvBackpack(player.inventory, (TileAdvBackpack) te);
			}
			break;
		case 4:
			inv = Actions.getBackpackInv(player, true);
			if (inv.containerStack != null) {
				return new GuiCraftAdvBackpack(player, inv);
			}
			break;
		case 5:
			inv = Actions.getBackpackInv(player, false);
			if (inv.containerStack != null) {
				return new GuiCraftAdvBackpack(player, inv);
			}
			break;
		}
		return null;
	}
}
