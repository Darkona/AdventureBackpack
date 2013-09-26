package com.darkona.adventurebackpack.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityRideableSpider extends EntitySpider {


	public EntityRideableSpider(World par1World) {
		super(par1World);
	}
	
	@Override
	public boolean canBeSteered() {
		return true;
	}

	@Override
	protected Entity findPlayerToAttack() {
		 if (this.riddenByEntity != null) return null;
		  float f = this.getBrightness(1.0F);

	        if (f < 0.5F)
	        {
	            double d0 = 16.0D;
	            return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
	        }
	        else
	        {
	            return null;
	        }
	}
	
	public void spiderJump()
    {
        this.motionY = 0.41999998688697815D;

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= (MathHelper.sin(f) * 0.2F);
            this.motionZ += (MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }
	
	public boolean shouldRiderFaceForward(EntityPlayer player)
	    {
	        return true;
	    }
	 
	public void moveEntityWithHeading(float par1, float par2)
    {
        if (this.riddenByEntity != null)
        {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            par1 = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
            par2 = ((EntityLivingBase)this.riddenByEntity).moveForward;
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote)
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(par1, par2);
            }
            
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F)
            {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(par1, par2);
        }
    }
}
