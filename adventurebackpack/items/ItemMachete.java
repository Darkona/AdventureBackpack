package adventurebackpack.items;

import java.util.ArrayList;
import java.util.Random;

import adventurebackpack.common.Textures;
import adventurebackpack.config.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMachete extends ItemSword {

	private float weaponDamage;
	private final EnumToolMaterial toolMaterial;
	
	public ItemMachete(int par1) {
		super(par1, EnumToolMaterial.IRON);
		setCreativeTab(CreativeTabs.tabCombat);
		setMaxDamage(Item.swordIron.getMaxDamage()+100);
		setUnlocalizedName("jungleMachete");
		setFull3D();
		setMaxStackSize(1);
		toolMaterial = EnumToolMaterial.IRON;
		this.weaponDamage = 5.0F + toolMaterial.getDamageVsEntity();
	}

	@Override
	public int getItemStackLimit() {
		return 1;
	}

	@Override
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(Textures.resourceString(ItemInfo.MACHETE_ICON));
		
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		// TODO Auto-generated method stub
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block) {
		
		if (block.blockID == Block.web.blockID)
        {
            return 25.0F;
        }
        else
        {
            Material material = block.blockMaterial;
            return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.pumpkin ? 4.0F : 1F;
        }
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLivingBase entity)
    {
		for(ItemStack stacky : OreDictionary.getOres("treeLeaves")){
			if (stacky.itemID == blockID) return true;
		}
        if (	blockID == Block.web.blockID ||
        		blockID != Block.tallGrass.blockID ||
        		blockID != Block.vine.blockID  ||
        		(Block.blocksList[blockID] instanceof IShearable))
        {
            return true;
        }
       return super.onBlockDestroyed(stack, world, blockID, x, y, z, entity);
    }
	
	 @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            return false;
        }
        int id = player.worldObj.getBlockId(x, y, z);
        for(ItemStack stacky : OreDictionary.getOres("treeLeaves")){
			if (stacky.itemID == id) return false;
		}
        
        if (Block.blocksList[id] instanceof IShearable)
        {
            IShearable target = (IShearable)Block.blocksList[id];
            if (target.isShearable(itemstack, player.worldObj, x, y, z))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
                Random rand = new Random();

                for(ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d, (double)y + d1, (double)z + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    player.worldObj.spawnEntityInWorld(entityitem);
                }

                itemstack.damageItem(1, player);
                player.addStat(StatList.mineBlockStatArray[id], 1);
            }
        }
        return false;
    }
}
