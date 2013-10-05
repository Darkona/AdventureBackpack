package com.darkona.adventurebackpack.items;

import java.util.List;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.config.ItemInfo;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBackpackComponent extends Item {

	public ItemBackpackComponent(int par1) {
		super(par1);
		setFull3D();
		setNoRepair();
		setHasSubtypes(true);
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}

	private Icon sleepingBagIcon;
	private Icon backpackTankIcon;
	private Icon hoseHeadIcon;
	private Icon macheteHandleIcon;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		 itemIcon = register.registerIcon(Textures.resourceString(ItemInfo.SLEEPING_BAG_ICON));
		 sleepingBagIcon = register.registerIcon(Textures.resourceString(ItemInfo.SLEEPING_BAG_ICON));
		 backpackTankIcon = register.registerIcon(Textures.resourceString(ItemInfo.BACKPACK_TANK_ICON));
		 hoseHeadIcon = register.registerIcon(Textures.resourceString(ItemInfo.HOSE_HEAD_ICON));
		 macheteHandleIcon = register.registerIcon(Textures.resourceString(ItemInfo.MACHETE_HANDLE_ICON));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemDisplayName(ItemStack stack) {
		switch(getDamage(stack)){
		case 1: return "Sleeping Bag";
		case 2: return "Backpack Tank";
		case 3: return "Hose Head";
		case 4: return "Machete Handle";
		}
		return "Backpack Component";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		switch(par1){
		case 1: return sleepingBagIcon;
		case 2: return backpackTankIcon;
		case 3: return hoseHeadIcon;
		case 4: return macheteHandleIcon;
		}
		return itemIcon;
	}
	
	@Override
	public Item setUnlocalizedName(String par1Str) {
		
		return super.setUnlocalizedName(par1Str);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		switch(getDamage(stack)){
		case 1: return "sleepingBag";
		case 2: return "backpackTank";
		case 3: return "hoseHead";
		case 4: return "macheteHandle";
		}
		return "Backpack Component";
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List subItems) {
		for(int i = 1; i <= 4; i++){
			subItems.add(new ItemStack(this,1,i));
		}
	}
	
	
}
