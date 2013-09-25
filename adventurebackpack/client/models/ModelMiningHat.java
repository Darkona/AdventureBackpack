
package adventurebackpack.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMiningHat extends ModelBiped
{
  public static ModelBiped instance = new ModelMiningHat();
	//fields
    ModelRenderer hatBottom;
    ModelRenderer hatTop;
    ModelRenderer light;
    ModelRenderer lightRight;
    ModelRenderer lightLeft;
    ModelRenderer lightTop;
    ModelRenderer lightBottom;
  
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

      lightRight = new ModelRenderer(this, 32, 23);
      lightRight.addBox(-2F, -11F, -5F, 1, 2, 2);
    
      lightLeft = new ModelRenderer(this, 38, 23);
      lightLeft.addBox(1F, -11F, -5F, 1, 2, 2);  

      lightTop = new ModelRenderer(this, 0, 29);
      lightTop.addBox(-2F, -12F, -5F, 4, 1, 2);

      lightBottom = new ModelRenderer(this, 12, 29);
      lightBottom.addBox(-2F, -9F, -5F, 4, 1, 2);



      
      ModelRenderer[] array = {hatBottom, hatTop, light, lightRight, lightLeft, lightTop, lightBottom};
      
      for (ModelRenderer part : array){
    	  bipedHeadwear.addChild(part);
    	  setRotation(lightTop, 0F, 0F, 0F);
    	  part.setTextureSize(64, 32);
    	  part.setRotationPoint(0F, 0F, 0F);
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    ModelRenderer[] array = {hatBottom, hatTop, light, lightRight, lightLeft, lightTop, lightBottom};
    for (ModelRenderer part : array){
    	part.rotateAngleX = bipedHead.rotateAngleX;
    	part.rotateAngleY= bipedHead.rotateAngleY;
    	part.rotateAngleZ = bipedHead.rotateAngleZ;
    	
    }
    hatBottom.render(f5);
    hatTop.render(f5);
    light.render(f5);
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
