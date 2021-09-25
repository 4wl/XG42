package com.unknown.xg42.module.modules.client;

import com.unknown.xg42.gui.clickgui.guis.ClickGuiScreen;
import com.unknown.xg42.gui.settingpanel.XG42SettingPanel;
import com.unknown.xg42.manager.ConfigManager;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "SettingPanelUI",category = Category.CLIENT,keyCode = Keyboard.KEY_RSHIFT,visible = false)
public class SettingPanel extends Module {
    public static SettingPanel INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
        if (screen == null){
            setGUIScreen(new XG42SettingPanel());
        }
    }

    XG42SettingPanel screen;

    @Override
    public void onEnable() {
        if (mc.player != null){
            if (!(mc.currentScreen instanceof XG42SettingPanel)) {
                mc.displayGuiScreen(screen);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof XG42SettingPanel) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

    private void setGUIScreen(XG42SettingPanel screen){
        this.screen = screen;
    }

}