package codechicken.lib.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rectangle4i;
import codechicken.lib.vec.Vector3;

public class RenderUtils
{
	static Vector3[] vectors = new Vector3[8];
	static
	{
		for (int i = 0; i < vectors.length; i++)
			vectors[i] = new Vector3();
	}

	public static void renderFluidQuad(Vector3 point1, Vector3 point2,
		Vector3 point3, Vector3 point4, Icon icon, double res)
	{
		double u1 = icon.getMinU();
		double du = icon.getMaxU() - icon.getMinU();
		double v2 = icon.getMaxV();
		double dv = icon.getMaxV() - icon.getMinV();

		Vector3 wide = vectors[0].set(point4).subtract(point1);
		Vector3 high = vectors[1].set(point1).subtract(point2);
		Tessellator t = Tessellator.instance;

		double wlen = wide.mag();
		double hlen = high.mag();

		double x = 0;
		while (x < wlen)
		{
			double rx = wlen - x;
			if (rx > res)
				rx = res;

			double y = 0;
			while (y < hlen)
			{
				double ry = hlen - y;
				if (ry > res)
					ry = res;

				Vector3 dx1 = vectors[2].set(wide).multiply(x / wlen);
				Vector3 dx2 = vectors[3].set(wide).multiply((x + rx) / wlen);
				Vector3 dy1 = vectors[4].set(high).multiply(y / hlen);
				Vector3 dy2 = vectors[5].set(high).multiply((y + ry) / hlen);

				t.addVertexWithUV(point2.x + dx1.x + dy2.x, point2.y + dx1.y
					+ dy2.y, point2.z + dx1.z + dy2.z, u1, v2 - ry / res * dv);
				t.addVertexWithUV(point2.x + dx1.x + dy1.x, point2.y + dx1.y
					+ dy1.y, point2.z + dx1.z + dy1.z, u1, v2);
				t.addVertexWithUV(point2.x + dx2.x + dy1.x, point2.y + dx2.y
					+ dy1.y, point2.z + dx2.z + dy1.z, u1 + rx / res * du, v2);
				t.addVertexWithUV(point2.x + dx2.x + dy2.x, point2.y + dx2.y
					+ dy2.y, point2.z + dx2.z + dy2.z, u1 + rx / res * du, v2
					- ry / res * dv);

				y += ry;
			}

			x += rx;
		}
	}

	public static void renderFluidCuboid(Cuboid6 bound, Icon tex, double res)
	{
		renderFluidQuad(
		// bottom
			new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(
				bound.max.x, bound.min.y, bound.min.z), new Vector3(
				bound.max.x, bound.min.y, bound.max.z), new Vector3(
				bound.min.x, bound.min.y, bound.max.z), tex, res);
		renderFluidQuad(
		// top
			new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(
				bound.min.x, bound.max.y, bound.max.z), new Vector3(
				bound.max.x, bound.max.y, bound.max.z), new Vector3(
				bound.max.x, bound.max.y, bound.min.z), tex, res);
		renderFluidQuad(
		// -x
			new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(
				bound.min.x, bound.min.y, bound.min.z), new Vector3(
				bound.min.x, bound.min.y, bound.max.z), new Vector3(
				bound.min.x, bound.max.y, bound.max.z), tex, res);
		renderFluidQuad(
		// +x
			new Vector3(bound.max.x, bound.max.y, bound.max.z), new Vector3(
				bound.max.x, bound.min.y, bound.max.z), new Vector3(
				bound.max.x, bound.min.y, bound.min.z), new Vector3(
				bound.max.x, bound.max.y, bound.min.z), tex, res);
		renderFluidQuad(
		// -z
			new Vector3(bound.max.x, bound.max.y, bound.min.z), new Vector3(
				bound.max.x, bound.min.y, bound.min.z), new Vector3(
				bound.min.x, bound.min.y, bound.min.z), new Vector3(
				bound.min.x, bound.max.y, bound.min.z), tex, res);
		renderFluidQuad(
		// +z
			new Vector3(bound.min.x, bound.max.y, bound.max.z), new Vector3(
				bound.min.x, bound.min.y, bound.max.z), new Vector3(
				bound.max.x, bound.min.y, bound.max.z), new Vector3(
				bound.max.x, bound.max.y, bound.max.z), tex, res);
	}

	public static boolean shouldRenderFluid(FluidStack stack)
	{
		return stack.amount > 0 && stack.getFluid() != null;
	}

	/**
	 * @param stack
	 *            The fluid stack to render
	 * @return The icon of the fluid
	 */
	public static Icon prepareFluidRender(FluidStack stack, int alpha)
	{
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Fluid fluid = stack.getFluid();
		CCRenderState.setColour(fluid.getColor(stack) << 8 | alpha);
		Minecraft.getMinecraft().renderEngine.bindTexture(fluid
			.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture
			: TextureMap.locationItemsTexture);
		return fluid.getIcon(stack);
	}

	/**
	 * Re-enables lighting and disables blending.
	 */
	public static void postFluidRender()
	{
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static double fluidDensityToAlpha(double density)
	{
		return Math.pow(density, 0.4);
	}

	/**
	 * Renders a fluid within a bounding box. If the fluid is a liquid it will
	 * render as a normal tank with height equal to density/bound.height. If the
	 * fluid is a gas, it will render the full box with an alpha equal to
	 * density. Warning, bound will be mutated if the fluid is a liquid
	 * 
	 * @param stack
	 *            The fluid to render.
	 * @param bound
	 *            The box within which the fluid is contained.
	 * @param density
	 *            The volume of fluid / the capacity of the tank. For gases this
	 *            determines the alpha, for liquids this determines the height.
	 * @param res
	 *            The resolution to render at.
	 */
	public static void renderFluidCuboid(FluidStack stack, Cuboid6 bound,
		double density, double res)
	{
		if (!shouldRenderFluid(stack))
			return;

		int alpha = 255;
		if (stack.getFluid().isGaseous())
			alpha = (int) (fluidDensityToAlpha(density) * 255);
		else
			bound.max.y = bound.min.y + (bound.max.y - bound.min.y) * density;

		Icon tex = prepareFluidRender(stack, alpha);
		CCRenderState.startDrawing(7);
		renderFluidCuboid(bound, tex, res);
		CCRenderState.draw();
		postFluidRender();
	}

	public static void renderFluidGauge(FluidStack stack, Rectangle4i rect,
		double density, double res)
	{
		if (!shouldRenderFluid(stack))
			return;

		int alpha = 255;
		if (stack.getFluid().isGaseous())
			alpha = (int) (fluidDensityToAlpha(density) * 255);
		else
		{
			int height = (int) (rect.h * density);
			rect.y += rect.h - height;
			rect.h = height;
		}

		Icon tex = prepareFluidRender(stack, alpha);
		CCRenderState.startDrawing(7);
		renderFluidQuad(new Vector3(rect.x, rect.y, 0), new Vector3(rect.x,
			rect.y + rect.h, 0), new Vector3(rect.x + rect.w, rect.y + rect.h,
			0), new Vector3(rect.x + rect.w, rect.y, 0), tex, res);
		CCRenderState.draw();
		postFluidRender();
	}
}
