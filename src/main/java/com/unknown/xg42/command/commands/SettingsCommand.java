package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.ModuleParser;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.setting.ModeSetting;
import com.unknown.xg42.setting.Setting;

import java.util.List;

/**
 * Created by 086 on 11/12/2017.
 */
public class SettingsCommand extends Command {
    public SettingsCommand() {
        super("settings", new ChunkBuilder()
                .append("module", true, new ModuleParser())
                .build());
        setDescription("List the possible settings of a command");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module to display the settings of.");
            return;
        }

        IModule m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Couldn't find a module &b" + args[0] + "!");
            return;
        }

        List<Setting> settings = m.getSettingList();
        String[] result = new String[settings.size()];
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();

            if (setting instanceof ModeSetting) {
                result[i] += "  (";
                ModeSetting.Mode[] enums = (ModeSetting.Mode[]) ((ModeSetting) setting).getModes().toArray();
                for (ModeSetting.Mode e : enums)
                    result[i] += e.getName() + ", ";
                result[i] = result[i].substring(0, result[i].length() - 2) + ")";
            }
        }
        Command.sendStringChatMessage(result);
    }
}
