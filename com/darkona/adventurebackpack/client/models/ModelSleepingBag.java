package com.darkona.adventurebackpack.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSleepingBag extends ModelBase {
	// fields
	ModelRenderer Bottom;
	ModelRenderer Blanket;
	ModelRenderer Pillow;

	public ModelSleepingBag() {
		textureWidth = 128;
		textureHeight = 64;

		Bottom = new ModelRenderer(this, 0, 0);
		Bottom.addBox(0F, 0F, 0F, 16, 1, 32);
		Bottom.setRotationPoint(-8F, 23F, -8F);

		Blanket = new ModelRenderer(this, 0, 0);
		Blanket.addBox(0F, 0F, 0F, 16, 1, 21);
		Blanket.setRotationPoint(-8F, 22F, -8F);

		Pillow = new ModelRenderer(this, 0, 38);
		Pillow.addBox(0F, 0F, 0F, 12, 2, 6);
		Pillow.setRotationPoint(-6F, 21F, 18F);

		ModelRenderer meh[] = { Bottom, Blanket, Pillow };

		for (ModelRenderer model : meh)
		{
			model.setTextureSize(128, 64);
			model.offsetY -= 0.1F;
		}

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		Bottom.render(f5);
		Blanket.render(f5);
		Pillow.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
