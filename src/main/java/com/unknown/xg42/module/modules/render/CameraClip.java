package com.unknown.xg42.module.modules.render;

import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.setting.BooleanSetting;
import com.unknown.xg42.setting.DoubleSetting;

@Module.Info(name = "CameraClip", category = Category.RENDER)
public class CameraClip extends Module {

    private static CameraClip INSTANCE;

    public BooleanSetting extend = bsetting("Extend", false);
    public DoubleSetting distance = dsetting("Distance", 0D, 0D, 500D);

    @Override
    public void onInit() {
        INSTANCE = this;
    }

    public static CameraClip getInstance() {
        if (CameraClip.INSTANCE == null) {
            CameraClip.INSTANCE = new CameraClip();
        }
        return CameraClip.INSTANCE;
    }
}
