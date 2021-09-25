package com.unknown.xg42.module;

import com.unknown.xg42.XG42;
import com.unknown.xg42.event.events.client.SettingChangeEvent;
import com.unknown.xg42.event.events.render.RenderEvent;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.font.CFontRenderer;
import com.unknown.xg42.setting.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class IModule {

    public CFontRenderer font = XG42.getFontRenderer();
    public String name;
    public boolean toggled;
    public String description;
    public Category category;
    public int keyCode;

    //HUD
    public boolean isHUD;
    public int x;
    public int y;
    public int width;
    public int height;

    private final ArrayList<Setting> settings = new ArrayList<>();
    public ArrayList<Setting> getSettingList() {
        return settings;
    }

    public static final Minecraft mc = Wrapper.mc;
    public static final FontRenderer fontRenderer = mc.fontRenderer;

    public String getName(){
        return name;
    }

    public void enable() {
        toggled = true;
        MinecraftForge.EVENT_BUS.register(this);
        //if(!isHUD) NotificationManager.addNewNotification(this,this.toggled);
        //if(Notification.INSTANCE.chat.getValue()) ChatUtil.sendNoSpamMessage(name + " has been " + ChatUtil.SECTIONSIGN + "a" + "Enabled!");
        onEnable();
    }

    public void disable() {
        toggled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        //if(!isHUD) NotificationManager.addNewNotification(this,this.toggled);
        //if(Notification.INSTANCE.chat.getValue()) ChatUtil.sendNoSpamMessage(name + " has been " + ChatUtil.SECTIONSIGN + "c" + "Disabled!");
        onDisable();
    }

    public BooleanSetting bsetting(String name, boolean defaultValue){
        BooleanSetting value = new BooleanSetting(name,this,defaultValue);
        this.getSettingList().add(value);
        return value;
    }

    public IntegerSetting isetting(String name, int defaultValue, int minValue, int maxValue){
        IntegerSetting value = new IntegerSetting(name,this,defaultValue,minValue,maxValue);
        this.getSettingList().add(value);
        return value;
    }

    public FloatSetting fsetting(String name, float defaultValue, float minValue, float maxValue){
        FloatSetting value = new FloatSetting(name,this,defaultValue,minValue,maxValue);
        this.getSettingList().add(value);
        return value;
    }

    public DoubleSetting dsetting(String name, double defaultValue, double minValue, double maxValue){
        DoubleSetting value = new DoubleSetting(name,this,defaultValue,minValue,maxValue);
        this.getSettingList().add(value);
        return value;
    }

    public ModeSetting msetting(String name, ModeSetting.Mode... modes){
        ModeSetting value = new ModeSetting(name,this,modes);
        this.getSettingList().add(value);
        return value;
    }

    public StringSetting ssetting(String name, String defaultValue){
        StringSetting value = new StringSetting(name,this,defaultValue);
        this.getSettingList().add(value);
        return value;
    }

    public boolean isEnabled(){
        return toggled;
    }
    public boolean isDisabled(){
        return !toggled;
    }

    public void onConfigLoad(){}
    public void onConfigSave(){}

    public void onEnable(){}
    public void onDisable(){}

    public void onTick(){}
    public void onRender(){}

    public void onRender2D(RenderGameOverlayEvent.Post event){}

    public void onWorldRender(RenderEvent event){}

    public String getHudInfo(){ return ""; }

    public void toggle(){
        SettingChangeEvent event = new SettingChangeEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            toggled = !toggled;
        }
        if (toggled){
            enable();
        } else {
            disable();
        }
    }

    public void onKey(InputUpdateEvent event){}

    public int getBind(){
        return keyCode;
    }

    public void setBind(int keycode){
        this.keyCode = keycode;
    }

    public void setEnable(boolean toggled){
        this.toggled = toggled;
    }

}
