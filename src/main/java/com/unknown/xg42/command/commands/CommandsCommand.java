package com.unknown.xg42.command.commands;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.SyntaxChunk;

import java.util.Comparator;

/**
 * Created by 086 on 12/11/2017.
 */
public class CommandsCommand extends Command {

    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY, "cmds");
        setDescription("Gives you this list of commands");
    }

    @Override
    public void call(String[] args) {
        XG42.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command ->
                Command.sendChatMessage("&f" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &7" + command.getDescription())
        );
    }
}
