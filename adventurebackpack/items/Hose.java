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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
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

public class Hose extends ItemBucket implements IFluidContainerItem {

	//TO-DO : Un-hardcode this shit to get the name from the tank that spawns the hose... whatever the way i end up doing that is...
	String name = "leftTank";

	public Hose(int par1, int par2) {
		super(par1, par2);
		setMaxStackSize(1)
		.setFull3D()
		.setCreativeTab(CreativeTabs.tabTools)
		.setNoRepair()
		.setUnlocalizedName(ItemInfo.HOSE_UNLOCALIZED_NAME);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return Constants.tankCapacity;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(Textures.TEXTURE_LOCATION + ":"+ ItemInfo.HOSE_ICON);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int dmg) {
		return itemIcon;
	}

	@Override
	public void onUpdate(ItemStack stack, World world,Entity entity, int par4, boolean par5) {
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.stackTagCompound.setInteger("mode", 0);
			stack.stackTagCompound.setInteger("amount", 0);
			stack.stackTagCompound.setString("fluid", "");
		}
		int amount = 0;
		String fluid = "";
		ItemStack backpack = Utils.getWearingBackpack((EntityPlayer)entity);
		if(backpack!=null && backpack.getItem() instanceof ItemAdvBackpack && backpack.stackTagCompound != null){
			if(backpack.stackTagCompound.getCompoundTag(name).hasKey("Amount")){
				
				amount = backpack.stackTagCompound.getCompoundTag(name).getInteger("Amount");
				fluid = backpack.stackTagCompound.getCompoundTag(name).getString("FluidName");
			}
		}
		stack.stackTagCompound.setInteger("amount", amount);
		stack.stackTagCompound.setString("fluid", fluid);
		
	}
	
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,Entity entity) {
		
		ItemStack backpack = Utils.getWearingBackpack(player);
		
		if(backpack == null){ 
			stack.stackTagCompound.setInteger("mode", -1);
			return true;
		}

		int mode = getHoseMode(stack, backpack);
		if(mode == 0){
			InventoryItem inventory = new InventoryItem(backpack);
			FluidTank tank = (name == "leftTank") ? inventory.getLeftTank() : 
							(name == "rightTank" ) ? inventory.getRightTank() : null;
	
				if(entity instanceof EntityCow){
					tank.fill(new FluidStack(Fluids.milk, Constants.bucket),true);
					inventory.onInventoryChanged();
					return false;
				}
		}
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y,int z, int side, float hitX, float hitY, float hitZ) {
		
		ItemStack backpack = Utils.getWearingBackpack(player);
		InventoryItem inventory = new InventoryItem(backpack);
		FluidTank tank = (name == "leftTank") ? inventory.getLeftTank() : 
						(name == "rightTank" ) ? inventory.getRightTank() : null;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te != null && te instanceof IFluidHandler){
			switch(getHoseMode(stack, backpack)){
			
				case 0: //Suck mode
					if(tank.fill(((IFluidHandler)te).drain(ForgeDirection.UNKNOWN, Constants.bucket, false), false)>= Constants.bucket){
						tank.fill(((IFluidHandler)te).drain(ForgeDirection.UNKNOWN, Constants.bucket, true), true);
						inventory.onInventoryChanged();
						return true;
					}
					break;
				case 1:// Spill mode
					if(tank.getFluid()!=null){
						if(((IFluidHandler)te).fill(ForgeDirection.UNKNOWN, tank.drain(Constants.bucket, false), false) >= Constants.bucket){
							((IFluidHandler)te).fill(ForgeDirection.UNKNOWN, tank.drain(Constants.bucket, true), true);
							inventory.onInventoryChanged();
							return true;	
						}
					}
					break;
				default: return false;
			}
		}

		
		
		
		return false;
	}
	
	private int getHoseMode(ItemStack hose, ItemStack backpack){
		
		if(backpack == null || !(backpack.getItem() instanceof ItemAdvBackpack)){ 
			hose.stackTagCompound.setInteger("mode", -1);
			return -1;
		}
		NBTTagCompound hoseNBT = hose.stackTagCompound;
		if(hoseNBT == null){
			hoseNBT = new NBTTagCompound();
			hoseNBT.setInteger("mode",-1);
			hose.stackTagCompound = hoseNBT;
			return -1;
		}

		return hoseNBT.getInteger("mode");
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		
		int mode = 0;	
		if(stack.hasTagCompound()){
			mode = (stack.stackTagCompound.hasKey("mode")) ? stack.stackTagCompound.getInteger("mode") : mode;
		}
		
		switch(mode){
			case 0: return  name + " Hose: Suck"; 
			case 1: return  name + " Hose: Spill"; 
			case 2: return  name + " Hose: Drink"; 
			default: return  "Hose: Useless"; 
		}
	}
		
	public ItemStack onItemRightClick(ItemStack stack, World world,EntityPlayer player) {
		
		ItemStack backpack = Utils.getWearingBackpack(player);

		MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world,player, true);
		
			InventoryItem inventory = new InventoryItem(backpack);
			FluidTank tank = (name == "leftTank") ? inventory.getLeftTank() : 
							(name == "rightTank" ) ? inventory.getRightTank() : null;
			if(tank != null){
				switch(getHoseMode(stack, backpack)){
					case 0: //If it's in Suck Mode
						if(mop != null && mop.typeOfHit == EnumMovingObjectType.TILE ){
							HoseSuckEvent suckEvent = new HoseSuckEvent(player, stack, world, mop, tank);
							if(MinecraftForge.EVENT_BUS.post(suckEvent)){
								return stack;
							}
							if(suckEvent.getResult() == Result.ALLOW){
								tank.fill(suckEvent.fluidResult, true);
								inventory.onInventoryChanged();
							}
						}
						if (mop!= null && mop.typeOfHit == EnumMovingObjectType.ENTITY){
							if(mop.entityHit instanceof EntityCow){
								tank.fill(new FluidStack(Fluids.milk, Constants.bucket),true);
								inventory.onInventoryChanged();
							}
						}
						break;
					case 1: //If it's in Spill Mode
						if(mop != null && mop.typeOfHit == EnumMovingObjectType.TILE ){
							
							
							
							int x= mop.blockX; int y = mop.blockY; int z = mop.blockZ;
							
							if(world.getBlockMaterial(x, y, z).isSolid()){
								switch (mop.sideHit) {
									case 0:--y;break;
									case 1:++y;break;
									case 2:--z;break;
									case 3:++z;break;
									case 4:--x;break;
									case 5:++x;break;
								}
							}
							HoseSpillEvent spillEvent = new HoseSpillEvent(player,world, x, y, z, tank);
							if(MinecraftForge.EVENT_BUS.post(spillEvent)){
								return stack;
							}
							if(spillEvent.getResult() == Result.ALLOW){
								//if(!player.capabilities.isCreativeMode){ this is off for debugging
									tank.drain(spillEvent.fluidResult.amount, true);
									inventory.onInventoryChanged();
								//}
							}
						
						}
							break;
					case 2: //If it's in Drink Mode
						if(tank.getFluidAmount()>0) {
							player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
						}
					default: return stack;
				}
			}			
		return stack;
	}

	@Override
	public boolean canHarvestBlock(Block block) {
		if (Utils.isBlockRegisteredAsFluid(block.blockID) > -1)
			return true;
		return false;
	}

	@Override
	public int getMaxDamage() {
		return Constants.tankCapacity;
	}

	@Override
	public boolean isDamageable() {
		// TODO Auto-generated method stub
		return super.isDamageable();
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		if(stack.stackTagCompound != null && stack.stackTagCompound.hasKey("mode")){
			return ( stack.stackTagCompound.getInteger("mode") == 2 ) ? EnumAction.drink : EnumAction.none;
		}
		return EnumAction.none;
	}

	@Override
	public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) {

		super.onUsingItemTick(stack, player, count);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world,EntityPlayer player) {
		NBTTagCompound hoseNBT = stack.stackTagCompound;
		if(hoseNBT == null){
			hoseNBT = new NBTTagCompound();
			hoseNBT.setInteger("mode",-1);
			stack.stackTagCompound = hoseNBT;
		}
		int mode = hoseNBT.getInteger("mode");
		if(mode == 2){
			ItemStack backpack = player.inventory.armorItemInSlot(2);
			InventoryItem inventory = new InventoryItem(backpack);
			FluidTank tank = (name == "leftTank") ? inventory.getLeftTank() : 
							 (name == "rightTank" ) ? inventory.getRightTank() : null;	
			if(tank != null){
				if(Actions.setFluidEffect(world,player, tank)){
					tank.drain(Constants.bucket, true);
					inventory.onInventoryChanged();
				}
			}
		}
		return stack;
	}

	@Override
	public FluidStack getFluid(ItemStack container) {
		if(container.stackTagCompound != null && container.stackTagCompound.hasKey("FluidName")){
			FluidStack fluid = new FluidStack(FluidRegistry.getFluid(container.stackTagCompound.getString("FluidName")),container.stackTagCompound.getInteger("Amount"));
			return fluid;
		}
		return null;
	}

	@Override
	public int getCapacity(ItemStack container) {
		int cap = 4000;
		if(container.stackTagCompound != null && container.stackTagCompound.hasKey("Amount")){
			cap = cap - container.stackTagCompound.getInteger("Amount");
		}
		return cap;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		
		return null;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
