package adventurebackpack.items;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPistonBoots extends ItemArmor {

	public ItemPistonBoots(int par1) {
		super(par1, EnumArmorMaterial.CLOTH, 0, 3);
		setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		
//		if(player instanceof EntityPlayerSP && ((EntityPlayerSP)player).movementInput.jump && player.onGround){
//			player.motionY += 0.3;
//			player.jumpMovementFactor += 0.1;
//		}
		if(player.isSprinting()){
			player.stepHeight = 1.001F;
		}else{
			player.stepHeight = 0.5001F;
		}
	}
}
