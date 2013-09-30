package com.darkona.adventurebackpack.items;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.config.ItemInfo;

import net.minecraft.client.renderer.texture.IconRegister;
//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPistonBoots extends ItemArmor {

	public ItemPistonBoots(int par1) {
		super(par1, EnumArmorMaterial.CLOTH, 0, 3);
		//setCreativeTab(CreativeTabs.tabCombat);
		setCreativeTab(AdventureBackpack.AdvBackpackTab);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {	
		itemIcon = 	register.registerIcon(Textures.resourceString(ItemInfo.PISTON_BOOTS_ICON));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return Textures.resourceString("textures/items/BootsTexture.png");
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		
		if(player.isSprinting()){
			player.stepHeight = 1.001F;
		}else{
			player.stepHeight = 0.5001F;
		}
		
		
	}
}
