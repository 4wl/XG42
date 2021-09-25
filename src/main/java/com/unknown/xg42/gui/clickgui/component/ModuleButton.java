package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.HUDModule;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.setting.*;
import com.unknown.xg42.utils.Rainbow;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Component {

    public List<Component> settings = new ArrayList<>();
    public IModule module;

    int x2, y2;
    boolean dragging;

    public ModuleButton(IModule module, int width, int height, Panel father) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.father = father;
        setup();
    }

    public void setup() {
        for (Setting<?> value : module.getSettingList()) {
            if (value instanceof BooleanSetting) settings.add(new BooleanButton((BooleanSetting) value, width, height, father));
            if (value instanceof IntegerSetting || value instanceof FloatSetting || value instanceof DoubleSetting)
                settings.add(new NumberSlider<>(value, width, height, father));
            if (value instanceof ModeSetting) settings.add(new ModeButton((ModeSetting) value, width, height, father));
        }
        settings.add(new BindButton(module,width,height,father));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        if(dragging){
            ((HUDModule)module).onDragging(mouseX,mouseY);
        }

        solveHUDPos(mouseX,mouseY);

        int color = GuiManager.getINSTANCE().isRainbow() ? GuiManager.getINSTANCE().getRainbowColorAdd((long) add) :GuiManager.getINSTANCE().getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        if (isHovered(mouseX, mouseY)) {
            color = (color & 0x7F7F7F) << 1;
        }

        //Background
        Gui.drawRect(x, y - 1, x + width, y + height + 1, 0x85000000);
        font.drawString(module.getName(), x + 1, (int) (y + height / 2 - font.getHeight() / 2f) + 2, module.isEnabled() ? color : fontColor);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (module.isHUD) {
            if (mouseButton == 0 && isHoveredHUD(mouseX, mouseY)) {
                x2 = this.module.x - mouseX;
                y2 = this.module.y - mouseY;
                dragging = true;
                return true;
            }
        }
        if (!isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            module.toggle();
        } else if (mouseButton == 1) {
            isExtended = !isExtended;
        }
        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0 && module.isHUD) {
            ((HUDModule)module).onMouseRelease();
            this.dragging = false;
        }
        for (Component setting : settings) {
            setting.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        for (Component setting : settings) {
            setting.keyTyped(typedChar, keyCode);
        }
    }

    public void solveHUDPos(int mouseX,int mouseY){
        if (module.isHUD && this.dragging) {
            module.x = x2 + mouseX;
            module.y = y2 + mouseY;
        }

        if (module.isHUD && !this.dragging) {
            if(Math.min(module.x,module.x + module.width) < 0){
                if(module.x < module.x + module.width){
                    module.x = 0;
                }else {
                    module.x = -module.width;
                }
            }

            if(Math.max(module.x,module.x + module.width) > mc.displayWidth/2){
                if(module.x < module.x + module.width){
                    module.x = mc.displayWidth/2 - module.width;
                }else {
                    module.x = mc.displayWidth/2;
                }
            }

            if(Math.min(module.y,module.y + module.height) < 0){
                if(module.y < module.y + module.height){
                    module.y = 0;
                }else {
                    module.y = -module.height;
                }
            }

            if(Math.max(module.y,module.y + module.height) > mc.displayHeight/2){
                if(module.y < module.y + module.height){
                    module.y = mc.displayHeight/2 - module.height;
                }else {
                    module.y = mc.displayHeight/2;
                }
            }
        }
    }

    public boolean isHoveredHUD(int mouseX, int mouseY) {
        return mouseX >= Math.min(module.x, module.x + module.width) && mouseX <= Math.max(module.x, module.x + module.width) && mouseY >= Math.min(module.y, module.y + module.height) && mouseY <= Math.max(module.y, module.y + module.height);
    }


}
