package adventurebackpack.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import adventurebackpack.common.Textures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPistonBoots extends ItemArmor {

	public ItemPistonBoots(int par1) {
		super(par1, EnumArmorMaterial.CLOTH, 0, 3);
		setCreativeTab(CreativeTabs.tabCombat);
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack boots, int armorSlot) {
//		return Boots.instance;
//	}
	
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
