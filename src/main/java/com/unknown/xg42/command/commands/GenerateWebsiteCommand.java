package com.unknown.xg42.command.commands;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author S-B99
 * Updated by S-B99 on 18/03/20
 */
public class GenerateWebsiteCommand extends Command {
    public GenerateWebsiteCommand() {
        super("genwebsite", null);
        setDescription("Generates the module page for the website");
    }

    private static String nameAndDescription(IModule module) {
        return "<li>" + module.getName() + "<p><i>" + module.description + "</i></p></li>";
    }

    @Override
    public void call(String[] args) {
        List<IModule> mods = new ArrayList<>(ModuleManager.getModules());
        String[] modCategories = new String[]{"Combat", "Misc", "Movement", "Player", "Render", "Client"};
        List<String> modCategoriesList = new ArrayList<>(java.util.Arrays.asList(modCategories));

        List<String> modsClient = new ArrayList<>();
        List<String> modsCombat = new ArrayList<>();
        List<String> modsMisc = new ArrayList<>();
        List<String> modsMovement = new ArrayList<>();
        List<String> modsPlayer = new ArrayList<>();
        List<String> modsRender = new ArrayList<>();

        mods.forEach(module -> {
            switch (module.category) {
                case COMBAT:
                    modsCombat.add(nameAndDescription(module));
                case MISC:
                    modsMisc.add(nameAndDescription(module));
                case MOVEMENT:
                    modsMovement.add(nameAndDescription(module));
                case PLAYER:
                    modsPlayer.add(nameAndDescription(module));
                case RENDER:
                    modsRender.add(nameAndDescription(module));
                case CLIENT:
                    modsClient.add(nameAndDescription(module));
            }
        });

        modCategoriesList.forEach(modCategory -> {
            XG42.logger.info("<details>");
            XG42.logger.info("    <summary>" + modCategory + "</summary>");
            XG42.logger.info("    <p><ul>");
            mods.forEach(module -> {
                if (module.category.toString().equalsIgnoreCase(modCategory)) {
                    XG42.logger.info("        <li>" + module.getName() + "<p><i>" + module.description + "</i></p></li>");
                }
            });
            XG42.logger.info("    </ul></p>");
            XG42.logger.info("</details>");

        });

        Command.sendChatMessage(getLabel().substring(0, 1).toUpperCase() + getLabel().substring(1) + ": Generated website to log file!");
    }
}
