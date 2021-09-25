package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.gui.settingpanel.utils.UserValueChangeListener;
import com.unknown.xg42.setting.ModeSetting;
import com.unknown.xg42.setting.Setting;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.List;

public class ComboBox extends AbstractComponent {
    private static final int PREFERRED_WIDTH = 180;
    private static final int PREFERRED_HEIGHT = 22;

    private int preferredWidth;
    private int preferredHeight;
    private boolean hovered;
    private boolean hoveredExtended;
    private ValueChangeListener<Integer> listener;
    private String[] values;
    private int selectedIndex;

    private boolean opened;
    private int mouseX;
    private int mouseY;

    public ComboBox(int preferredWidth, int preferredHeight, String[] values, int selectedIndex) {
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;

        this.values = values;
        this.selectedIndex = selectedIndex;

        setWidth(preferredWidth);
        updateHeight();
    }

    public ComboBox(String[] values, int selectedIndex) {
        this(PREFERRED_WIDTH, PREFERRED_HEIGHT, values, selectedIndex);
    }

    private void updateHeight() {
        if (opened) setHeight(preferredHeight * values.length + 4);
        else setHeight(preferredHeight);
    }

    @Override
    public void render() {
        updateHeight();

        GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), getHeight(), Window.TERTIARY_FOREGROUND.getRGB());

        if (hovered)
            GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), preferredHeight, Window.SECONDARY_FOREGROUND.getRGB());
        else if (hoveredExtended) {
            int offset = preferredHeight + 4;

            for (int i = 0; i < values.length; i++) {
                if (i == selectedIndex)
                    continue;

                int height = preferredHeight;

                if ((selectedIndex == 0 ? i == 1 : i == 0)
                        || (selectedIndex == values.length - 1 ? i == values.length - 2
                        : i == values.length - 1))
                    height++;

                if (mouseY >= getY() + offset
                        && mouseY <= getY() + offset + preferredHeight) {
                    GLUtil.drawRect(GL11.GL_QUADS, x, y + offset, getWidth(), preferredHeight, Window.SECONDARY_FOREGROUND.getRGB());
                    break;
                }
                offset += height;
            }
        }
        // Draw triangle background
        GLUtil.drawRect(GL11.GL_QUADS, x + getWidth() - preferredHeight, y, preferredHeight, getHeight(), (hovered || opened) ? Window.TERTIARY_FOREGROUND.getRGB() : Window.SECONDARY_FOREGROUND.getRGB());
        // Draw triangle
//        renderer.drawTriangle(
//                x + getWidth() - getHeight() + getHeight() / 4.0, y + getHeight() / 4.0,
//                x + getWidth() - getHeight() + getHeight() / 2.0, y + getHeight() / 4.0 + getHeight() / 2.0,
//                x + getWidth() - getHeight() + getHeight() / 4.0, y + getHeight() / 4.0,
//                Window.FOREGROUND);]
        GL11.glPushMatrix();

        double x1, x2, x3;
        double y1, y2, y3;

        if (opened) {
            x3 = x + getWidth() - preferredHeight + preferredHeight / 4.0;         y3 = y + preferredHeight * 3.0 / 4.0;
            x2 = x + getWidth() - preferredHeight + preferredHeight / 2.0;         y2 = y + preferredHeight / 4.0;
            x1 = x + getWidth() - preferredHeight + preferredHeight * 3.0 / 4.0;   y1 = y + preferredHeight * 3.0 / 4.0;
        }else {
            x1 = x + getWidth() - preferredHeight + preferredHeight / 4.0;         y1 = y + preferredHeight / 4.0;
            x2 = x + getWidth() - preferredHeight + preferredHeight / 2.0;         y2 = y + preferredHeight * 3.0 / 4.0;
            x3 = x + getWidth() - preferredHeight + preferredHeight * 3.0 / 4.0;   y3 = y + preferredHeight / 4.0;
        }

        if (Window.isRainbow) {
            GLUtil.drawTriangle(
                    x1, y1,
                    x2, y2,
                    x3, y3,
                    new Color(Rainbow.getRainbow(10, 0.8f, 1f)));
        }else {
            GLUtil.drawTriangle(
                    x1, y1,
                    x2, y2,
                    x3, y3,
                    Window.FOREGROUND);
        }
        GL11.glPopMatrix();

        if (Window.isRainbow) {
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
        }else {
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, (hovered && !opened) ? Window.SECONDARY_OUTLINE : Window.SECONDARY_FOREGROUND);
        }

        String text = values[selectedIndex];

        GLUtil.FontRenderer.drawString(text, x + 4, (y + preferredHeight / 2f) - GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());


        if (opened) {
            int offset = preferredHeight + 8;

            for (int i = 0; i < values.length; i++) {
                if (i == selectedIndex)
                    continue;

                GLUtil.FontRenderer.drawString(values[i], x + 4, y + offset, Window.FOREGROUND.getRGB());

                offset += preferredHeight;
            }
        }
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        return false;
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + preferredHeight;
        hoveredExtended = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();

        mouseX = x;
        mouseY = y;
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        if (button != 0)
            return false;

        if (hovered) {
            setOpened(!opened);
            updateHeight();
            return true;
        }
        if (hoveredExtended && opened) {
            int offset = this.y + preferredHeight + 4;

            for (int i = 0; i < values.length; i++) {
                if (i == selectedIndex)
                    continue;

                if (y >= offset
                        && y <= offset
                        + preferredHeight) {
                    setSelectedChecked(i);
                    setOpened(false);
                    break;
                }
                offset += preferredHeight;
            }
            updateHovered(x, y, offscreen);
            return true;
        }
        return false;
    }

    private void setSelectedChecked(int i) {
        boolean change = true;

        if (listener != null) {
            change = listener.onValueChange(i);
            UserValueChangeListener.ValueChange();
            listener.onValueChange(i);
        }
        if (change) selectedIndex = i;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(String[] modes, List<ModeSetting.Mode> con, Setting value) {
        int finalSelectedIndex = 0;
        for (int i = 0; i < modes.length; i++) {
            if (((ModeSetting) value).getValue() == con.get(i)) {
                finalSelectedIndex = i;
                continue;
            }
        }
        this.selectedIndex = finalSelectedIndex;
    }

    public void setListener(ValueChangeListener<Integer> listener) {
        this.listener = listener;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
        updateHeight();
    }
}
