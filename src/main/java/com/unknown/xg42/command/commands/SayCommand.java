package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;

/**
 * @author S-B99
 * Created by S-B99 on 12/03/20
 */
public class SayCommand extends Command {
    public SayCommand() {
        super("say", new ChunkBuilder().append("message").build());
        setDescription("Allows you to send any message, even with a prefix in it");
    }

    @Override
    public void call(String[] args) {
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            if (arg != null) {
                message.append(" ").append(arg);
            }
        }
        Command.sendServerMessage(message.toString());
    }
}