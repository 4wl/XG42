package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ActionEventListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class Spoiler extends AbstractComponent {
    private static final int PREFERRED_HEIGHT = 28;
    public int preferredWidth = 614;
    private String title;
    public int preferredHeight;
    private boolean hovered;
    private ActionEventListener listener;
    private Pane contentPane;
    private boolean opened = false;

    public Spoiler(String title, int preferredWidth, int preferredHeight, Pane contentPane) {
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.contentPane = contentPane;

        setTitle(title);
    }

    public Spoiler(String title, int preferredWidth, Pane contentPane) {
        this(title, preferredWidth, PREFERRED_HEIGHT, contentPane);
    }

    public IModule getModule(){
        return ModuleManager.getModuleByName(getTitle());
    }

    @Override
    public void render() {
        if (getModule().isEnabled()) GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), preferredHeight, Window.EnableColor.getRGB());
        else if (hovered) GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), preferredHeight, Window.SECONDARY_FOREGROUND.getRGB());

        if (opened) {
            updateBounds();
            contentPane.setX(getX());
            contentPane.setY(getY() + preferredHeight);

            contentPane.render();
            GL11.glLineWidth(1.0f);
            if (Window.isRainbow){
                RenderUtils.drawRoundedRectangleOutline( x, y, getWidth() , preferredHeight, 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
                RenderUtils.drawRoundedRectangleOutline( x, y, getWidth(), getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
            }else {
                GLUtil.drawRect(GL11.GL_LINE_LOOP, x, y, getWidth(), getHeight(), Window.SECONDARY_FOREGROUND.getRGB());
            }
        }else {
            if (Window.isRainbow) {
                RenderUtils.drawRoundedRectangleOutline(x , y, getWidth() , preferredHeight, 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
            }else
                GLUtil.drawRect(GL11.GL_LINE_LOOP, x, y, getWidth(), preferredHeight, Window.SECONDARY_FOREGROUND.getRGB());
        }

        GLUtil.FontRenderer.drawString(title, x + getWidth() / 2f - GLUtil.FontRenderer.getStringWidth(title) / 2f, (y + preferredHeight / 2f) - GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());

        if (hovered){
            float widthD = GLUtil.FontRenderer.getStringWidth(getModule().description) + 15;
            float heightD = GLUtil.FontRenderer.getHeight() + 5;
            float xd = lastmousex + 7;
            float yd = lastmousey - (heightD / 2);
            GLUtil.drawRect(GL11.GL_QUADS, xd, yd, widthD, heightD, new Color(40, 40, 40, 191).getRGB());
            GLUtil.FontRenderer.drawCenteredString(getModule().description, xd + (widthD / 2), (yd + ((heightD) / 2)) - ((heightD - 5) / 2), new Color(255, 255, 255).getRGB());
            //RenderUtil.drawRect(p_MouseX+15, p_MouseY, p_MouseX+19+RenderUtil.getStringWidth(HoveredItem.GetDescription()), p_MouseY + RenderUtil.getStringHeight(HoveredItem.GetDescription())+3, 0x90000000);
            //RenderUtil.drawStringWithShadow(HoveredItem.GetDescription(), p_MouseX+17, p_MouseY, 0xFFFFFF);
        }
    }

    public int lastmousex = 0, lastmousey = 0;

    public Pane getContentPane() {
        return contentPane;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void setContentPane(Pane newContentPane) {
        this.contentPane = newContentPane;
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        lastmousey = y;
        lastmousex = x;
        updateHovered(x, y, offscreen);
        return opened && contentPane.mouseMove(x, y, offscreen);
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + preferredHeight;
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            if (x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + this.preferredHeight) {
                updateHovered(x, y, offscreen);
                if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null)
                    ModuleManager.getModuleByName(getTitle()).toggle();
                return false;
            }
        }else if (button == 1) {
            updateHovered(x, y, offscreen);

            if (hovered) {
                opened = !opened;

                contentPane.updateLayout();

                updateBounds();
                return true;
            }
        }

        return opened && contentPane.mousePressed(button, x, y, offscreen);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        updateBounds();
    }

    private void updateBounds() {
        setWidth(Math.max(Math.max(GLUtil.FontRenderer.getStringWidth(getTitle()), contentPane.getHeight()), preferredWidth));
        setHeight(Math.max(GLUtil.FontRenderer.getHeight() * 5 / 4, preferredHeight) + (opened ? contentPane.getHeight() : 0));
    }

    public void setListener(ActionEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        return opened && contentPane.mouseReleased(button, x, y, offscreen);
    }

    @Override
    public boolean mouseWheel(int change) {
        return opened && contentPane.mouseWheel(change);
    }

    @Override
    public boolean keyPressed(int key, char c) {
        return opened && contentPane.keyPressed(key, c);
    }
}
