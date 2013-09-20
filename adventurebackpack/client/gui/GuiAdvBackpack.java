package adventurebackpack.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import adventurebackpack.ModInformation;
import adventurebackpack.blocks.tileentities.TileAdvBackpack;
import adventurebackpack.common.BackpackContainer;
import adventurebackpack.common.Constants;
import adventurebackpack.common.IAdvBackpack;
import adventurebackpack.common.Textures;
import adventurebackpack.common.Utils;
import adventurebackpack.config.GeneralInfo;
import adventurebackpack.handlers.PacketHandler;
import adventurebackpack.inventory.InventoryItem;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvBackpack extends GuiContainer implements openGui{
	
	protected IAdvBackpack inventory;
	protected boolean source;
	protected boolean sbstatus;
	protected int X;
	protected int Y;
	protected int Z;
	@SuppressWarnings("unused")
	private EntityPlayer player;
	
	private static final ResourceLocation texture = Textures.resourceRL("textures/gui/BackPackGUI.png");
	private static GuiImageButton bedButton = new GuiImageButton(71, 15, 18, 18);
	private static GuiImageButton craftButton = new GuiImageButton(90, 15, 18, 18);
	private static GuiTank tankLeft = new GuiTank(26, 7, 64, 16, GeneralInfo.GUI_TANK_RES);
	private static GuiTank tankRight = new GuiTank(134, 7, 64, 16, GeneralInfo.GUI_TANK_RES);
	private static String fluidLeft, amntLeft, fluidRight, amntRight = "";
	
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
		if (inventory != null) inventory.closeChest();
		super.onGuiClosed();
	}
	
	@Override	
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);
		
		this.mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		//Buttons and button highlight	
		int srcX = 177;
		int srcY = 35;
		if(source){
			if (bedButton.inButton(this, mouseX, mouseY)){
				bedButton.draw(this, srcX+19, srcY+19);
				
			}else{
				bedButton.draw(this, srcX, srcY+19);
			}
		}
		
		if(craftButton.inButton(this, mouseX, mouseY)){
			craftButton.draw(this, srcX+19, srcY);
		}else{
			craftButton.draw(this, srcX, srcY);
		}
		
		
	}
	
	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		super.updateScreen();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name= " Adventure-pack";
		fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name))/2, 4, 0x404040);	
		FluidStack lft = inventory.getLeftTank().getFluid();
		FluidStack rgt = inventory.getRightTank().getFluid();
		
		tankLeft.draw(this, lft);
		tankRight.draw(this, rgt);
		
		if(tankLeft.inTank(this, mouseX, mouseY)){
			fluidLeft = (lft != null) ? Utils.capitalize(lft.getFluid().getName()) : "None";
			amntLeft  =	(lft != null) ? lft.amount  + "/" + Constants.tankCapacity: "Empty";
			ArrayList<String> tanktips = new ArrayList<String>();
			tanktips.add(fluidLeft);
			tanktips.add(amntLeft);
			drawHoveringText(tanktips, mouseX-guiLeft, mouseY-guiTop, fontRenderer);
		}
		
		if(tankRight.inTank(this, mouseX, mouseY)){
			fluidRight = (rgt != null) ? Utils.capitalize(rgt.getFluid().getName()) : "None";
			amntRight  = (rgt != null) ? rgt.amount  + "/" + Constants.tankCapacity: "Empty";
			ArrayList<String> tanktips = new ArrayList<String>();
			tanktips.add(fluidRight);
			tanktips.add(amntRight);
			drawHoveringText(tanktips, mouseX-guiLeft, mouseY-guiTop, fontRenderer);
		}
		
	}
	
	public float getZLevel(){
		return this.zLevel;
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	public int getLeft() {
		return guiLeft;
	}
	
	public int getTop() {
		return guiTop;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if(bedButton.inButton(this, mouseX, mouseY) && source){
			if(source){
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5,0,X,Y,Z));
			}else{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5,1));
			}
			
		}else
		if(craftButton.inButton(this, mouseX, mouseY)){
			if(source){
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5,2,0,X,Y,Z));
			}else{
				PacketDispatcher.sendPacketToServer(PacketHandler.makePacket(5,2,1));
			}
			
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int par3) {
		
		super.mouseMovedOrUp(mouseX, mouseY, par3);
	}
	
	
}
