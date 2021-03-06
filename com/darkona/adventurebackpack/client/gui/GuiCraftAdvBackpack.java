package com.darkona.adventurebackpack.client.gui;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.darkona.adventurebackpack.blocks.tileentities.TileAdvBackpack;
import com.darkona.adventurebackpack.common.IAdvBackpack;
import com.darkona.adventurebackpack.common.Textures;
import com.darkona.adventurebackpack.config.GeneralInfo;
import com.darkona.adventurebackpack.handlers.PacketHandler;
import com.darkona.adventurebackpack.inventory.BackCraftContainer;
import com.darkona.adventurebackpack.inventory.InventoryItem;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftAdvBackpack extends GuiContainer implements IBackpackGui {

	protected IAdvBackpack inventory;
	protected boolean source;
	private boolean wearing;
	protected int X;
	protected int Y;
	protected int Z;
	protected EntityPlayer player;
	/* Tanks */
	private static GuiTank tankLeft = new GuiTank(8, 7, 64, 16, GeneralInfo.GUI_TANK_RES);
	private static GuiTank tankRight = new GuiTank(153, 7, 64, 16, GeneralInfo.GUI_TANK_RES);

	/* Buttons */
	private static GuiImageButton backButton = new GuiImageButton(114, 24, 18, 18);

	private static final ResourceLocation texture = Textures.resourceRL("textures/gui/guiBackpackCraft.png");

	public GuiCraftAdvBackpack(EntityPlayer player, TileAdvBackpack tile) {
		super(new BackCraftContainer(player, tile));
		this.inventory = tile;
		this.source = true;
		xSize = 176;
		ySize = 166;
		X = tile.xCoord;
		Y = tile.yCoord;
		Z = tile.zCoord;
	}

	public GuiCraftAdvBackpack(EntityPlayer player, InventoryItem item, boolean wearing) {
		super(new BackCraftContainer(player, player.worldObj, item));
		this.inventory = item;
		this.player = player;
		this.wearing = wearing;
		this.source = false;
		xSize = 176;
		ySize = 166;
	}

	@Override
	public void onGuiClosed() {
		if (inventory != null)
			inventory.closeChest();
		super.onGuiClosed();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		// this.mc.func_110434_K().func_110577_a(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int srcX = 177;
		int srcY = 111;

		if (backButton.inButton(this, mouseX, mouseY))
		{
			backButton.draw(this, srcX + 19, srcY);
		} else
		{
			backButton.draw(this, srcX, srcY);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		this.fontRenderer.drawString(I18n.getString("container.crafting"), 92, 7, 4210752);
		GuiCraftAdvBackpack.tankLeft.draw(this, inventory.getLeftTank().getFluid());
		GuiCraftAdvBackpack.tankRight.draw(this, inventory.getRightTank().getFluid());
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
	public float getZLevel() {
		return this.zLevel;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (backButton.inButton(this, mouseX, mouseY))
		{
			if(source){
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(0, X, Y, Z));
			} else{
				((EntityClientPlayerMP) player).sendQueue.addToSendQueue(PacketHandler.makePacket(wearing ? 1 : 2));
			}
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

}
