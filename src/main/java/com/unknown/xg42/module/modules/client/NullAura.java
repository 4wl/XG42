package com.unknown.xg42.module.modules.client;

import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.setting.BooleanSetting;
import com.unknown.xg42.setting.IntegerSetting;

@Module.Info(name = "Null Aura", category = Category.CLIENT, description = "java.lang.NullPointerException")
public class NullAura extends Module {

    IntegerSetting level = isetting("Level", 1, 1, 25);
    BooleanSetting crash = bsetting("Crash", false);

    @Override
    public void onInit() {

    }

    @Override
    public void onEnable() {
        try {
            for (int c=0; c < level.getValue();c++)
                throw new NullPointerException();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (crash.getValue()) {
            crash.setValue(false);
            throw new NullPointerException("null");
        }
        disable();
    }

    @Override
    public void onConfigLoad() {
        crash.setValue(false);
    }

    @Override
    public void onConfigSave() {
        crash.setValue(false);
    }
}
