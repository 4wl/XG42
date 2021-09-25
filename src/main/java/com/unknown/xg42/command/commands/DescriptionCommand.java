package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;

/**
 * @author S-B99
 * Created by S-B99 on 17/02/20
 */
public class DescriptionCommand extends Command {
    public DescriptionCommand() {
        super("description", new ChunkBuilder().append("module").build(), "tooltip");
        setDescription("Prints a module's description into the chat");
    }

    @Override
    public void call(String[] args) {
        for (String s : args) {
            if (s == null)
                continue;
            IModule module = ModuleManager.getModuleByName(s);
            Command.sendChatMessage(module.getName() + "Description: &7" + module.description);
        }
    }
}
