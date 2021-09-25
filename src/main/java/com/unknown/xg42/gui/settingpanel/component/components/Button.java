package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ActionEventListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

public class Button extends AbstractComponent {
    private static final int PREFERRED_WIDTH = 180;
    private static final int PREFERRED_HEIGHT = 22;

    private String title;
    private int preferredWidth;
    private int preferredHeight;
    private boolean hovered;
    private ActionEventListener listener;

    public Button(String title, int preferredWidth, int preferredHeight) {

        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        setWidth(preferredWidth);
        setHeight(preferredHeight);

        setTitle(title);
    }

    public Button(String title) {
        this(title, PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    @Override
    public void render() {
        if (Window.isRainbow){
            GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), getHeight(), hovered ? Window.SECONDARY_FOREGROUND.getRGB() : Window.TERTIARY_FOREGROUND.getRGB());
            RenderUtils.drawRoundedRectangleOutline(x , y, getWidth() , getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
            GLUtil.FontRenderer.drawString(title, x + getWidth() / 2f - GLUtil.FontRenderer.getStringWidth(title) / 2f, (y + getHeight() / 2f) - GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());
        }else {
            GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), getHeight(), hovered ? Window.SECONDARY_FOREGROUND.getRGB() : Window.TERTIARY_FOREGROUND.getRGB());
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, hovered ? Window.SECONDARY_OUTLINE : Window.SECONDARY_FOREGROUND);
            GLUtil.FontRenderer.drawString(title, x + getWidth() / 2f - GLUtil.FontRenderer.getStringWidth(title) / 2f, (y + getHeight() / 2f) - GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());
        }
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);
        return false;
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            updateHovered(x, y, offscreen);

            if (hovered && listener != null) {
                listener.onActionEvent();

                return true;
            }
        }

        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        setWidth(Math.max(GLUtil.FontRenderer.getStringWidth(title), preferredWidth));
        setHeight(Math.max(GLUtil.FontRenderer.getHeight() * 5 / 4, preferredHeight));
    }

    public ActionEventListener getOnClickListener() {
        return listener;
    }

    public void setOnClickListener(ActionEventListener listener) {
        this.listener = listener;
    }
}
