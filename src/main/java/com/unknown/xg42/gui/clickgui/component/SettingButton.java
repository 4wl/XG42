package com.unknown.xg42.gui.clickgui.component;

import com.unknown.xg42.setting.ModeSetting;
import com.unknown.xg42.setting.Setting;


public abstract class SettingButton<T> extends Component {
    private Setting<T> value;
    public Setting<T> getValue(){
        return value;
    }
    public ModeSetting getAsModeValue(){
        return (ModeSetting) value;
    }
    public void setValue(Setting<T> value){
        this.value = value;
    }
}
