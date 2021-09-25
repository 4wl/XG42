package com.unknown.xg42.module.modules.client;

import com.unknown.xg42.gui.clickgui.guis.HUDEditorScreen;
import com.unknown.xg42.manager.ConfigManager;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "HUDEditor",category = Category.CLIENT,keyCode = Keyboard.KEY_GRAVE,visible = false)
public class HUDEditor extends Module {
    public static HUDEditor INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
    }

    HUDEditorScreen screen;

    @Override
    public void onEnable() {
        if (screen == null){
            setGUIScreen(new HUDEditorScreen());
        }
        if (mc.player != null){
            if (!(mc.currentScreen instanceof HUDEditorScreen)) {
                mc.displayGuiScreen(screen);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof HUDEditorScreen) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

    private void setGUIScreen(HUDEditorScreen screen){
        this.screen = screen;
    }

}
