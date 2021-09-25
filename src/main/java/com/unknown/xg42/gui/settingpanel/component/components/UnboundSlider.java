package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

public class UnboundSlider extends AbstractComponent {
    private static final int PREFERRED_WIDTH = 180;
    private static final int PREFERRED_HEIGHT = 24;

    double value;
    public int sensitivity = 5;

    private boolean hovered;
    private ValueChangeListener<Number> listener;

    int originX;
    double originValue;

    boolean integer;
    double max = Double.MAX_VALUE;
    double min = Double.MIN_VALUE;

    public UnboundSlider(double value, boolean integer) {
        this.value = value;
        this.integer = integer;

        setHeight(PREFERRED_HEIGHT);
        setWidth(PREFERRED_WIDTH);
    }

    @Override
    public void render() {
        if (Window.isRainbow)
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
        else
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, (hovered) ? Window.SECONDARY_OUTLINE : Window.SECONDARY_FOREGROUND);
        GLUtil.drawRect(7, x, y, getWidth(), getHeight(), (hovered) ? Window.SECONDARY_FOREGROUND.getRGB() : Window.TERTIARY_FOREGROUND.getRGB());
        GLUtil.FontRenderer.drawString(String.valueOf(value), x + getWidth() / 2f - GLUtil.FontRenderer.getStringWidth(String.valueOf(value)) / 2f, (y + getHeight()/2f) - GLUtil.FontRenderer.getHeight() / 2f, Window.FOREGROUND.getRGB());
    }

    private void updateValue(int x, int y) {

    }

    private boolean isDrag;

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        originX = x;
        originValue = getValue();
        if (button == 0) {
            if (x >= this.x && y >= this.y && x <= this.x + this.getWidth() && y <= this.y + this.getHeight()) {
                isDrag = true;
                return true;
            }
        }
        return false;
    }

    public void mouseDrag(int x, int y, boolean offscreen) {

        int diff = (originX - x) / sensitivity;
        double newValue = Math.floor((originValue - (diff * (originValue == 0 ? 1 : Math.abs(originValue) / 10f))) * 10f) / 10f;

        boolean change = true;

        if (originValue != newValue && listener != null) {
            change = listener.onValueChange(newValue);
        }

        if (change) {
            value = newValue;
        }

        return;
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);
        if (isDrag) {
            mouseDrag(x, y, offscreen);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        if (button == 0 && isDrag) {
            isDrag = false;
            return true;
        }
        return false;
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setValue(double value) {
        this.value = integer ? Math.floor(value) : value;
    }

    public void setListener(ValueChangeListener<Number> listener) {
        this.listener = listener;
    }

    public double getValue() {
        return value;
    }
}
