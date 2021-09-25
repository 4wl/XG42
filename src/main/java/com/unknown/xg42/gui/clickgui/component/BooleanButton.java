package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.setting.BooleanSetting;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class BooleanButton extends SettingButton<Boolean> {

    public BooleanButton(BooleanSetting value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {


        int color = GuiManager.getINSTANCE().isRainbow() ? GuiManager.getINSTANCE().getRainbowColorAdd((long) add) :GuiManager.getINSTANCE().getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c = (getValue().getValue() ? color : fontColor);

        if (isHovered(mouseX, mouseY)){
            c = (c & 0x7F7F7F) << 1;
        }

        final BooleanSetting booleanValue = (BooleanSetting) getValue();

        font.drawString(booleanValue.getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);

    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!getValue().visible() || !isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            this.getValue().setValue(!getValue().getValue());
        }
        return true;
    }

}
