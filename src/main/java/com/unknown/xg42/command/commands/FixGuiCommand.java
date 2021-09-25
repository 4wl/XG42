
package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;

public class FixGuiCommand extends Command {
    public FixGuiCommand() {
        super("fixgui", new ChunkBuilder().build());
        setDescription("Allows you to disable the automatic gui positioning");
    }

    @Override
    public void call(String[] args) {
        sendChatMessage("[" + label + "] " + "Ran");
    }
}