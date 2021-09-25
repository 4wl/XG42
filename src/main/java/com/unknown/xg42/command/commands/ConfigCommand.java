package com.unknown.xg42.command.commands;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.DependantParser;
import com.unknown.xg42.command.syntax.parsers.EnumParser;
import com.unknown.xg42.manager.ConfigManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by 086 on 14/10/2018.
 */
public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", new ChunkBuilder()
                .append("mode", true, new EnumParser(new String[]{"reload", "save", "path"}))
                .append("path", true, new DependantParser(0, new DependantParser.Dependency(new String[][]{{"path", "path"}}, "")))
                .build(), "cfg");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Missing argument &bmode&r: Choose from reload, save or path");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reload();
                break;
            case "save":
                try {
                    ConfigManager.saveAll();
                    Command.sendChatMessage("Saved configuration!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Command.sendChatMessage("Failed to save ! " + e.getMessage());
                }
                break;
            case "path":
                if (args[1] == null) {
                    Path file = Paths.get(ConfigManager.MODULE_CONFIG);
                    Command.sendChatMessage("Path to configuration: &b" + file.toAbsolutePath().toString());
                } else {
                    String newPath = args[1];
                    if (!isFilenameValid(newPath)) {
                        Command.sendChatMessage("&b" + newPath + "&r is not a valid path");
                        break;
                    }
                    try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("KAMILastConfig.txt"))) {
                        writer.write(newPath);
                        reload();
                        Command.sendChatMessage("Configuration path set to &b" + newPath + "&r!");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Command.sendChatMessage("Couldn't set path: " + e.getMessage());
                        break;
                    }
                }
                break;
            default:
                Command.sendChatMessage("Incorrect mode, please choose from: reload, save or path");
        }
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void reload() {
        ConfigManager.loadAll();
        Command.sendChatMessage("Configuration reloaded!");
    }

}
