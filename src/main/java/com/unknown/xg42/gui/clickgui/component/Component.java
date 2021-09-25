package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.XG42;
import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.utils.font.CFontRenderer;
import net.minecraft.client.Minecraft;

public abstract class Component {
    public CFontRenderer font = XG42.getFontRenderer();
    public Minecraft mc = Minecraft.getMinecraft();
    public int x,y,width,height;
    public Panel father;
    public double add;
    public boolean isToggled,isExtended;
    public abstract void render(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased(int mouseX, int mouseY, int state) { }
    public void keyTyped(char typedChar, int keyCode) { }
    public void setAdd(double add){this.add = add;}

    public void solvePos(){
        this.x = father.x;
        this.y = father.y;
    }
    protected boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= Math.min(x,x+width) && mouseX <= Math.max(x,x+width)  && mouseY >= Math.min(y,y+height) && mouseY <= Math.max(y,y+height);
    }
}
