package com.unknown.xg42.gui.clickgui;
import com.unknown.xg42.XG42;
import com.unknown.xg42.gui.clickgui.component.Component;
import com.unknown.xg42.gui.clickgui.component.ModuleButton;
import com.unknown.xg42.gui.clickgui.component.SettingButton;
import com.unknown.xg42.gui.clickgui.guis.HUDEditorScreen;
import com.unknown.xg42.gui.clickgui.util.SpecialRender;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.HUDModule;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.font.CFontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Panel {
    public int x,y,width,height;
    public Category category;

    public boolean extended;
    boolean dragging;

    boolean isHUD;

    int x2,y2;

    CFontRenderer font;

    public List<ModuleButton> Elements = new ArrayList<>();

    public Panel(Category category, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.extended = true;
        this.dragging = false;
        this.category = category;
        isHUD = category.isHUD();
        font = XG42.getFontRenderer();
        setup();
    }

    public void setup() {
        for (IModule m : ModuleManager.getAllModules()) {
            if(m.category == category){
                Elements.add(new ModuleButton(m,width,height,this));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (this.dragging) {
            x = x2 + mouseX;
            y = y2 + mouseY;
        }

        int panelColor = 0x85000000;
        int GradientIntensity = GuiManager.getINSTANCE().getGradientIntensity();
        int color = new Color(GuiManager.getINSTANCE().getRed(),GuiManager.getINSTANCE().getGreen(),GuiManager.getINSTANCE().getBlue(),208).getRGB();

        RenderUtils.setColor(color);
        Gui.drawRect(x-4, y, x + width + 4, y + height, color);
        font.drawString(category.getName(), x + (width / 2F - font.getStringWidth(category.getName()) / 2F), y + height / 2F - font.getHeight() / 2f + 2, 0xffefefef);

        Gui.drawRect(x, y+height, x + width, y +height +1 , panelColor);

        if (this.extended && !Elements.isEmpty()) {

            int startY = y + height + 2;

            for (ModuleButton button : Elements){
                button.solvePos();
                button.y = startY;
                button.setAdd(((startY-y)/height)*GradientIntensity);
                button.render(mouseX,mouseY,partialTicks);
                int settingY = startY-1;
                startY += height + 1;
                if(button.isExtended){
                    for(Component component : button.settings){
                        if(component instanceof SettingButton) {
                            if(!((SettingButton<?>) component).getValue().visible()) continue;
                        }
                        component.solvePos();
                        component.y = startY;
                        component.setAdd(((startY-y)/height)*GradientIntensity);
                        component.render(mouseX,mouseY,partialTicks);
                        startY += height;
                    }
                    if(GuiManager.getINSTANCE().isSettingRect() || GuiManager.getINSTANCE().isSettingSide()) {
                        SpecialRender.drawSLine(x, settingY, startY, height, 2, ((settingY-y)/height)*GradientIntensity, GradientIntensity);
                    }
                    if(GuiManager.getINSTANCE().isSettingRect()) {
                        SpecialRender.drawSLine(x + width, settingY, startY, height, 2, ((settingY-y)/height)*GradientIntensity, GradientIntensity);
                        Gui.drawRect(x, settingY, x + width, settingY + 1, GuiManager.getINSTANCE().getRainbowColorAdd((long) ((settingY - y) / height) *GradientIntensity));
                        Gui.drawRect(x, startY - 1, x + width, startY, GuiManager.getINSTANCE().getRainbowColorAdd((long) ((startY - y) / height) *GradientIntensity));
                    }
                }
                startY += 1;
                if(button.module.isHUD && Wrapper.mc.currentScreen instanceof HUDEditorScreen) {
                    HUDModule hud = (HUDModule) button.module;
                    Gui.drawRect(hud.x, hud.y, hud.x + hud.width, hud.y + hud.height , panelColor);
                    hud.onRender();
                }
            }
            SpecialRender.draw(x-1, y+height, width+2, startY-1-height-y, height, 2.0f, GradientIntensity);
        }

    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY).test(this)) {
            x2 = this.x - mouseX;
            y2 = this.y - mouseY;
            dragging = true;
            if(!isHUD) {
                Collections.swap(GUIRender.getINSTANCE().panels, 0,GUIRender.getINSTANCE().panels.indexOf(this));
            } else {
                Collections.swap(HUDRender.getINSTANCE().panels, 0,HUDRender.getINSTANCE().panels.indexOf(this));
            }
            return true;
        }
        if (mouseButton == 1 && isHovered(mouseX, mouseY).test(this)) {
            extended = !extended;
            return true;
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0)
            this.dragging = false;
        for (Component part : Elements)
            part.mouseReleased(mouseX, mouseY, state);
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Component part : Elements)
            part.keyTyped(typedChar, keyCode);
    }

    public Predicate<Panel> isHovered(int mouseX, int mouseY) {
        return c -> mouseX >= Math.min(c.x,c.x + c.width) && mouseX <= Math.max(c.x,c.x+c.width)  && mouseY >= Math.min(c.y,c.y + c.height) && mouseY <= Math.max(c.y,c.y + c.height);
    }
}
