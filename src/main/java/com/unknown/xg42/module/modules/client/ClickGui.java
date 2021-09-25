package com.unknown.xg42.module.modules.client;

import com.unknown.xg42.gui.clickgui.guis.ClickGuiScreen;
import com.unknown.xg42.manager.ConfigManager;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "ClickGUI",category = Category.CLIENT,keyCode = Keyboard.KEY_U,visible = false)
public class ClickGui extends Module {
    public static ClickGui INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
        if (screen == null){
            setGUIScreen(new ClickGuiScreen());
        }
    }

    ClickGuiScreen screen;

    @Override
    public void onEnable() {
        if (mc.player != null){
            if (!(mc.currentScreen instanceof ClickGuiScreen)) {
                mc.displayGuiScreen(screen);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof ClickGuiScreen) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

    private void setGUIScreen(ClickGuiScreen screen){
        this.screen = screen;
    }

}
