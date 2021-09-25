package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.function.Predicate;

public class IntegerSetting extends Setting<Integer> {
    protected Integer min, max;

    public IntegerSetting(String name, IModule contain, Integer defaultValue, Integer min, Integer max) {
        super(name, contain, defaultValue);
        this.min = min;
        this.max = max;
    }

    public IntegerSetting v(Predicate<Object> predicate) {
        return (IntegerSetting) super.v(predicate);
    }

    public IntegerSetting b(BooleanSetting value) {
        return (IntegerSetting) super.v(v -> value.getValue());
    }

    public IntegerSetting r(BooleanSetting value) {
        return (IntegerSetting) super.v(v -> !value.getValue());
    }

    public IntegerSetting c(double min, Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (IntegerSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (IntegerSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (IntegerSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
        }
        return (IntegerSetting) super.v(v -> true);
    }

    public IntegerSetting c(double min, Setting setting){
        if(setting instanceof IntegerSetting) {
            return (IntegerSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (IntegerSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (IntegerSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
        }
        return (IntegerSetting) super.v(v -> true);
    }

    public IntegerSetting c(Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (IntegerSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
        }
        if(setting instanceof FloatSetting) {
            return (IntegerSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
        }
        if(setting instanceof DoubleSetting) {
            return (IntegerSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
        }
        return (IntegerSetting) super.v(v -> true);
    }

    public IntegerSetting m(ModeSetting value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Integer getMin(){
        return min;
    }

    public Integer getMax() {
        return max;
    }
}