
package adventurebackpack.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ModelMiningHat extends ModelBiped
{
  public static ModelMiningHat instance = new ModelMiningHat();
  
  public ModelMiningHat setHelmetStack(ItemStack helmet){
	  this.helmet = helmet;
	  return (ModelMiningHat) instance;
  };
	//fields
  	ItemStack helmet;
    ModelRenderer hatBottom;
    ModelRenderer hatTop;
    ModelRenderer light;
    ModelRenderer lightRight;
    ModelRenderer lightLeft;
    ModelRenderer lightTop;
    ModelRenderer lightBottom;
	ModelRenderer lightOff;
	ModelRenderer lightAuto;
	
  public ModelMiningHat()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      hatBottom = new ModelRenderer(this, 0, 0);
      hatBottom.addBox(-5F, -9F, -5F, 10, 3, 10);

      hatTop = new ModelRenderer(this, 0, 13);
      hatTop.addBox(-4F, -12F, -3F, 8, 3, 7);

      light = new ModelRenderer(this, 0, 25);
      light.addBox(-1F, -11F, -5F, 2, 2, 2);
      
      lightOff = new ModelRenderer(this, 8, 25);
      lightOff.addBox(-1F, -11F, -5F, 2, 2, 2);
      
      lightAuto = new ModelRenderer(this, 16, 25);
      lightAuto.addBox(-1F, -11F, -5F, 2, 2, 2);

      lightRight = new ModelRenderer(this, 32, 23);
      lightRight.addBox(-2F, -11F, -5F, 1, 2, 2);
    
      lightLeft = new ModelRenderer(this, 38, 23);
      lightLeft.addBox(1F, -11F, -5F, 1, 2, 2);  

      lightTop = new ModelRenderer(this, 0, 29);
      lightTop.addBox(-2F, -12F, -5F, 4, 1, 2);

      lightBottom = new ModelRenderer(this, 12, 29);
      lightBottom.addBox(-2F, -9F, -5F, 4, 1, 2);
      
      for (Object part : this.boxList){
    	  setRotation((ModelRenderer)part, 0F, 0F, 0F);
    	  ((ModelRenderer)part).setTextureSize(64, 32);
    	  ((ModelRenderer)part).setRotationPoint(0F, 0F, 0F);
    	  ((ModelRenderer)part).offsetY += 0.1f;
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);

    for (Object part : this.boxList){
    	((ModelRenderer)part).rotateAngleX = bipedHead.rotateAngleX;
    	((ModelRenderer)part).rotateAngleY= bipedHead.rotateAngleY;
    	((ModelRenderer)part).rotateAngleZ = bipedHead.rotateAngleZ;
    }
    if(helmet != null && helmet.hasTagCompound()){ 
    	doRender(f5, helmet.getTagCompound().getByte("mode"));
    }
    else doRender(f5,(byte)0);
  }
  
  public void render2(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, ItemStack helmet){
	  setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	  if(helmet.hasTagCompound()){ 
	    	doRender(f5, helmet.getTagCompound().getByte("mode"));
	  }else{
	  doRender(f5, (byte) 0);
	  }
  }
  
  private void doRender(float f5, byte mode){
	  
    	switch(mode){
    	case 1 : light.render(f5);break;
    	case 2 : lightAuto.render(f5);break;
    	case 0 : default: lightOff.render(f5);break;
    	}
	    hatBottom.render(f5);
	    hatTop.render(f5);
	    lightRight.render(f5);
	    lightLeft.render(f5);
	    lightTop.render(f5);
	    lightTop.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
