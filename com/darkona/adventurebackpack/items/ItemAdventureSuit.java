package com.darkona.adventurebackpack.items;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IconRegister;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.client.models.FullArmorModel;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.config.ItemInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdventureSuit extends ItemArmor {

	public ItemAdventureSuit(int par1) {
		super(par1, EnumArmorMaterial.CLOTH, 0, 2);
		setFull3D();
		//setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName(ItemInfo.SUIT_UNLOCALIZED_NAME);
		setMaxStackSize(1);
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 2;
	}
	
	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(Textures.resourceString(ItemInfo.SUIT_ICON));
		
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

	@Override
	public String getItemDisplayName(ItemStack helmet) {
		return ItemInfo.SUIT_NAME;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return Textures.resourceString("textures/items/item.adventurerSuit.png");
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack itemStack, int armorSlot) {
		return entity instanceof EntityPlayer ?  FullArmorModel.instance.setPlayer(((EntityPlayer)entity)) : null;
	}
}
