package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class BindButton extends Component {

    IModule module;
    boolean accepting = false;

    public BindButton(IModule module, int width, int height, Panel father) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.father = father;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GuiManager.getINSTANCE().isRainbow() ? GuiManager.getINSTANCE().getRainbowColorAdd((long) add) :GuiManager.getINSTANCE().getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c = (accepting ? color : fontColor);
        if (isHovered(mouseX, mouseY)){
            c = (c & 0x7F7F7F) << 1;
        }

        font.drawString(accepting ? "Bind | ..." : "Bind | " + (module.keyCode == 0x00 ? "NONE" : Keyboard.getKeyName(module.getBind())) , x + 3, (int) (y + height / 2 - font.getHeight() / 2f ) + 2, c);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (accepting) {
            if (keyCode == ModuleManager.getModuleByName("ClickGUI").getBind()) {
                this.module.setBind(Keyboard.KEY_NONE);
            } else {
                this.module.setBind(keyCode);
            }
            accepting = false;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;

        if (mouseButton == 0) {
            accepting = true;
        }
        return true;

    }

}
