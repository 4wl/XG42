package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.gui.settingpanel.utils.Utils;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.Locale;
import java.util.function.Function;

public class Slider extends AbstractComponent {
    private static final int PREFERRED_WIDTH = 180;
    private static final int PREFERRED_HEIGHT = 24;

    private int preferredWidth;
    private int preferredHeight;
    private boolean hovered;

    private double value;
    private double minValue;
    private double maxValue;

    private NumberType numberType;

    private ValueChangeListener<Number> listener;

    private boolean changing = false;

    public Slider(double value, double minValue, double maxValue, NumberType numberType, int preferredWidth, int preferredHeight) {
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numberType = numberType;

        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;

        setWidth(preferredWidth);
        setHeight(preferredHeight);
    }

    public Slider(double value, double minValue, double maxValue, NumberType numberType) {
        this(value, minValue, maxValue, numberType, PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    @Override
    public void render() {

        if (Window.isRainbow)
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
        else
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f, (hovered || changing) ? Window.SECONDARY_OUTLINE : Window.SECONDARY_FOREGROUND);
        int sliderWidth = 4;
        double sliderPos = (value - minValue) / (maxValue - minValue) * (getWidth() - sliderWidth);
        if (Window.isRainbow)
            RenderUtils.drawRoundedRectangle(x + sliderPos, y + 2, sliderWidth, getHeight() - 3, 2.0f, Rainbow.getRainbowColor(10, 0.8f, 1f));
        else
            RenderUtils.drawRoundedRectangle(x + sliderPos, y + 2, sliderWidth, getHeight() - 3, 2.0f, (hovered || changing) ? Window.TERTIARY_FOREGROUND : Window.SECONDARY_FOREGROUND);
        String text = numberType.getFormatter().apply(value);
        GLUtil.FontRenderer.drawString(text, x + getWidth() / 2f - GLUtil.FontRenderer.getStringWidth(text) / 2f, (y + getHeight() /2f) - GLUtil.FontRenderer.getHeight() / 4, Window.FOREGROUND.getRGB());
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);
        updateValue(x, y);
        return changing;
    }

    private void updateValue(int x, int y) {
        if (changing) {
            double oldValue = value;
            double newValue = Math.max(Math.min((x - this.x) / (double) getWidth() * (maxValue - minValue) + minValue, maxValue), minValue);

            boolean change = true;

            if (oldValue != newValue && listener != null) {
                change = listener.onValueChange(newValue);
            }

            if (change) {
                value = newValue;
            }
        }

    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            updateHovered(x, y, offscreen);

            if (hovered) {
                changing = true;

                updateValue(x, y);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            updateHovered(x, y, offscreen);

            if (changing) {
                changing = false;
                updateValue(x, y);
                return true;
            }
        }

        return false;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setListener(ValueChangeListener<Number> listener) {
        this.listener = listener;
    }

    public enum NumberType {
        PERCENT(number -> String.format(Locale.ENGLISH, "%.1f%%", number.floatValue())),
        TIME(number -> Utils.formatTime(number.longValue())),
        DECIMAL(number -> String.format(Locale.ENGLISH, "%.4f", number.floatValue())),
        INTEGER(number -> Long.toString(number.longValue()));

        private Function<Number, String> formatter;

        NumberType(Function<Number, String> formatter) {
            this.formatter = formatter;
        }

        public Function<Number, String> getFormatter() {
            return formatter;
        }
    }
}
