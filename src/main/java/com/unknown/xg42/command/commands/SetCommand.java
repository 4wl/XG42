package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.ModuleParser;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.setting.*;
import scala.xml.dtd.DFAContentModel;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by 086 on 18/11/2017.
 * Updated by S-B99 on 24/02/20
 */
public class SetCommand extends Command {

    public SetCommand() {
        super("set", new ChunkBuilder()
                .append("module", true, new ModuleParser())
                .append("setting", true)
                .append("value", true)
                .build());
        setDescription("Change the setting of a certain module");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }

        IModule m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            return;
        }

        if (args[1] == null) {
            String settings = String.join(", ", m.getSettingList().stream().map(setting -> setting.getName()).collect(Collectors.toList()));
            if (settings.isEmpty())
                Command.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
            else {
                Command.sendStringChatMessage(new String[]{
                        "Please specify a setting! Choose one of the following:", settings
                });
            }
            return;
        }

        Optional<Setting> optionalSetting = m.getSettingList().stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting.isPresent()) {
            Command.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
            return;
        }

        Setting setting = optionalSetting.get();

        if (args[2] == null) {
            Command.sendChatMessage("&b" + setting.getName() + "&r is a &3" + setting.getClass().getSimpleName() + "&r. Its current value is &3" + setting.getValue().toString());
            return;
        }

        try {
            String arg2 = args[2];
            if (setting.getClass().getSimpleName().equals("EnumSetting")) {
                arg2 = arg2.toUpperCase();
            }
            if (setting instanceof BooleanSetting){
                setting.setValue(Boolean.parseBoolean(arg2));
            }else if (setting instanceof DoubleSetting){
                setting.setValue(Double.parseDouble(arg2));
            }else if (setting instanceof FloatSetting){
                setting.setValue(Float.parseFloat(arg2));
            }else if (setting instanceof IntegerSetting){
                setting.setValue(Integer.parseInt(arg2));
            }else if (setting instanceof ModeSetting){
                setting.setValue(((ModeSetting) setting).getMode(arg2));
            }else if (setting instanceof StringSetting){
                setting.setValue(String.valueOf(arg2));
            }

            /* PLEASE MAKE SURE TO USE PROPER NAMING WHEN USING ENUMS */ /* if you use improper lowercase letters it will *not* work with this command ~S-B99 */
            Command.sendChatMessage("Set &b" + setting.getName() + "&r to &3" + arg2 + "&r.");
        } catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Unable to set value! &6" + e.getMessage());
        }
    }
}
