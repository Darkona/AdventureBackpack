package adventurebackpack.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.inventory.InventoryItem;
import adventurebackpack.inventory.SlotBackpack;
import adventurebackpack.inventory.SlotFluid;
import adventurebackpack.inventory.SlotTool;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class BackpackContainer extends Container {

	public IAdvBackpack inventory;

	public boolean source;
	public Boolean needsUpdate;

	public BackpackContainer(InventoryPlayer invPlayer, TileAdvBackpack te) {
		needsUpdate = false;
		inventory = te;
		makeSlots(invPlayer);
		source = true;
	}

	public BackpackContainer(InventoryPlayer invPlayer, InventoryItem inventoryItem) {

		needsUpdate = false;
		inventory = inventoryItem;
		source = false;
		makeSlots(invPlayer);
		inventory.openChest();
	}

	private void makeSlots(InventoryPlayer invPlayer) {

		IInventory sexy = inventory;

		// Player's Hotbar
		for (int x = 0; x < 9; x++)
		{
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 142));
		}

		// Player's Inventory
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				addSlotToContainer(new Slot(invPlayer, (x + y * 9 + 9), (8 + 18 * x), (84 + y * 18)));
			}
		}
		int thing = 0;

		// Backpack Inventory
		addSlotToContainer(new SlotTool(sexy, thing++, 62, 37));// Upper Tool -0
		addSlotToContainer(new SlotBackpack(sexy, thing++, 80, 37));// 1
		addSlotToContainer(new SlotBackpack(sexy, thing++, 98, 37));// 2
		addSlotToContainer(new SlotTool(sexy, thing++, 62, 55));// Lower Tool -3
		addSlotToContainer(new SlotBackpack(sexy, thing++, 80, 55));// 4
		addSlotToContainer(new SlotBackpack(sexy, thing++, 98, 55));// 5

		addSlotToContainer(new SlotFluid(sexy, thing++, 7, 25));// bucket left
																// -6
		addSlotToContainer(new SlotFluid(sexy, thing++, 7, 55));// bucket out
																// left -7
		addSlotToContainer(new SlotFluid(sexy, thing++, 153, 25));// bucket
																	// right -8
		addSlotToContainer(new SlotFluid(sexy, thing++, 153, 55));// bucket out
																	// right -9

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	public TileAdvBackpack getTile() {
		return (TileAdvBackpack) inventory;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		// Todo: Fix the shit don't respecting slot accepting itemstack.
		Slot slot = getSlot(i);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();

			if (i >= 36)
			{
				if (!mergeItemStack(stack, 0, 36, false))
				{
					return null;
				}
			} else if (!mergeItemStack(stack, 36, 36 + inventory.getSizeInventory(), false))
			{
				return null;
			}

			if (stack.stackSize == 0)
			{
				slot.putStack(null);
			} else
			{
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, stack);

			return result;
		}

		return null;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		this.needsUpdate = true;
		super.onContainerClosed(par1EntityPlayer);
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	@Override
	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {

		return super.mergeItemStack(par1ItemStack, par2, par3, par4);
	}

	public NBTTagCompound getCompound() {
		this.needsUpdate = false;
		return ((InventoryItem) inventory).writeToNBT();

	}

	@Override
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
		this.needsUpdate = true;
		super.putStackInSlot(par1, par2ItemStack);
	}

}
