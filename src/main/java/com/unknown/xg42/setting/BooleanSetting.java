package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.function.Predicate;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, IModule contain, Boolean defaultValue) {
        super(name, contain, defaultValue);
    }

    public BooleanSetting v(Predicate<Object> predicate) {
        return (BooleanSetting) super.v(predicate);
    }

    public BooleanSetting b(BooleanSetting value) {
        return (BooleanSetting) super.v(v -> value.getValue());
    }

    public BooleanSetting r(BooleanSetting value) {
        return (BooleanSetting) super.v(v -> !value.getValue());
    }

    public BooleanSetting c(double min, Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (BooleanSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (BooleanSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (BooleanSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
        }
        return (BooleanSetting) super.v(v -> true);
    }

    public BooleanSetting c(double min, Setting setting){
        if(setting instanceof IntegerSetting) {
            return (BooleanSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (BooleanSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (BooleanSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
        }
        return (BooleanSetting) super.v(v -> true);
    }

    public BooleanSetting c(Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (BooleanSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
        }
        if(setting instanceof FloatSetting) {
            return (BooleanSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
        }
        if(setting instanceof DoubleSetting) {
            return (BooleanSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
        }
        return (BooleanSetting) super.v(v -> true);
    }

    public BooleanSetting m(ModeSetting value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

}
