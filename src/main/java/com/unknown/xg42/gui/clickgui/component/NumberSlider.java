package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.setting.DoubleSetting;
import com.unknown.xg42.setting.FloatSetting;
import com.unknown.xg42.setting.IntegerSetting;
import com.unknown.xg42.setting.Setting;
import com.unknown.xg42.utils.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class NumberSlider<T> extends SettingButton<T> {

    boolean sliding = false;

    public NumberSlider(Setting<T> value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!getValue().visible())
            sliding = false;

        int color = GuiManager.getINSTANCE().isRainbow() ? GuiManager.getINSTANCE().getRainbowColorAdd((long) add, 192) : GuiManager.getINSTANCE().getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height,0x85000000);

        double iwidth = 0;
        String displayvalue = "0";

        int sliderWidth = (width - 2);

        if (getValue() instanceof DoubleSetting) {
            DoubleSetting doubleValue = (DoubleSetting) getValue();
            displayvalue = String.format("%.1f", doubleValue.getValue());

            double percentBar = (doubleValue.getValue() - doubleValue.getMin()) / (doubleValue.getMax() - doubleValue.getMin());

            iwidth = sliderWidth * percentBar;
        } else if (getValue() instanceof FloatSetting) {
            FloatSetting floatValue = (FloatSetting) getValue();
            displayvalue = String.format("%.1f", floatValue.getValue());

            double percentBar = (floatValue.getValue() - floatValue.getMin()) / (floatValue.getMax() - floatValue.getMin());

            iwidth = sliderWidth * percentBar;
        } else if (getValue() instanceof IntegerSetting) {
            IntegerSetting intValue = (IntegerSetting) getValue();
            displayvalue = String.valueOf(intValue.getValue());

            double percentBar = (double) (intValue.getValue() - intValue.getMin()) / (double) (intValue.getMax() - intValue.getMin());

            iwidth = sliderWidth * percentBar;
        }

        Gui.drawRect(x + 1, y + 1, x + 1 + (int) iwidth, y + height, color);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.sliding) {
            if (getValue() instanceof DoubleSetting) {
                DoubleSetting doubleValue = (DoubleSetting) getValue();
                double diff = doubleValue.getMax() - doubleValue.getMin();
                double val = doubleValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                doubleValue.setValue(val);
            } else if (getValue() instanceof FloatSetting) {
                FloatSetting floatValue = (FloatSetting) getValue();
                double diff = floatValue.getMax() - floatValue.getMin();
                double val = floatValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                floatValue.setValue((float) val);
            } else if (getValue() instanceof IntegerSetting) {
                IntegerSetting intValue = (IntegerSetting) getValue();
                double diff = intValue.getMax() - intValue.getMin();
                double val = intValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                intValue.setValue((int) val);
            }
        }

        font.drawString(getValue().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, fontColor);
        font.drawString(String.valueOf(displayvalue), x + width - 1 - font.getStringWidth(String.valueOf(displayvalue)), (int) (y + height / 2 - font.getHeight() / 2f) + 2, ColorUtil.getHoovered(0x909090,isHovered(mouseX, mouseY)));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (!getValue().visible() || !isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            this.sliding = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }


}
