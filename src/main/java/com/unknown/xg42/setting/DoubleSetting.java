package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.function.Predicate;

public class DoubleSetting extends Setting<Double> {

    protected Double min, max;

    public DoubleSetting(String name, IModule contain, Double defaultValue, Double min, Double max) {
        super(name, contain, defaultValue);
        this.min = min;
        this.max = max;
    }

    public DoubleSetting v(Predicate<Object> predicate) {
        return (DoubleSetting) super.v(predicate);
    }

    public DoubleSetting b(BooleanSetting value) {
        return (DoubleSetting) super.v(v -> value.getValue());
    }

    public DoubleSetting r(BooleanSetting value) {
        return (DoubleSetting) super.v(v -> !value.getValue());
    }

    public DoubleSetting c(double min, Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (DoubleSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (DoubleSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (DoubleSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
        }
        return (DoubleSetting) super.v(v -> true);
    }

    public DoubleSetting c(double min, Setting setting){
        if(setting instanceof IntegerSetting) {
            return (DoubleSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
        }
        if(setting instanceof FloatSetting) {
            return (DoubleSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
        }
        if(setting instanceof DoubleSetting) {
            return (DoubleSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
        }
        return (DoubleSetting) super.v(v -> true);
    }

    public DoubleSetting c(Setting setting, double max){
        if(setting instanceof IntegerSetting) {
            return (DoubleSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
        }
        if(setting instanceof FloatSetting) {
            return (DoubleSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
        }
        if(setting instanceof DoubleSetting) {
            return (DoubleSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
        }
        return (DoubleSetting) super.v(v -> true);
    }

    public DoubleSetting m(ModeSetting value, String mode){
        this.visibility.add(v -> value.getMode(mode).isToggled());
        return this;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }
}
