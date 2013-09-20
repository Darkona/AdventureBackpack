package adventurebackpack.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.inventory.InventoryItem;

public class BackCraftContainer extends Container
{

	public IAdvBackpack inventory;
	public boolean source;
	public Boolean needsUpdate;
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World world;

	public BackCraftContainer(InventoryPlayer invPlayer, TileAdvBackpack te)
	{
		needsUpdate = false;
		inventory = te;
		makeSlots(invPlayer);
		source = true;
		world = te.worldObj;
	}

	public BackCraftContainer(EntityPlayer player, World world,
		InventoryItem item)
	{
		needsUpdate = false;
		inventory = item;
		source = false;
		makeSlots(player.inventory);
		inventory.openChest();
		this.world = world;
	}

	private void makeSlots(InventoryPlayer invPlayer)
	{

		IInventory sexy = this.craftMatrix;

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
				addSlotToContainer(new Slot(invPlayer, (x + y * 9 + 9),
					(8 + 18 * x), (84 + y * 18)));
			}
		}

		// Backpack Crafting Grif Inventory
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 3; x++)
			{
				addSlotToContainer(new Slot(sexy, (x + y * 3), (31 + 18 * x),
					(7 + y * 18)));
			}
		}

		addSlotToContainer(new SlotCrafting(invPlayer.player, this.craftMatrix,
			this.craftResult, 0, 91, 25));
		this.onCraftMatrixChanged(this.craftMatrix);

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager
			.getInstance().findMatchingRecipe(this.craftMatrix, this.world));
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.world.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack =
					this.craftMatrix.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					par1EntityPlayer.dropPlayerItem(itemstack);
				}
			}
		}
	}

	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return par2Slot.inventory != this.craftResult
			&& super.func_94530_a(par1ItemStack, par2Slot);
	}

	@Override
	public ItemStack
		transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 0)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (par2 >= 10 && par2 < 37)
			{
				if (!this.mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			} else if (par2 >= 37 && par2 < 46)
			{
				if (!this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}
}
