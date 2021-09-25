package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author S-B99
 * Updated by S-B99 on 07/02/20
 */
public class EnabledCommand extends Command {
    public EnabledCommand() {
        super("enabled", null);
        setDescription("Prints enabled modules");
    }

    @Override
    public void call(String[] args) {
        AtomicReference<String> enabled = new AtomicReference<>("");
        List<IModule> mods = new ArrayList<>(ModuleManager.getModules());

        mods.forEach(module -> {
            if (module.isEnabled()) {
                enabled.set(enabled + module.getName() + ", ");
            }
        });
        enabled.set(StringUtils.chop(StringUtils.chop(String.valueOf(enabled)))); // this looks horrible but I don't know how else to do it sorry
        Command.sendChatMessage("Enabled modules: \n" + TextFormatting.GRAY + enabled);
    }

}
