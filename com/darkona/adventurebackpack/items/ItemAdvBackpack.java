package com.darkona.adventurebackpack.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.blocks.ABPBlocks;
import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.client.models.ModelAdvBackpackArmor;
import com.darkona.adventurebackpack.common.BackpackAbilities;
import com.darkona.adventurebackpack.common.Utils;
import com.darkona.adventurebackpack.handlers.PacketHandler;
import com.darkona.adventurebackpack.inventory.BackpackContainer;
import com.darkona.adventurebackpack.inventory.InventoryItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdvBackpack extends ItemArmor {

	public ItemAdvBackpack(int id) {
		super(id, EnumArmorMaterial.CHAIN, 0, 1);
		//setCreativeTab(CreativeTabs.tabTools);
		setMaxDamage(Item.plateLeather.getMaxDamage());
		setUnlocalizedName("adventureBackpack");
		setFull3D();
		setMaxStackSize(1);
		this.BlockID = ABPBlocks.advbackpack.blockID;
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}

	public final int armorType = 1;
	public final int damageReduceAmount = Item.plateLeather.damageReduceAmount;
	public int BlockID;

	@Override
	public int getItemStackLimit() {
		return 1;
	}

	@Override
	public void onCreated(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {

		super.onCreated(stack, par2World, par3EntityPlayer);
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		if (!stack.stackTagCompound.hasKey("color") || stack.stackTagCompound.getString("color").isEmpty())
		{
			stack.stackTagCompound.setString("color", "Standard");
			stack.stackTagCompound.setString("colorName","Standard");
		}
	}

	public boolean placeBackpack(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, boolean from) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		if (!stack.stackTagCompound.hasKey("color") || stack.stackTagCompound.getString("color").isEmpty())
		{
			stack.stackTagCompound.setString("color", "Standard");
			stack.stackTagCompound.setString("colorName","Standard");
		}
		
		world.spawnEntityInWorld(new EntityLightningBolt(world,x,y, z));
		Block backpack = ABPBlocks.advbackpack;
		
		if (y <= 0 || y >= 255)
			return false;
		if (backpack.canPlaceBlockOnSide(world, x, y, z, side))
		{
			if (world.getBlockMaterial(x, y, z).isSolid())
			{
				switch (side)
				{
				case 0:
					--y;
					break;
				case 1:
					++y;
					break;
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
					break;
				}
			}
			if (y <= 0 || y >= world.getHeight())
				return false;
			if (backpack.canPlaceBlockAt(world, x, y, z))
			{
				if (world.setBlock(x, y, z, backpack.blockID))
				{
					backpack.onBlockPlacedBy(world, x, y, z, player, stack);
					world.playSoundAtEntity(player, Block.soundClothFootstep.getPlaceSound(), 0.5f, 1.0f);
					((TileAdvBackpack) world.getBlockTileEntity(x, y, z)).loadFromNBT(stack.stackTagCompound);
					if (from)
					{
						player.inventory.decrStackSize(player.inventory.currentItem, 1);
					} else
					{
						player.inventory.armorInventory[2] = null;
						;
					}
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return placeBackpack(stack, player, world, x, y, z, side, true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
		if (mop == null || mop.typeOfHit == EnumMovingObjectType.ENTITY)
		{
			if (world.isRemote)
			{
				((EntityClientPlayerMP) player).sendQueue.addToSendQueue(PacketHandler.makePacket(2));
			}
		}
		return stack;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean isCurrentItem) {
		EntityPlayer player = (EntityPlayer) entity;
		if (!world.isRemote)
		{
			if (player.openContainer != null)
			{
				if (player.openContainer instanceof BackpackContainer && !((BackpackContainer) player.openContainer).source)
				{

					if (((BackpackContainer) player.openContainer).needsUpdate)
					{
						((BackpackContainer) player.openContainer).inventory.onInventoryChanged();
						((BackpackContainer) player.openContainer).needsUpdate = false;

					}
				}
			}
			
		}

		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote)
		{
			if (player.openContainer != null)
			{
				if (player.openContainer instanceof BackpackContainer && !((BackpackContainer) player.openContainer).source)
				{
					if (((BackpackContainer) player.openContainer).needsUpdate)
					{
						((BackpackContainer) player.openContainer).inventory.onInventoryChanged();
						((BackpackContainer) player.openContainer).needsUpdate = false;

					}
				}
			}
		}

		if (stack.stackTagCompound != null)
		{
			BackpackAbilities.instance.executeAbility(player, world, stack);
		}
		
		
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		return super.onDroppedByPlayer(stack, player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack stack, int armorSlot) {
		InventoryItem inv = new InventoryItem(stack);
		return ModelAdvBackpackArmor.instance.setTanks(inv.getLeftTank().getFluid(), inv.getRightTank().getFluid()).setItems(inv.getStackInSlot(3),
				inv.getStackInSlot(0));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return Utils.getBackpackColorResourceLocation(stack).toString();
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ,
			int metadata) {
		return true;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (stack.stackTagCompound != null)
		{
			if (stack.stackTagCompound.hasKey("colorName"))
			{
				list.add(stack.stackTagCompound.getString("colorName"));
			} else if (stack.stackTagCompound.hasKey("color"))
			{
				list.add(stack.stackTagCompound.getString("color"));
			}
		}
		super.addInformation(stack, player, list, par4);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List subItems) {
		for (String name : Utils.backpackNames){
			ItemStack bp = new ItemStack(this,1,0);
			NBTTagCompound c = new NBTTagCompound();
			c.setString("color", name);
			c.setString("colorName",Utils.getDisplayNameForColor(name));
			bp.setTagCompound(c);
			subItems.add(bp);
		}
	}

}
