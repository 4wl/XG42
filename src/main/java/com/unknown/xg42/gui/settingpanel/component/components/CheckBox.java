package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.gui.settingpanel.utils.UserValueChangeListener;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class CheckBox extends AbstractComponent {
    private static final int PREFERRED_HEIGHT = 22;

    private boolean selected;
    private String title;
    private int preferredHeight;
    private boolean hovered;
    private ValueChangeListener<Boolean> listener;

    public CheckBox(String title, int preferredHeight) {

        this.preferredHeight = preferredHeight;

        setTitle(title);
    }

    public CheckBox(String title) {
        this(title, PREFERRED_HEIGHT);
    }

    @Override
    public void render() {
        GLUtil.drawRect(GL11.GL_QUADS, x, y, preferredHeight, preferredHeight, hovered ? Window.SECONDARY_FOREGROUND.getRGB() : Window.TERTIARY_FOREGROUND.getRGB());
        Color color = Window.isRainbow ? new Color(Rainbow.getRainbow(10, 0.8f, 1f)) :(hovered ? Window.TERTIARY_FOREGROUND : Window.SECONDARY_FOREGROUND);
        if (selected) {
            RenderUtils.drawRoundedRectangle(x + 2, y + 3, preferredHeight - 5, preferredHeight - 5, 7.0f, color);
        }
        RenderUtils.drawRoundedRectangleOutline(x, y, preferredHeight, preferredHeight, 7.0f, 1.0f, color);
        GLUtil.FontRenderer.drawString(title, x + preferredHeight + preferredHeight / 2f, (y + preferredHeight /2f) -  GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());
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

            if (hovered) {

                boolean newVal = !selected;
                boolean change = true;

                if (listener != null) {
                    change = listener.onValueChange(newVal);
                    UserValueChangeListener.ValueChange();
                }

                if (change) selected = newVal;

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

        setWidth(GLUtil.FontRenderer.getStringWidth(title) + preferredHeight + preferredHeight / 4);
        setHeight(preferredHeight);
    }

    public void setListener(ValueChangeListener<Boolean> listener) {
        this.listener = listener;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
