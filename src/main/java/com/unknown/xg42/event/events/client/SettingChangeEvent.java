package com.unknown.xg42.event.events.client;

import com.unknown.xg42.event.EventStage;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.setting.Setting;

public class SettingChangeEvent extends EventStage {
    private IModule iModule;
    private Setting setting;

    public SettingChangeEvent(int stage, IModule iModule) {
        super(stage);
        this.iModule = iModule;
    }

    public SettingChangeEvent(Setting setting) {
        super(2);
        this.setting = setting;
    }

    public IModule getModule() {
        return this.iModule;
    }

    public Setting getSetting() {
        return this.setting;
    }
}
