package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.function.Predicate;

public class StringSetting extends Setting<String> {

    public StringSetting(String name, IModule contain, String defaultValue) {
        super(name, contain, defaultValue);
    }

    public StringSetting v(Predicate<Object> predicate) {
        return (StringSetting) super.v(predicate);
    }

    public StringSetting b(BooleanSetting value) {
        return (StringSetting) super.v(v -> value.getValue());
    }

    public StringSetting r(BooleanSetting value) {
        return (StringSetting) super.v(v -> !value.getValue());
    }

    public StringSetting c(double min, Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (StringSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (StringSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (StringSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
        }
        return (StringSetting) super.v(v -> true);
    }

    public StringSetting c(double min, Setting setting){
        if(setting instanceof IntegerSetting) {
            return (StringSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (StringSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (StringSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
        }
        return (StringSetting) super.v(v -> true);
    }

    public StringSetting c(Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (StringSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
        }
        if(setting instanceof FloatSetting) {
            return (StringSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
        }
        if(setting instanceof DoubleSetting) {
            return (StringSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
        }
        return (StringSetting) super.v(v -> true);
    }

    public StringSetting m(ModeSetting value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }
}
