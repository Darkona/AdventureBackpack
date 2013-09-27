package com.darkona.adventurebackpack.client.gui;

import net.minecraft.util.Icon;

public interface IBackpackGui {

	public void drawTexturedModelRectFromIcon(int i, int j, Icon icon, int h, int w);

	public int getLeft();

	public int getTop();

	public void drawTexturedModalRect(int i, int j, int srcX, int srcY, int w, int h);

	public float getZLevel();

}
