package com.unknown.xg42.command.commands;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.ModuleParser;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.setting.BooleanSetting;
import com.unknown.xg42.setting.Setting;
import com.unknown.xg42.utils.Wrapper;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public static Setting<Boolean> modifiersEnabled = new BooleanSetting("modifiersEnabled",null, false);

    public static BindCommand INSTANCE;

    public BindCommand() {
        super("bind", new ChunkBuilder()
                .append("[module]|modifiers", true, new ModuleParser())
                .append("[key]|[on|off]", true)
                .build(),"b");
        setDescription("Binds a module to a key, or allows you to change modifier options");
        INSTANCE = this;
    }

    @Override
    public void call(String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }

        String module = args[0];
        String rkey = args[1];

        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                sendChatMessage("Expected: on or off");
                return;
            }

            if (rkey.equalsIgnoreCase("on")) {
                modifiersEnabled.setValue(true);
                sendChatMessage("Turned modifiers on.");
            } else if (rkey.equalsIgnoreCase("off")) {
                modifiersEnabled.setValue(false);
                sendChatMessage("Turned modifiers off.");
            } else {
                sendChatMessage("Expected: on or off");
            }
            return;
        }

        IModule m = ModuleManager.getModuleByName(module);

        if (m == null) {
            sendChatMessage("Unknown module '" + module + "'!");
            return;
        }

        if (rkey == null) {
            sendChatMessage(m.getName() + " is bound to &b" + Keyboard.getKeyName(m.getBind()));
            return;
        }

        int key = Wrapper.getKey(rkey);

        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }

        if (key == 0) {
            sendChatMessage("Unknown key '" + rkey + "'!");
            return;
        }

        m.setBind(key);
        sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
    }
}
