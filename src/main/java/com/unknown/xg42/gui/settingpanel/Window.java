package com.unknown.xg42.gui.settingpanel;

import com.unknown.xg42.XG42;
import com.unknown.xg42.gui.settingpanel.component.components.Pane;
import com.unknown.xg42.gui.settingpanel.component.components.Spoiler;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.module.modules.client.SettingPanel;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import com.unknown.xg42.utils.font.RFontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Window {
    /* Color constants */
    
    public static Color EnableColor = new Color(60, 60, 60, 240);
    public static Color SECONDARY_FOREGROUND = new Color(80, 80, 80, 240);
    public static Color TERTIARY_FOREGROUND = new Color(20, 20, 20, 150);
    public static Color SECONDARY_OUTLINE = new Color(10, 10, 10, 255);
    public static Color BACKGROUND = new Color(20, 20, 20, 220);
    public static Color FOREGROUND = Color.white;
    public static boolean isRainbow;

    private String title;
    private int y;
    private int x;
    private int width;
    private int height;

    private RFontRenderer RainbowFont = new RFontRenderer(new Font("Consoles", 0, 18), true, false);

    private int headerHeight;

    private boolean beingDragged;
    private int dragX;
    private int dragY;

    private Pane contentPane;
    private Pane SpoilerPane;

    public Window(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        isRainbow = GuiManager.getINSTANCE().isRainbow();
        int fontHeight = GLUtil.FontRenderer.getHeight();
        int headerFontOffset = fontHeight / 2;

        headerHeight = headerFontOffset * 2 + fontHeight;
        RenderUtils.drawRoundedRectangle(x, y, width, height, 15, BACKGROUND);
        RenderUtils.drawHalfRoundedRectangle(x , y, width, headerHeight, 15, RenderUtils.HalfRoundedDirection.Top, Rainbow.getRainbowColor(10, 0.6f, 1f));

        if (Window.isRainbow){
            RenderUtils.drawHalfRoundedRectangle(x , y, width, headerHeight, 15, RenderUtils.HalfRoundedDirection.Top, new Color(50, 50, 50, 77));
            RainbowFont.drawStringWithShadow(title ,x + width / 2f - RainbowFont.getStringWidth(title) / 2f, y + headerFontOffset, 4, 1, 1, 20, 255);
        }else {
            GLUtil.drawRect(7, x, y, width, headerHeight, SECONDARY_FOREGROUND.getRGB());
            XG42.getFontRenderer().drawStringWithShadow(title ,x + width / 2f - XG42.getFontRenderer().getStringWidth(title) / 2f, y + headerFontOffset, -1);
        }
        if (contentPane != null) {
            if (contentPane.isSizeChanged()) {
                contentPane.setSizeChanged(false);
            }
            contentPane.setX(x);
            contentPane.setY(y + headerHeight + 15);
            contentPane.setWidth(125);
            contentPane.setHeight(height - headerHeight);
            contentPane.render();
        }
        if (SpoilerPane != null) {
            if (SpoilerPane.isSizeChanged()) {
                SpoilerPane.setSizeChanged(false);
            }
            SpoilerPane.setX(x + 130);
            SpoilerPane.setY(y + headerHeight);
            SpoilerPane.setWidth(width-130);
            SpoilerPane.setHeight(height - headerHeight);
            SpoilerPane.render();
        }
    }

    public void mousePressed(int button, int x, int y) {
        if (this.contentPane != null) contentPane.mousePressed(button, x, y, false);
        if (this.SpoilerPane != null) SpoilerPane.mousePressed(button, x, y, false);
        if (button == 0) {
            if (x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.headerHeight) {
                beingDragged = true;
                dragX = this.x - x;
                dragY = this.y - y;
            }
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (beingDragged) {
            this.x = mouseX + dragX;
            this.y = mouseY + dragY;
        }
    }

    public void mouseReleased(int button, int x, int y) {
        if (this.contentPane != null) contentPane.mouseReleased(button, x, y, false);
        if (this.SpoilerPane != null) SpoilerPane.mouseReleased(button, x, y, false);

        if (button == 0) {
            beingDragged = false;
        }
    }

    public void mouseMoved(int x, int y) {
        if (this.contentPane != null) contentPane.mouseMove(x, y, false);
        if (this.SpoilerPane != null) SpoilerPane.mouseMove(x, y, false);
        drag(x, y);
    }

    public void setContentPane(Pane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSpoilerPane(Pane spoilerPane) {
        SpoilerPane = spoilerPane;
    }

    public void keyPressed(int key, char c) {
        if (this.contentPane != null) contentPane.keyPressed(key, c);
        if (this.SpoilerPane != null) SpoilerPane.keyPressed(key, c);
    }

    public void mouseWheel(int change) {
        if (this.contentPane != null) contentPane.mouseWheel(change);
        if (this.SpoilerPane != null) SpoilerPane.mouseWheel(change);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}