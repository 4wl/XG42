package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {
    protected Float min, max;

    public FloatSetting(String name, IModule contain, Float defaultValue, Float min, Float max) {
        super(name, contain, defaultValue);
        this.min = min;
        this.max = max;
    }

    public FloatSetting v(Predicate<Object> predicate) {
        return (FloatSetting) super.v(predicate);
    }

    public FloatSetting b(BooleanSetting value) {
        return (FloatSetting) super.v(v -> value.getValue());
    }

    public FloatSetting r(BooleanSetting value) {
        return (FloatSetting) super.v(v -> !value.getValue());
    }

    public FloatSetting c(double min, Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (FloatSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (FloatSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (FloatSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
        }
        return (FloatSetting) super.v(v -> true);
    }

    public FloatSetting c(double min, Setting setting){
        if(setting instanceof IntegerSetting) {
            return (FloatSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (FloatSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (FloatSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
        }
        return (FloatSetting) super.v(v -> true);
    }

    public FloatSetting c(Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (FloatSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
        }
        if(setting instanceof FloatSetting) {
            return (FloatSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
        }
        if(setting instanceof DoubleSetting) {
            return (FloatSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
        }
        return (FloatSetting) super.v(v -> true);
    }

    public FloatSetting m(ModeSetting value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Float getMin() {
        return min;
    }

    public Float getMax() { return max; }
}
