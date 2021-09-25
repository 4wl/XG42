package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.setting.ModeSetting;
import com.unknown.xg42.utils.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class ModeButton extends SettingButton<ModeSetting.Mode> {

    public ModeButton(ModeSetting value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int fontColor = 0x909090;

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        //Mode Name
        font.drawString(getAsModeValue().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f ) + 2, ColorUtil.getHoovered(new Color(255,255,255).getRGB(), isHovered(mouseX, mouseY)));

        //Mode Value
        font.drawString(getAsModeValue().getToggledMode().getName(),
                x + width - 1 - font.getStringWidth(getAsModeValue().getToggledMode().getName()), (int) (y + height / 2 - font.getHeight() / 2f ) + 2,
                ColorUtil.getHoovered(fontColor, isHovered(mouseX, mouseY)));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY) || !getValue().visible()) return false;
        if (mouseButton == 0) {
            getAsModeValue().forwardLoop();
        }
        return true;
    }
}
