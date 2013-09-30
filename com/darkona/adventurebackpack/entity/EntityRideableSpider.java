package com.darkona.adventurebackpack.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.common.Utils;

public class EntityRideableSpider extends EntitySpider implements IAnimals{

	private float prevRearingAmount;

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub
		super.entityInit();
	}

	public boolean canRiderInteract() {
		return false;
	}

	@Override
	protected boolean interact(EntityPlayer player) {
		try
		{
			if (!this.worldObj.isRemote && Utils.isWearingBackpack(player))
			{
				if (Utils.getWearingBackpack(player).getTagCompound().getString("colorName").equals("Spider"))
				{
					player.mountEntity(this);
					return true;
				}
			}
		} catch (Exception oops)
		{
			return false;
		}
		return false;
	}

	public EntityRideableSpider(World par1World) {
		super(par1World);
	}

	@Override
	public boolean canBeSteered() {
		return true;
	}

	@Override
	protected Entity findPlayerToAttack() {
		if (this.riddenByEntity != null)
			return null;
		float f = this.getBrightness(1.0F);

		if (f < 0.5F)
		{
			double d0 = 16.0D;
			return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
		} else
		{
			return null;
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}
	
	@Override
	public boolean shouldRiderFaceForward(EntityPlayer player) {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.riddenByEntity instanceof EntityPlayer)
		{

		}
		if (this.riddenByEntity != null && this.riddenByEntity.isDead)
		{
			this.riddenByEntity = null;
		}
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		if (this.riddenByEntity != null)
		{
			this.isCollidedHorizontally = this.riddenByEntity.isCollidedHorizontally;
			this.isCollidedVertically = this.riddenByEntity.isCollidedVertically;
			this.updateArmSwingProgress();

			if (this.newPosRotationIncrements > 0)
			{
				double d0 = this.posX + (this.newPosX - this.posX) / (double) this.newPosRotationIncrements;
				double d1 = this.posY + (this.newPosY - this.posY) / (double) this.newPosRotationIncrements;
				double d2 = this.posZ + (this.newPosZ - this.posZ) / (double) this.newPosRotationIncrements;
				double d3 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double) this.rotationYaw);
				this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.newPosRotationIncrements);
				this.rotationPitch = (float) ((double) this.rotationPitch + (this.newRotationPitch - (double) this.rotationPitch)
						/ (double) this.newPosRotationIncrements);
				--this.newPosRotationIncrements;
				this.setPosition(d0, d1, d2);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else if (!this.isClientWorld())
			{
				this.motionX *= 0.98D;
				this.motionY *= 0.98D;
				this.motionZ *= 0.98D;
			}

			if (Math.abs(this.motionX) < 0.005D)
			{
				this.motionX = 0.0D;
			}

			if (Math.abs(this.motionY) < 0.005D)
			{
				this.motionY = 0.0D;
			}

			if (Math.abs(this.motionZ) < 0.005D)
			{
				this.motionZ = 0.0D;
			}

			this.worldObj.theProfiler.startSection("ai");

			if (this.isMovementBlocked())
			{
				this.isJumping = false;
				this.moveStrafing = 0.0F;
				this.moveForward = 0.0F;
				this.randomYawVelocity = 0.0F;
			}

			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("jump");

			 if (this.isJumping)
			 {
				 if (!this.isInWater() && !this.handleLavaMovement())
				 {
					 if (this.onGround)
					 {
						 this.jump();
					 }
				 }
				 else
				 {
					 this.motionY += 0.03999999910593033D;
				 }
			 }

			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("travel");
			this.moveStrafing *= 0.98F;
			this.moveForward *= 0.98F;
			this.randomYawVelocity *= 0.9F;
			this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("push");

			if (!this.worldObj.isRemote)
			{
				this.collideWithNearbyEntities();
			}
			this.worldObj.theProfiler.endSection();

		} else
		{
			super.onLivingUpdate();
		}

	}

	@Override
	public double getMountedYOffset() {
		return super.getMountedYOffset();
	}

	@Override
	public void moveEntityWithHeading(float par1, float par2) {
		if (this.riddenByEntity != null)
		{
			this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
			this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
			par1 = ((EntityLivingBase) this.riddenByEntity).moveStrafing * 0.5F;
			par2 = ((EntityLivingBase) this.riddenByEntity).moveForward;

			if (par2 <= 0.0F)
			{
				par2 *= 0.25F;
			}

			this.stepHeight = 1.0F;
			this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

			if (!this.worldObj.isRemote)
			{
				this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
				super.moveEntityWithHeading(par1, par2);
			}

			if (this.onGround)
			{
				this.setJumping(false);
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
		} else
		{
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			super.moveEntityWithHeading(par1, par2);
		}
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();

		if (this.prevRearingAmount > 0.0F)
		{
			float f = MathHelper.sin(this.renderYawOffset * (float) Math.PI / 180.0F);
			float f1 = MathHelper.cos(this.renderYawOffset * (float) Math.PI / 180.0F);
			float f2 = 0.7F * this.prevRearingAmount;
			float f3 = 0.15F * this.prevRearingAmount;
			this.riddenByEntity.setPosition(this.posX + (f2 * f), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset()
					+  f3, this.posZ -  (f2 * f1));

			if (this.riddenByEntity instanceof EntityLivingBase)
			{
				((EntityLivingBase) this.riddenByEntity).renderYawOffset = this.renderYawOffset;
			}
		}
	}

	public void spiderJump() {
		this.getJumpHelper().setJumping();
		this.getJumpHelper().doJump();
		
	}

}
