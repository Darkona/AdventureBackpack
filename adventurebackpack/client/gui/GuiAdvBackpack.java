package adventurebackpack.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.common.IAdvBackpack;
import adventurebackpack.common.Textures;
import adventurebackpack.config.GeneralInfo;
import adventurebackpack.handlers.PacketHandler;
import adventurebackpack.inventory.BackpackContainer;
import adventurebackpack.inventory.InventoryItem;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvBackpack extends GuiContainer implements IBackpackGui {

	protected IAdvBackpack inventory;
	protected boolean source;
	protected boolean sbstatus;
	protected int X;
	protected int Y;
	protected int Z;

	@SuppressWarnings("unused")
	private EntityPlayer player;
	private static final ResourceLocation texture = Textures.resourceRL("textures/gui/guiBackpack.png");
	private static GuiImageButton bedButton = new GuiImageButton(71, 15, 18, 18);
	private static GuiImageButton craftButton = new GuiImageButton(90, 15, 18, 18);
	private static GuiTank tankLeft = new GuiTank(26, 7, 64, 16, GeneralInfo.GUI_TANK_RES);
	private static GuiTank tankRight = new GuiTank(134, 7, 64, 16, GeneralInfo.GUI_TANK_RES);
	private FluidStack lft;
	private FluidStack rgt;
	
	public GuiAdvBackpack(EntityPlayer player, TileAdvBackpack tileBackpack) {
		super(new BackpackContainer(player.inventory, tileBackpack));
		this.inventory = tileBackpack;
		this.source = true;
		this.sbstatus = tileBackpack.sleepingBagDeployed;
		xSize = 176;
		ySize = 166;
		this.X = tileBackpack.xCoord;
		this.Y = tileBackpack.yCoord;
		this.Z = tileBackpack.zCoord;
		this.player = player;
	}

	public GuiAdvBackpack(EntityPlayer player, InventoryItem item) {
		super(new BackpackContainer(player.inventory, item));
		this.inventory = item;
		this.source = false;
		xSize = 176;
		ySize = 166;
		this.player = player;
	}

	@Override
	public void onGuiClosed() {
		if (inventory != null)
			inventory.closeChest();
		super.onGuiClosed();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);

		this.mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// Buttons and button highlight
		int srcX = 177;
		int srcY = 35;
		if (source)
		{
			if (bedButton.inButton(this, mouseX, mouseY))
			{
				bedButton.draw(this, srcX + 19, srcY + 19);

			} else
			{
				bedButton.draw(this, srcX, srcY + 19);
			}
		}

		if (craftButton.inButton(this, mouseX, mouseY))
		{
			craftButton.draw(this, srcX + 19, srcY);
		} else
		{
			craftButton.draw(this, srcX, srcY);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = " Adventure-pack";
		fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 4, 0x404040);
		lft = inventory.getLeftTank().getFluid();
		rgt = inventory.getRightTank().getFluid();

		tankLeft.draw(this, lft);
		tankRight.draw(this, rgt);

		if (tankLeft.inTank(this, mouseX, mouseY))
		{
			drawHoveringText(tankLeft.getTankTooltip(), mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}

		if (tankRight.inTank(this, mouseX, mouseY))
		{
			drawHoveringText(tankRight.getTankTooltip(), mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}

	}

	@Override
	public float getZLevel() {
		return this.zLevel;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public int getLeft() {
		return guiLeft;
	}

	@Override
	public int getTop() {
		return guiTop;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (bedButton.inButton(this, mouseX, mouseY) && source)
		{
			if (source)
			{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5, 0, X, Y, Z));
			} else
			{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5, 1));
			}

		} else if (craftButton.inButton(this, mouseX, mouseY))
		{
			if (source)
			{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5, 2, 0, X, Y, Z));
			} else
			{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5, 2, 1));
			}

		}
		super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int par3) {

		super.mouseMovedOrUp(mouseX, mouseY, par3);
	}

}
