package com.unknown.xg42.module;

import com.unknown.xg42.setting.ModeSetting;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Module extends IModule {

    ModeSetting visible_value;

    public boolean isShownOnArray(){
        return this.visible_value.getMode("ON").isToggled();
    }

    public Module(){
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.description = getAnnotation().description();
        this.keyCode = getAnnotation().keyCode();
        this.getSettingList().add(visible_value = new ModeSetting("Visible", this,new ModeSetting.Mode("ON",getAnnotation().visible()),new ModeSetting.Mode("OFF",!getAnnotation().visible())));
        this.isHUD = false;
        this.onInit();
    }

    public void onInit(){}

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        String description() default "";
        int keyCode() default Keyboard.KEY_NONE;
        Category category();
        boolean visible() default true;
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public static boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }
    public static boolean nullCheck() {
        return mc.player == null;
    }

}