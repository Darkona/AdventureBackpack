package adventurebackpack.items;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemMachete extends ItemSword{
	
	public ItemMachete(int par1) {
		super(par1, EnumToolMaterial.IRON);
		setCreativeTab(CreativeTabs.tabCombat);
		setMaxDamage(Item.swordDiamond.getMaxDamage());
		setUnlocalizedName("jungleMachete");
		setFull3D();
		setMaxStackSize(1);
	}

	@Override
	public int getItemStackLimit() {
		return 1;
	}	



}
