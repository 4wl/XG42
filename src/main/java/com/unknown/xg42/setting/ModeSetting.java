package com.unknown.xg42.setting;

import com.unknown.xg42.module.IModule;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ModeSetting extends Setting<ModeSetting.Mode> {
	
	private final List<Mode> modes;
	private final String modeName;
	private int index;

    public List<Mode> getModes() {
		return modes;
	}

	public ModeSetting(String modeName, IModule contain, Mode... modes) {
        super(modeName, contain, null);
        this.modeName = modeName;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(getToggledMode());
    }

	public ModeSetting(String modeName, IModule contain, List<Mode> modes) {
		super(modeName, contain, null);
		this.modeName = modeName;
		this.modes = modes;
		index = this.modes.indexOf(getToggledMode());
	}

	public ModeSetting v(Predicate<Object> predicate) {
		return (ModeSetting) super.v(predicate);
	}

	public ModeSetting b(BooleanSetting value) {
		return (ModeSetting) super.v(v -> value.getValue());
	}

	public ModeSetting r(BooleanSetting value) {
		return (ModeSetting) super.v(v -> !value.getValue());
	}

	public ModeSetting c(double min, Setting setting, double max){
		if(setting instanceof IntegerSetting) {
			return (ModeSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max && ((IntegerSetting) setting).getValue() >= min);
		}
		if(setting instanceof FloatSetting) {
			return (ModeSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max && ((FloatSetting) setting).getValue() >= min);
		}
		if(setting instanceof DoubleSetting) {
			return (ModeSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max && ((DoubleSetting) setting).getValue() >= min);
		}
		return (ModeSetting) super.v(v -> true);
	}

	public ModeSetting c(double min, Setting setting){
		if(setting instanceof IntegerSetting) {
			return (ModeSetting) super.v(v -> ((IntegerSetting) setting).getValue() >= min);
		}
		if(setting instanceof FloatSetting) {
			return (ModeSetting) super.v(v -> ((FloatSetting) setting).getValue() >= min);
		}
		if(setting instanceof DoubleSetting) {
			return (ModeSetting) super.v(v -> ((DoubleSetting) setting).getValue() >= min);
		}
		return (ModeSetting) super.v(v -> true);
	}

	public ModeSetting c(Setting setting, double max){
		if(setting instanceof IntegerSetting) {
			return (ModeSetting) super.v(v -> ((IntegerSetting) setting).getValue() <= max);
		}
		if(setting instanceof FloatSetting) {
			return (ModeSetting) super.v(v -> ((FloatSetting) setting).getValue() <= max);
		}
		if(setting instanceof DoubleSetting) {
			return (ModeSetting) super.v(v -> ((DoubleSetting) setting).getValue() <= max);
		}
		return (ModeSetting) super.v(v -> true);
	}

	public ModeSetting m(ModeSetting value, String mode){
		this.visibility.add(v -> value.getMode(mode).isToggled());
		return this;
	}

	public boolean page(String page) {
		return getMode(page).isToggled();
	}


    public Mode getMode(String name) {
    	Mode m = null;
    	for(Mode mode : modes) {
    		if(mode.getName().equals(name)) {
    			m = mode;
    		}
    	}
    	return m;
    }

    public Mode getToggledMode(){
		Mode m = null;
		for(Mode mode : modes) {
			if(mode.isToggled()) {
				m = mode;
			}
		}
		return m;
	}

	public void setModeWithName(String s){
		for(Mode mode : modes) {
			mode.setToggled(mode.getName().equals(s));
		}
	}

	public void forwardLoop() {
		if (index < modes.size() - 1) {
			index++;
		} else {
			index = 0;
		}
		for (Mode mode : modes) {
			mode.setToggled(mode == modes.get(index));
		}
	}

	public String getModeName() {
		return modeName;
	}

	public static class Mode {

		private String name;
		private boolean toggled;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isToggled() {
			return toggled;
		}
		public void setToggled(boolean toggled) {
			this.toggled = toggled;
		}
		public Mode(String name, boolean toggled) {
			this.name = name;
			this.toggled = toggled;
		}

		public Mode(String name) {
			this.name = name;
			this.toggled = false;
		}

	}
}
