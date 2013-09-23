package adventurebackpack.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import adventurebackpack.common.Actions;
import adventurebackpack.common.Constants;
import adventurebackpack.common.Textures;
import adventurebackpack.common.Utils;
import adventurebackpack.common.events.HoseSpillEvent;
import adventurebackpack.common.events.HoseSuckEvent;
import adventurebackpack.config.ItemInfo;
import adventurebackpack.fluids.Fluids;
import adventurebackpack.inventory.InventoryItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHose extends ItemBucket {

	Icon leftIcon;
	Icon rightIcon;
	
	public ItemHose(int par1, int par2) {
		super(par1, par2);
		setMaxStackSize(1).setFull3D().setCreativeTab(CreativeTabs.tabTools).setNoRepair().setUnlocalizedName(ItemInfo.HOSE_UNLOCALIZED_NAME);
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {	
		leftIcon = 	register.registerIcon(Textures.TEXTURE_LOCATION + ":" + ItemInfo.HOSE_LEFT_ICON);
		rightIcon = register.registerIcon(Textures.TEXTURE_LOCATION + ":" + ItemInfo.HOSE_RIGHT_ICON);
		itemIcon = 	register.registerIcon(Textures.TEXTURE_LOCATION + ":" + ItemInfo.HOSE_USELESS_ICON);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		ItemStack backpack = Utils.getWearingBackpack((EntityPlayer)entity);
		if(backpack != null)
		{
			if(nbt.getInteger("tank") == -1) nbt.setInteger("tank", 0);
			if(nbt.getInteger("mode") == -1) nbt.setInteger("mode", 0);
			InventoryItem inv = new InventoryItem(backpack);
			inv.readFromNBT();
			FluidTank tank =  nbt.getInteger("tank") == 0 ? inv.leftTank : inv.rightTank;
			if(tank != null && tank.getFluid() != null)
			{
				nbt.setString("fluid", Utils.capitalize(tank.getFluid().getFluid().getName()));
				nbt.setInteger("amount", tank.getFluidAmount());
			}else
			{
				nbt.setInteger("amount", 0 );
				nbt.setString("fluid", "Empty");
			}
		}
		else
		{
			nbt.setInteger("amount", 0 );
			nbt.setString("fluid", "None");
			nbt.setInteger("mode", -1);
			nbt.setInteger("tank", -1);
		}
		stack.setTagCompound(nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return itemIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack stack, int pass) {
		if(stack.getTagCompound() == null || stack.getTagCompound().getInteger("tank") == -1) return itemIcon;
		return  stack.getTagCompound().getInteger("tank") == 0 ? leftIcon : rightIcon;
		//return itemIcon;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

		ItemStack backpack = Utils.getWearingBackpack(player);
		if(backpack == null) return false;
		
		InventoryItem inv = Utils.getBackpackInv(player, true);
		FluidTank tank = getHoseTank(stack) == 0 ? inv.leftTank : inv.rightTank;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof IFluidHandler)
		{
			switch (getHoseMode(stack))
			{
			case 0: // Suck mode
				if (tank.fill(((IFluidHandler) te).drain(ForgeDirection.UNKNOWN, Constants.bucket, false), false) >= Constants.bucket)
				{
					tank.fill(((IFluidHandler) te).drain(ForgeDirection.UNKNOWN, Constants.bucket, true), true);
					inv.onInventoryChanged();
					return true;
				}
				break;
			case 1:// Spill mode
				if (tank.getFluid() != null)
				{
					if (((IFluidHandler) te).fill(ForgeDirection.UNKNOWN, tank.drain(Constants.bucket, false), false) >= Constants.bucket)
					{
						((IFluidHandler) te).fill(ForgeDirection.UNKNOWN, tank.drain(Constants.bucket, true), true);
						inv.onInventoryChanged();
						return true;
					}
				}
				break;
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		ItemStack backpack = Utils.getWearingBackpack(player);
		if(entity instanceof EntityCow && backpack !=null){
			InventoryItem inventory = new InventoryItem(backpack);
			FluidTank tank = getHoseTank(stack) == 0 ? inventory.leftTank : inventory.rightTank;
			tank.fill(new FluidStack(Fluids.milk, Constants.bucket), true);
			inventory.onInventoryChanged();
			
			((EntityCow)entity).faceEntity(player, 0.1f, 0.1f);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return true;
	}
	
	
	
	public static int getHoseMode(ItemStack hose) {
		return hose.stackTagCompound != null ? hose.stackTagCompound.getInteger("mode") : -1;
	}

	public static int getHoseTank(ItemStack hose){
		 return hose.hasTagCompound() ? hose.getTagCompound().getInteger("tank") : -1;
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {	
		String name = getHoseTank(stack) == 0 ? "Left Tank" : getHoseTank(stack) == 1 ? "Right Tank" : "";
		switch (getHoseMode(stack))
		{
		case 0:
			return name + " / Suck";
		case 1:
			return name + " / Spill";
		case 2:
			return name + " / Drink";
		default:
			return "Hose: Useless";
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		ItemStack backpack = Utils.getWearingBackpack(player);
		if(backpack==null)return stack;
			InventoryItem inventory = new InventoryItem(backpack);
			MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
			FluidTank tank = getHoseTank(stack) == 0 ? inventory.leftTank : inventory.rightTank;
			if (tank != null)
			{
				switch (getHoseMode(stack))
				{
				case 0: // If it's in Suck Mode
					if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
					{
						HoseSuckEvent suckEvent = new HoseSuckEvent(player, stack, world, mop, tank);
						if (MinecraftForge.EVENT_BUS.post(suckEvent))
						{
							return stack;
						}
						if (suckEvent.getResult() == Result.ALLOW)
						{
							tank.fill(suckEvent.fluidResult, true);
							inventory.onInventoryChanged();
						}
					}
//					if (mop != null && mop.typeOfHit == EnumMovingObjectType.ENTITY)
//					{
//						if (mop.entityHit instanceof EntityCow)
//						{
//							tank.fill(new FluidStack(Fluids.milk, Constants.bucket), true);
//							inventory.onInventoryChanged();
//						}
//					}
					break;
				case 1: // If it's in Spill Mode
					if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
					{
	
						int x = mop.blockX;
						int y = mop.blockY;
						int z = mop.blockZ;
	
						if (world.getBlockMaterial(x, y, z).isSolid())
						{
							switch (mop.sideHit)
							{
							case 0: --y; break;
							case 1: ++y; break;
							case 2: --z; break;
							case 3: ++z; break;
							case 4: --x; break;
							case 5: ++x; break;
							}
						}
						HoseSpillEvent spillEvent = new HoseSpillEvent(player, world, x, y, z, tank);
						if (MinecraftForge.EVENT_BUS.post(spillEvent))
						{
							return stack;
						}
						if (spillEvent.getResult() == Result.ALLOW)
						{
							// if(!player.capabilities.isCreativeMode){ this is
							// off
							// for debugging
							tank.drain(spillEvent.fluidResult.amount, true);
							inventory.onInventoryChanged();
							// }
						}
	
					}
					break;
				case 2: // If it's in Drink Mode
					if (tank.getFluidAmount() > 0)
					{
						player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
					}
				default:
					return stack;
				}
			}
		return stack;
	}

	@Override
	public boolean canHarvestBlock(Block block) {
		return Utils.isBlockRegisteredAsFluid(block.blockID) > -1;
	}

	@Override
	public int getMaxDamage() {
		return Constants.tankCapacity;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		return true;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("mode"))
		{
			return (stack.stackTagCompound.getInteger("mode") == 2) ? EnumAction.drink : EnumAction.none;
		}
		return EnumAction.none;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public ItemStack onEaten(ItemStack hose, World world, EntityPlayer player) {
		int mode = -1;
		int tank = -1;
		if(hose.stackTagCompound != null){
			tank = hose.stackTagCompound.getInteger("tank");
			mode = hose.stackTagCompound.getInteger("mode");
		}
		if (mode == 2 && tank > -1)
		{
			InventoryItem inventory = new InventoryItem(Utils.getWearingBackpack(player));
			FluidTank backpackTank = (tank == 0) ? inventory.getLeftTank() : (tank == 1) ? inventory.getRightTank() : null;
			if (backpackTank != null)
			{
				if (Actions.setFluidEffect(world, player, backpackTank))
				{
					backpackTank.drain(Constants.bucket, true);
					inventory.onInventoryChanged();
				}
			}
		}
		return hose;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return false;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return Constants.tankCapacity;
	}
}
