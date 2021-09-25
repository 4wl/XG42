package com.unknown.xg42.module.modules.client;

import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.setting.BooleanSetting;
import com.unknown.xg42.setting.FloatSetting;
import com.unknown.xg42.setting.IntegerSetting;
import com.unknown.xg42.setting.ModeSetting;

@Module.Info(name = "Colors",category = Category.CLIENT,visible = false)
public class Colors extends Module {

    public IntegerSetting red = isetting("Red",255,0,255);
    public IntegerSetting green = isetting("Green",0,0,255);
    public IntegerSetting blue = isetting("Blue",0,0,255);
    public BooleanSetting particle = bsetting("Particle",true);
    public BooleanSetting rainbow = bsetting("Rainbow",false);
    public FloatSetting rainbowSpeed = fsetting("Rainbow Speed", 5.0f,0.0f,30.0f).b(rainbow);
    public FloatSetting rainbowSaturation = fsetting("Saturation",0.65f,0.0f,1.0f).b(rainbow);
    public FloatSetting rainbowBrightness = fsetting("Brightness",1.0f,0.0f,1.0f).b(rainbow);
    public IntegerSetting GradientIntensity = isetting("G Intensity",50,0,500).b(rainbow);
    public ModeSetting background = msetting("Background",new ModeSetting.Mode("Shadow",true),new ModeSetting.Mode("Blur"),new ModeSetting.Mode("Both"),new ModeSetting.Mode("None"));
    public ModeSetting setting = msetting("Setting",new ModeSetting.Mode("Rect"),new ModeSetting.Mode("Side",true),new ModeSetting.Mode("None"));

    static Colors INSTANCE;

    public static Colors getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onInit(){
        INSTANCE = this;
    }

}
