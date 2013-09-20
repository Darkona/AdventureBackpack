package adventurebackpack.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

public class RendererHose implements IItemRenderer
{

	private static RenderItem renderHose = new RenderItem();
	private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	private Tessellator tessellator = Tessellator.instance;

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
		ItemRendererHelper helper)
	{
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void renderItem(ItemRenderType type, ItemStack hose, Object... data)
	{
		switch (type)
		{
			case INVENTORY :

				// ====================Render the item=====================
				Icon icon = hose.getIconIndex();
				renderHose.renderIcon(0, 0, icon, 16, 16);

				if (hose.hasTagCompound())
				{
					NBTTagCompound nbt = hose.stackTagCompound;
					String amount = nbt.getInteger("amount") + "";
					String name = nbt.getString("fluid");
					String mode;
					switch (nbt.getInteger("mode"))
					{
						case 0 :
							mode = "Suck";
							break;
						case 1 :
							mode = "Spill";
							break;
						case 2 :
							mode = "Drink";
							break;
						default :
							mode = "Useless";
							break;
					}
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
						GL11.GL_ONE_MINUS_SRC_ALPHA);

					// Set drawing mode. Tessellator should support most drawing
					// modes.
					tessellator.startDrawing(GL11.GL_QUADS);
					// Set semi-transparent black color
					tessellator.setColorRGBA(0, 0, 0, 0);

					// Draw a 8x8 square
					tessellator.addVertex(0, 0, 0);
					tessellator.addVertex(0, 8, 0);
					tessellator.addVertex(8, 8, 0);
					tessellator.addVertex(8, 0, 0);

					tessellator.draw();

					GL11.glDepthMask(true);
					GL11.glDisable(GL11.GL_BLEND);

					GL11.glEnable(GL11.GL_TEXTURE_2D);
					// Get our text value

					// Draw our text at (1, 1) with white color

					GL11.glPushMatrix();
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					fontRenderer.drawStringWithShadow(mode, 0, 0, 0xFFFFFF);
					fontRenderer.drawStringWithShadow(amount, 0, 18, 0xFFFFFF);
					fontRenderer.drawStringWithShadow(name, 0, 24, 0xFFFFFF);

					GL11.glPopMatrix();
				}

				break;

		}

	}

}
