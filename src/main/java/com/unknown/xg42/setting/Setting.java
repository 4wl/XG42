package com.unknown.xg42.setting;

import com.unknown.xg42.event.events.client.SettingChangeEvent;
import com.unknown.xg42.module.IModule;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Setting<T> {

    private T value;
    private final T defaultValue;
    List<Predicate<Object>> visibility = new ArrayList<>();
    private final String name;
    private final IModule contain;

    public Setting(String name, IModule contain, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.contain = contain;
        this.visibility.add(V -> true);
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean visible() {
        for(Predicate<Object> predicate : visibility){
            if(!predicate.test(this)) return false;
        }
        return true;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        SettingChangeEvent event = new SettingChangeEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.value = value;
        }
    }

    public IModule getContain(){
        return contain;
    }

    public Setting<T> v(Predicate<Object> predicate) {
        this.visibility.add(predicate);
        return this;
    }

}
